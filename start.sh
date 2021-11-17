#!/bin/sh

lsof -i:8080

echo 'start process...'

# 找不到java
source /etc/profile

mainjar=$(find . -type f -name '*.jar')
nohup java -jar $mainjar --spring.profiles.active=test >catalina.out 2>&1 &

echo 'wait 15s...'
# gitlab-runner may exit 1
# sleep 8s

lsof -i:8080

echo 'start success!'
