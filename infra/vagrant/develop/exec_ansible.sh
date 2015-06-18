#!/bin/bash

ansible_exec_home=$1

sudo apt-get update && sudo apt-get -y install python-dev python-setuptools
sudo easy_install pip
sudo pip install ansible

ansible-playbook -i $ansible_exec_home/inventories/develop $ansible_exec_home/site.yml
