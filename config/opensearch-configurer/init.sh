for i in {1..20};
  do curl --request GET --insecure --fail -H "Connection: close" \
   -u dashboards:dashboards -v \
   --url https://opensearch:9200/_cluster/health?wait_for_status=yellow \
&& break || sleep 10;
done;


if [ ! -f push_gateway_channel ]; then
  curl --request POST --insecure -u dashboards:dashboards -v --url https://opensearch:9200/_plugins/_notifications/configs/ \
    --header 'Content-Type: application/json' \
    --fail \
    --data '{
              "config_id": "prometheus-push-gateway",
              "name": "Prometheus push gateway",
              "config": {
                "name": "Prometheus push gateway channel",
                "description": "Channel for sending prometheus formated metrics to prometheus push gateway",
                "config_type": "webhook",
                "is_enabled": true,
                "webhook": {
                  "url": "http://prometheus-push-gateway:9091/metrics/job/log_alerts"
                }
              }
            }'
  if [ $? != 0 ]; then
      exit 100
  fi
  echo "push_gateway_channel" > push_gateway_channel
fi

if [ ! -f bad_logs_monitor ]; then
  curl --request POST -u dashboards:dashboards --insecure -v --url https://opensearch:9200/_plugins/_alerting/monitors \
    --header 'Content-Type: application/json' \
    --fail \
    --data '{
              "type": "monitor",
              "name": "bad-logs-monitor",
              "monitor_type": "query_level_monitor",
              "enabled": true,
              "schedule": {
                "period": {
                  "interval": 1,
                  "unit": "MINUTES"
                }
              },
              "inputs": [
                {
                  "search": {
                    "indices": [
                      "logstash-all-logs-*"
                    ],
                    "query": {
                      "size": 1,
                      "sort": [{"application_timestamp": "desc"}],
                      "script_fields": {
                        "unix_timestamp": {
                          "script": {
                            "lang": "painless",
                            "source": "doc[\"application_timestamp\"].value.toInstant().toEpochMilli() / 1000"
                          }
                        }
                      },
                      "query": {
                        "bool": {
                          "filter": [
                            {
                              "query_string": {
                                "query": "\"*Bad log*\""
                              }
                            },
                            {
                              "range": {
                                "application_timestamp": {
                                  "from": "{{period_end}}||-2m",
                                  "to": "{{period_end}}",
                                  "include_lower": true,
                                  "include_upper": true,
                                  "format": "epoch_millis",
                                  "boost": 1
                                }
                                }
                              }
                            ],
                            "adjust_pure_negative": true,
                            "boost": 1
                          }
                        }
                      }
                    }
                  }
              ],
              "triggers": [
                {
                  "name": "bad_logs_triggers",
                  "severity": "1",
                  "condition": {
                    "script": {
                      "source": "ctx.results[0].hits.total.value > 0",
                      "lang": "painless"
                    }
                  },
                  "actions": [
                    {
                      "name": "notify-prometheus",
                      "destination_id": "prometheus-push-gateway",
                      "message_template": {
                        "source": "bad_logs_last_timestamp {{ ctx.results.0.hits.hits.0.fields.unix_timestamp.0 }}\n"
                      }
                    }
                  ]
                }
              ]
            }'
  if [ $? != 0 ]; then
      exit 100
  fi
  echo "bad_logs_monitor" > bad_logs_monitor
fi

if [ ! -f index_pattern ]; then
  for i in {0..20};
    do curl --request POST -u dashboards:dashboards -v --insecure --url 'http://opensearch-dashboards:5601/api/saved_objects/index-pattern' -H 'Content-Type: application/json' -H 'osd-version: 2.18.0' -H 'osd-xsrf: osd-fetch' --data-raw '{"attributes":{"title":"logstash-all-logs-*","timeFieldName":"application_timestamp"}}' && break || sleep 10;
  done;
  if [ $? != 0 ]; then
      exit 100
  fi
  echo "index_pattern" > index_pattern
fi
