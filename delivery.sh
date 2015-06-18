#!/bin/bash

set -eux

commands=$@

for command in $commands; do
  case "$command" in

    "webui_build" )
      : webui_build!
    ;;

    "webui_deploy" )
      : webui_deploy!
    ;;

    "api_build" )
      : api_build!
    ;;

    "api_deploy" )
      : api_deploy!
    ;;

    "acceptance_test" )
      : acceptance_test!
    ;;
    
    * )
      echo "invalid command $command"
      exit 1
    ;;

  esac
done
