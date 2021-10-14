#!/bin/sh

#ps -ef|grep rxapi|grep -v grep|cut -c 9-15|xargs kill -9

lsof -i:8080

#kill process at port 8080
pid=$(lsof -i:8080|grep 'java'|grep -v 'grep'|awk '{print $2}')
if [ "$pid" != "" ]; then
  lsof -i:8080|grep java|grep -v grep|awk '{print $2}'|xargs kill -9
else
  echo 'no process start at :8080'
fi

echo 'stop process...'

lsof -i:8080

echo 'stop success!'
