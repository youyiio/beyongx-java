#!/bin/sh

deploy_path=/data/application/beyongx-java

if [ ! -d $deploy_path ]; then
  mkdir $deploy_path
fi