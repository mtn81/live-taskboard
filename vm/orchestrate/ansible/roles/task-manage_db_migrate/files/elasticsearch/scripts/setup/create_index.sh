#!/bin/bash

es_host=$1

curl -XPUT "http://$es_host:9200/task-manage" -d '
{
  "mappings": {
    "member": {
      "properties": {
        "member_id": { "type": "string", "index": "not_analyzed" },
        "type": { "type": "string", "index": "not_analyzed" }
      }
    },
    "group": {
      "properties": {
        "group_id": { "type": "string", "index": "not_analyzed" },
        "owner_member_id": { "type": "string", "index": "not_analyzed" }
      }
    },
    "v_group_belonging": {
      "_parent": { "type": "group" },
      "properties": {
        "member_id": { "type": "string", "index": "not_analyzed" },
        "group_id": { "type": "string", "index": "not_analyzed" }
      }
    },
    "v_group_owner": {
      "properties": {
        "member_id": { "type": "string", "index": "not_analyzed" },
        "member_type": { "type": "string", "index": "not_analyzed" }
      }
    },
    "group_join": {
      "_parent": { "type": "v_group_owner" },
      "properties": {
        "application_id": { "type": "string", "index": "not_analyzed" },
        "group_id": { "type": "string", "index": "not_analyzed" },
        "applicant_id": { "type": "string", "index": "not_analyzed" },
        "status": { "type": "string", "index": "not_analyzed" }
      }
    }
  }
}'

