#!/bin/bash

set -eux

es_host=$1

curl -XPUT "http://$es_host:9200/task-manage" -d '
{
  "mappings": {
    "member": {
      "properties": {
        "member_id": { "type": "string", "index": "not_analyzed" },
        "type": { "type": "string", "index": "not_analyzed" },
        "name": { "type": "string" },
        "email": { "type": "string", "index": "not_analyzed" },
        "belongings": {
          "type": "nested",
          "properties": {
            "group_id": { "type": "string", "index": "not_analyzed" },
            "admin": { "type": "boolean" }
          }
        }
      }
    },
    "group": {
      "properties": {
        "group_id": { "type": "string", "index": "not_analyzed" },
        "owner_member_id": { "type": "string", "index": "not_analyzed" },
        "name": { "type": "string" },
        "description": { "type": "string" }
      }
    },
    "group_join": {
      "properties": {
        "application_id": { "type": "string", "index": "not_analyzed" },
        "group_id": { "type": "string", "index": "not_analyzed" },
        "applicant_id": { "type": "string", "index": "not_analyzed" },
        "status": { "type": "string", "index": "not_analyzed" },
        "applied_time": { "type": "date" }
      }
    },

    "view_group_belonging": {
      "properties": {
        "group_id": { "type": "string", "index": "not_analyzed" },
        "group_name": { "type": "string" },
        "member_id": { "type": "string", "index": "not_analyzed" },
        "admin": { "type": "boolean" }
      }
    },
    "view_group_search": {
      "properties": {
        "group_id": { "type": "string", "index": "not_analyzed" },
        "group_name": { "type": "string" },
        "group_description": { "type": "string" },
        "owner_id": { "type": "string" , "index": "not_analyzed" },
        "owner_type": { "type": "string" , "index": "not_analyzed" },
        "owner_name": { "type": "string" },
        "applicants": { "type": "string" , "index": "not_analyzed" },
        "members": { "type": "string" , "index": "not_analyzed" }
      }
    },
    "view_group_join_by_applicant": {
      "properties": {
        "application_id": { "type": "string", "index": "not_analyzed" },
        "applied_time": { "type": "date" },
        "status": { "type": "string", "index": "not_analyzed" },
        "applicant_id": { "type": "string", "index": "not_analyzed" },
        "group_id": { "type": "string", "index": "not_analyzed" },
        "group_name": { "type": "string" },
        "owner_id": { "type": "string" , "index": "not_analyzed" },
        "owner_type": { "type": "string" , "index": "not_analyzed" },
        "owner_name": { "type": "string" }
      }
    },
    "view_group_join_to_admin": {
      "properties": {
        "application_id": { "type": "string", "index": "not_analyzed" },
        "applied_time": { "type": "date" },
        "status": { "type": "string", "index": "not_analyzed" },
        "group_id": { "type": "string", "index": "not_analyzed" },
        "group_name": { "type": "string" },
        "applicant_id": { "type": "string", "index": "not_analyzed" },
        "applicant_name": { "type": "string" },
        "applicant_type": { "type": "string" , "index": "not_analyzed" },
        "admin_members": { "type": "string" , "index": "not_analyzed" }
      }
    }
  }
}'

