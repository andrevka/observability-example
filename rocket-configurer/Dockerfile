FROM ubuntu:noble-20241015 as builder

RUN apt-get update
RUN apt-get install --assume-yes curl
RUN apt-get install --assume-yes jq
RUN curl -L https://github.com/mikefarah/yq/releases/download/v4.44.5/yq_linux_amd64 -o /usr/bin/yq
RUN chmod +x /usr/bin/yq

ENTRYPOINT ["bash", "/etc/init.sh"]