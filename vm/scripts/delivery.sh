#!/bin/bash

set -eux

commands=$@
project_dir=$(dirname $(readlink -f $0))/../..
orchestrate_dir=$project_dir/vm/orchestrate
ansible_dir=$orchestrate_dir/ansible
webui_dir=$project_dir/app/webui
api_dir=$project_dir/app/api

build_images () {
  cd $orchestrate_dir
  for n in $@; do
    sudo docker build -t livetaskboard/$n -f Dockerfile_$n .
  done
}

for command in $commands; do
  case "$command" in

    build:images=* )
      docker_images=($(echo $command | sed -e 's/build\:images\=//' | sed -e 's/\,/ /'))
      build_images ${docker_images[@]}
    ;;

    "build:images" )
      docker_images=( \
        "base" \
        "tm-db" "tm-api" "tm-backend" \
        "aa-db" "aa-api" "aa-backend" \
        "ws-db" "ws-api" "ws-backend" \
        "mq" "web" \
      )
      build_images ${docker_images[@]}
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

    "build:servers" )
      cd $api_dir

      # ./gradlew build -x test

      cp auth-access_api/build/libs/auth-access_api-*.jar $ansible_dir/roles/auth-access_api_deploy/files/build/app.jar
      cp auth-access_backend/build/libs/auth-access_backend-*.jar $ansible_dir/roles/auth-access_backend_deploy/files/build/app.jar
      cp task-manage_api/build/libs/task-manage_api-*.jar $ansible_dir/roles/task-manage_api_deploy/files/build/app.jar
      cp task-manage_backend/build/libs/task-manage_backend-*.jar $ansible_dir/roles/task-manage_backend_deploy/files/build/app.jar
      cp widget-store_api/build/libs/widget-store_api-*.jar $ansible_dir/roles/widget-store_api_deploy/files/build/app.jar
      cp widget-store_backend/build/libs/widget-store_backend-*.jar $ansible_dir/roles/widget-store_backend_deploy/files/build/app.jar
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
