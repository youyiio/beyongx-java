#!/bin/sh

tar -xzf bundle.tar.gz

deploy_path=$1

if [ ! -d $deploy_path ]; then
  mkdir $deploy_path
fi

# 是否首次部署
deploy_first=false
if [ ! -f "$deploy_path/start.sh" ]; then
  deploy_first=true
fi

if [ "$deploy_first" = true ]; then
  /bin/cp -fr start.sh $deploy_path
  /bin/cp -fr stop.sh $deploy_path
  chmod +x start.sh
  chmod +x stop.sh
fi

/bin/cp -fr *.jar $deploy_path

# 启动应用
./start.sh