인덱스 리스트 조회
GET /_cat/indices

인덱스 리스트 조회 json 포멧
GET  /_cat/indices?format=json&pretty

인덱스 검색(URL 파라메터)
GET /cat_tab_mas/_search?q=table_nm:TR

인덱스 검색(REQUEST BODY)
GET /cat_tab_mas/_search
```json
{
  "query": {
    "match": {
      "table_nm": "TR"
    }
  }
}
```
인덱스 생성 request
PUT /cat_tab_mas
```json
{
  "settings": {
    "index": {
      "analysis": {
        "analyzer": {
          "ngram_analyzer": {
            "type": "custom",
            "tokenizer": "my_ngram"
          }
        },
        "tokenizer": {
          "my_ngram": {
            "type": "ngram",
            "min_gram": 2,
            "max_gram": 500,
            "token_chars": [
              "letter",
              "digit"
            ]
          }
        }
      }
    }
  },
  "mappings": {
      "properties" : {
        "table_id" : {"type" : "integer"},
        "table_nm" : {
          "type":"text",
          "analyzer": "ngram_analyzer"
        },
        "table_desc": {"type":"text"},
        "table_tag" : {"type":"text"}
      }
  }
}

```
인덱스 삭제 request
DELETE /cat_tab_mas

인덱스 조회 request
```json

```
cat_tab_mas DDL
```
CREATE TABLE `cat_tab_mas` (
  `table_id` bigint NOT NULL,
  `table_nm` varchar(255) DEFAULT NULL,
  `table_desc` varchar(255) DEFAULT NULL,
  `table_tag` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
```


cat_tab_mas insert query
```
INSERT INTO glue.cat_tab_mas (table_id,table_nm,table_desc,table_tag) VALUES 
(1,'EQP_RUNTIME_HISTORY','장비의 실시간 가동 이력','#EQP, #RUNTIME, #HIST #HISTORY')
,(2,'LOT_TRACE_SUMMARY','LOT 추적 요약','#LOT, #TRACE, #SUMMARY')
,(3,'EQP_TRACE_SUMMARY','EQP 추적 요약','#EQP, #TRACE, #SUMMARY')
,(4,'EQP_INFO','EQP 정보','#EQP, #INFO')
,(5,'RECIPE_INFO','RECIPE 정보','#RECIPE, #INFO')
,(6,'SUMMARY_INFO','SUMMARY 정보','#SUMMARY, #INFO')
,(7,'SUM_INFO','SUMMARY 정보','#SUM, #SUMMARY, #INFO')
,(8,'REQ_INFO','EQP 정보','#EQP, #REQ, #INFO')
,(9,'REQUEST_INFO','REQUEST 정보',' #REQUEST, #INFO')
,(10,'EQUPMENT_RUNTIME_HISTORY','장비의 실시간 가동 이력',' #EQUPMENT, #INFO')
;
INSERT INTO glue.cat_tab_mas (table_id,table_nm,table_desc,table_tag) VALUES 
(11,'SUMMARY_INFO','SUMMARY 정보',' #SUMMARY, #INFO')
,(12,'SUMMARY_INFORMATION','SUMMARY 정보',' #SUMMARY, #INFORMATION')
;
```