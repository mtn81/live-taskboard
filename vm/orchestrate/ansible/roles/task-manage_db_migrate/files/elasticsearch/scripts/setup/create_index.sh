#!/bin/bash

set -eux

es_host=$1

set +e
curl -XDELETE "http://$es_host:9200/sequence"
curl -XDELETE "http://$es_host:9200/task-manage"
set +e

curl -XPUT "http://$es_host:9200/sequence" -d '
{
  "settings": {
    "number_of_shards": 1,
    "auto_expand_replicas": "0-all"
  },
  "mappings": {
    "task_id": {
      "_all": {"enabled": false},
      "dynamic": "strict",
      "properties": {
        "value": { "type": "integer", "index": "no" }
      }
    }
  }
}
'

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
    "task": {
      "properties": {
   			"task_id": { "type": "string", "index": "not_analyzed" },
				"group_id": { "type": "string", "index": "not_analyzed" },
				"status": { "type": "string", "index": "not_analyzed" },
				"name": { "type": "string" },
				"assigned": { "type": "string", "index": "not_analyzed" },
				"memo": { "type": "string" },
		    "deadline": { "type": "date" }
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

