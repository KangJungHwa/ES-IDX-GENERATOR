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