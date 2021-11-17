#!/bin/sh

#ps -ef|grep rxapi|grep -v grep|awk '{print $2}'|xargs kill -9

lsof -i:8080

#kill process at port 8080
pid=$(lsof -i:8080|grep 'java'|grep -v 'grep'|awk '{print $2}')
if [ "$pid" != "" ]; then
  lsof -i:8080|grep java|grep -v grep|awk '{print $2}'|xargs kill -9
else
  echo 'no process start at :8080'
fi

echo 'start process...'

# 找不到java
source /etc/profile

mainjar=$(find . -type f -name '*.jar')
nohup java -jar $mainjar --spring.profiles.active=test >catalina.out 2>&1 &

echo 'wait 15s...'
# gitlab-runner may exit 1
# sleep 8s

lsof -i:8080

echo 'restart success!'
