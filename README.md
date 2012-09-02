Paoding Analysis for ElasticSearch
==================================

The Paoding Analysis plugin integrates Paoding(http://code.google.com/p/paoding/) module into elasticsearch.

In order to install the plugin, simply run: `bin/plugin -install medcl/elasticsearch-analysis-paoding/1.0.0`.

    --------------------------------------------------
    | Paoding    Analysis Plugin    | ElasticSearch  |
    --------------------------------------------------
    | master                        | 0.19.8 -> master|
    --------------------------------------------------
    | 1.0.0                         | 0.19.8 -> master|
    --------------------------------------------------

The plugin includes `paoding` analyzer and `paoding` tokenizer.

optional config `collector` can be set to `most_word` or `max_word_len`

1.install the paoding analysis plugin and drop the config files(https://github.com/downloads/medcl/elasticsearch-analysis-paoding/config.zip) into `your_es_root/config/paoding/`

2.create a index with paoding analysis config

```
curl -XPUT http://localhost:9200/medcl/ -d'
{
            "index.analysis.tokenizer.paoding2.collector": "max_word_len",
            "index.analysis.tokenizer.paoding2.type": "paoding",

            "index.analysis.analyzer.paoding_analyzer2.filter.0": "standard",
            "index.analysis.analyzer.paoding_analyzer2.tokenizer": "paoding2",

            "index.number_of_shards": "5",
            "index.number_of_replicas": "1",


            "index.analysis.tokenizer.paoding1.type": "paoding",
            "index.analysis.tokenizer.paoding1.collector": "most_word",

            "index.analysis.analyzer.paoding_analyzer1.filter.0": "standard",
            "index.analysis.analyzer.paoding_analyzer1.tokenizer": "paoding1"
        }'

```

3.analyzing tests

```
http://localhost:9200/index7/_analyze?text=%e4%b8%ad%e5%8d%8e%e4%ba%ba%e6%b0%91%e5%85%b1%e5%92%8c%e5%9b%bd&analyzer=paoding_analyzer1
{"tokens":[{"token":"中华","start_offset":0,"end_offset":2,"type":"paoding","position":1},{"token":"华人","start_offset":1,"end_offset":3,"type":"paoding","position":2},{"token":"人民","start_offset":2,"end_offset":4,"type":"paoding","position":3},{"token":"中华人民","start_offset":0,"end_offset":4,"type":"paoding","position":4},{"token":"共和","start_offset":4,"end_offset":6,"type":"paoding","position":5},{"token":"共和国","start_offset":4,"end_offset":7,"type":"paoding","position":6},{"token":"人民共和国","start_offset":2,"end_offset":7,"type":"paoding","position":7},{"token":"中华人民共和国","start_offset":0,"end_offset":7,"type":"paoding","position":8}]}
http://localhost:9200/index7/_analyze?text=%e4%b8%ad%e5%8d%8e%e4%ba%ba%e6%b0%91%e5%85%b1%e5%92%8c%e5%9b%bd&analyzer=paoding_analyzer2
{"tokens":[{"token":"中华人民共和国","start_offset":0,"end_offset":7,"type":"paoding","position":1}]}
```

4.have fun.
