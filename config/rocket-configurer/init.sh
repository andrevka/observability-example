for i in {1..20};
  do curl --request POST \
  --url http://rocket:3000/api/v1/login \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{
  "user": "admin",
  "password": "admin"
}' > authentication && break || sleep 10;
done;

cat authentication
echo ""
echo "X-User-Id: $(jq -r .data.userId authentication)"
echo "X-Auth-Token: $(jq -r .data.authToken authentication)"

if [ ! -f user_created ]; then
  curl --request POST \
    --url http://rocket:3000/api/v1/users.create \
    --header "X-User-Id: $(jq -r .data.userId authentication)" \
    --header "X-Auth-Token: $(jq -r .data.authToken authentication)" \
    --header 'accept: application/json' \
    --header 'content-type: application/json' \
    --fail \
    --data '{
             "name": "Test User",
             "email": "email@user.tld1",
             "password": "myuser",
             "username": "myuser",
             "active": true,
             "nickname": "testusername",
             "bio": "All about the user",
             "joinDefaultChannels": true,
             "statusText": "On a vacation",
             "roles": [
              "bot", "user"
             ],
             "requirePasswordChange": false,
             "setRandomPassword": false,
             "sendWelcomeEmail": false,
             "verified": false,
             "customFields": {
              "clearance": "High",
              "team": "Queen"
             }
            }'
  if [ $? != 0 ]; then
      exit 100
  fi
  echo "user_created" > user_created
fi

if [ ! -f channel_created ]; then
  curl --request POST \
    --url http://rocket:3000/api/v1/channels.create \
    --header "X-User-Id: $(jq -r .data.userId authentication)" \
    --header "X-Auth-Token: $(jq -r .data.authToken authentication)" \
    --header 'accept: application/json' \
    --header 'content-type: application/json' \
    --fail \
    --data '{
              "name": "Monitoring",
              "members": [
                "myuser"
              ],
              "readOnly": false,
              "excludeSelf": true
            }'
  if [ $? != 0 ]; then
      exit 100
  fi
  echo "channel_created" > channel_created
fi

if [ ! -f monitoring_integration_created ]; then
  curl --request POST \
    --url http://rocket:3000/api/v1/integrations.create \
    --header "X-User-Id: $(jq -r .data.userId authentication)" \
    --header "X-Auth-Token: $(jq -r .data.authToken authentication)" \
    --header 'accept: application/json' \
    --header 'content-type: application/json' \
    --fail \
    -o integration \
    -v \
    --data '{
              "type": "webhook-incoming",
              "username": "admin",
              "channel": "#Monitoring",
              "scriptEnabled": true,
              "script": "class Script { process_incoming_request({ request }) { console.log(request.content.groupLabels.alertname); return { content:{ text: request.content.status + \": \" + request.content.groupLabels.alertname } }; }}",
              "name": "monitoring-incoming-messages",
              "enabled": true
            }'
  if [ $? != 0 ]; then
      exit 100
  fi
  echo "monitoring_integration_created" > monitoring_integration_created
fi

yq_command="'.receivers[0].webhook_configs[0].url=\"http://rocketchat:3000/hooks/$(jq -r .integration._id integration)/$(jq -r .integration.token integration)\"'"
eval "yq e -i $yq_command /etc/alertmanager.yml"