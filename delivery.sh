#!/bin/bash

set -eux

commands=$@

for command in $commands; do
  case "$command" in

    "images" )
      sudo docker build -t livetaskboard/web -f Dockerfile_web .
    ;;

    "build:webui" )
    ;;

    * )
      echo "invalid command $command"
      exit 1
    ;;

  esac
done
