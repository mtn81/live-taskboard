#!/bin/bash
set -eux

join_addr=
case "$1" in
  --join-ip=* )
    join_addr=$(echo $1 | sed -e s/--join-ip=//)
  ;;
  --join-host=* )
    join_host=$(echo $1 | sed -e s/--join-host=//)
    join_addr=$(host $join_host | awk '{print $4}')
  ;;
  * )
    echo "invalid param $1"
    exit 1
  ;;
esac

this_ip=$(ip addr show eth0 | grep 'inet ' | awk '{print $2}' | sed -e 's/\/.*//')

echo "(this_ip, join_addr) = ($this_ip, $join_addr)"

/usr/local/bin/consul agent \
  -config-dir /etc/consul.d \
  -bind=$this_ip \
  -join=$join_addr
