#!/bin/bash

set -eux

es_host=$1

curl -XPUT "http://$es_host:9200/task-manage/member/taro" -d '
{
  "member_id": "taro",
  "type": "PROPER",
  "name": "タスク太郎",
  "email": "",
  "belongings": [
    { "group_id": "test-group", "admin": true }
  ]
}
'
curl -XPUT "http://$es_host:9200/task-manage/member/jiro" -d '
{
  "member_id": "jiro",
  "type": "PROPER",
  "name": "タスク次郎",
  "email": "",
  "belongings": [
    { "group_id": "test-group", "admin": false }
  ]
}
'
curl -XPUT "http://$es_host:9200/task-manage/group/test-group" -d '
{
  "group_id": "test-group",
  "owner_member_id": "taro",
  "name": "テストグループ",
  "description": "テスト用のグループ"
}
'
curl -XPUT "http://$es_host:9200/task-manage/view_group_belonging/taro@test-group" -d '
{
  "group_id": "test-group",
  "group_name": "テストグループ",
  "member_id": "taro",
  "admin": true
}
'
curl -XPUT "http://$es_host:9200/task-manage/view_group_belonging/jiro@test-group" -d '
{
  "group_id": "test-group",
  "group_name": "テストグループ",
  "member_id": "jiro",
  "admin": false
}
'
curl -XPUT "http://$es_host:9200/task-manage/view_group_search/test-group" -d '
{
  "group_id": "test-group",
  "group_name": "テストグループ",
  "group_description": "テスト用のグループ",
  "owner_id": "taro",
  "owner_type": "PROPER",
  "owner_name": "テスト太郎",
  "applicants": [],
  "members": [ "taro", "jiro" ]
}
'
