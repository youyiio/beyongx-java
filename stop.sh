#!/bin/sh

port=8080

lsof -i:$port

#kill process at port 8080
pid=$(lsof -i:$port|grep 'java'|grep -v 'grep'|awk '{print $2}')
if [ "$pid" != "" ]; then
  lsof -i:$port|grep java|grep -v grep|awk '{print $2}'|xargs kill -9
else
  echo 'no process start at :${port}'
fi

echo 'stop process...'

lsof -i:$port

echo 'stop success!'
