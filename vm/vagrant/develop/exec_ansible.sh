#!/bin/bash

ansible_exec_home=$1

sudo apt-get update && sudo apt-get -y install python-dev python-setuptools
sudo easy_install pip
sudo pip install ansible

ansible-playbook $ansible_exec_home/devmachine.yml -c local --tags setup
