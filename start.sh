#!/bin/sh

port=8080

lsof -i:$port

echo "start process at :${port}..."

# 找不到java
source /etc/profile

mainjar=$(find . -type f -name '*.jar')
nohup java -jar $mainjar --server.port=$port --spring.profiles.active=test >catalina.out 2>&1 &

echo 'wait 15s...'
# gitlab-runner may exit 1
# sleep 8s

lsof -i:$port

echo 'start success!'
