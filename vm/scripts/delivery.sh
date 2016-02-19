#!/bin/bash

set -eux

commands=$@
project_dir=$(dirname $(readlink -f $0))/../..
orchestrate_dir=$project_dir/vm/orchestrate
ansible_dir=$orchestrate_dir/ansible
webui_dir=$project_dir/app/webui
api_dir=$project_dir/app/api

for command in $commands; do
  case "$command" in

    "build:images" )
      cd $orchestrate_dir
      sudo docker build -t livetaskboard/web -f Dockerfile_web .
      sudo docker build -t livetaskboard/tm-db -f Dockerfile_tm-db .
      sudo docker build -t livetaskboard/aa-db -f Dockerfile_aa-db .
      sudo docker build -t livetaskboard/ws-db -f Dockerfile_ws-db .
      sudo docker build -t livetaskboard/mq -f Dockerfile_mq .
      sudo docker build -t livetaskboard/tm-api -f Dockerfile_tm-api .
    ;;

    "build:webui" )
      cd $webui_dir

      set +eu
      . ~/.nvm/nvm.sh
      nvm use 5
      set -eu
      gulp export

      dest_dir=$ansible_dir/roles/webserver_deploy/files/build
      rm -fR $dest_dir/*
      cp -r export/* $dest_dir
    ;;

    "build:api" )
      cd $api_dir

      ./gradlew build -x test

      cp auth-access/build/libs/auth-access-*.jar $ansible_dir/roles/auth-access_api_deploy/files/build/app.jar
      cp task-manage/build/libs/task-manage-*.jar $ansible_dir/roles/task-manage_api_deploy/files/build/app.jar
      cp widget-store/build/libs/widget-store-*.jar $ansible_dir/roles/widget-store_api_deploy/files/build/app.jar
    ;;

    "clean" )
      cd $orchestrate_dir/ansible
      set +e
      sudo pkill consul
      sudo docker rm -f $(sudo docker ps -aq)
      sudo docker rmi $(sudo docker images -f "dangling=true" -q)
      set -e
    ;;

    "run:dev" )
      cd $orchestrate_dir/ansible
      set +e
      sudo pkill consul
      set -e
      consul agent -server -bootstrap-expect 1 -data-dir /tmp/consul -bind=172.17.0.1 &
      ansible-playbook -i inventories/develop -c local --tags start_dev devmachine.yml
    ;;

    "run:all" )
      cd $orchestrate_dir/ansible
      set +e
      sudo pkill consul
      set -e
      consul agent -server -bootstrap-expect 1 -data-dir /tmp/consul -bind=172.17.0.1 &
      ansible-playbook -i inventories/develop -c local --tags start devmachine.yml
    ;;

    * )
      echo "invalid command $command"
      exit 1
    ;;

  esac
done
