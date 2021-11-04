#!/bin/sh

deploy_path=$1

if [ ! -d $deploy_path ]; then
  mkdir $deploy_path
fi