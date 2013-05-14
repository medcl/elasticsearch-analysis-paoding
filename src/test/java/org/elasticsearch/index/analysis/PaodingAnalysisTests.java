/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.analysis;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import net.paoding.analysis.analyzer.PaodingTokenizer;
import net.paoding.analysis.analyzer.impl.MostWordsTokenCollector;
import net.paoding.analysis.knife.Paoding;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import static org.elasticsearch.common.settings.ImmutableSettings.Builder.EMPTY_SETTINGS;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 */
public class PaodingAnalysisTests {


    @Test
    public void testPaodingAnalysis() {
        Index index = new Index("test");
        Injector parentInjector = new ModulesBuilder().add(new SettingsModule(EMPTY_SETTINGS), new EnvironmentModule(new Environment(EMPTY_SETTINGS))).createInjector();
        Injector injector = new ModulesBuilder().add(
                new IndexSettingsModule(index, EMPTY_SETTINGS),
                new IndexNameModule(index),
                new AnalysisModule(EMPTY_SETTINGS, parentInjector.getInstance(IndicesAnalysisService.class)).addProcessor(new PaodingAnalysisBinderProcessor()))
                .createChildInjector(parentInjector);
        AnalysisService analysisService = injector.getInstance(AnalysisService.class);

        TokenizerFactory tokenizerFactory = analysisService.tokenizer("paoding");
        MatcherAssert.assertThat(tokenizerFactory, instanceOf(PaodingTokenizerFactory.class));

    }


    @Test
    public void TestTokenizer() throws IOException {

        String[] s = {"Paoding's Knives 中文分词具有极 高效率 和 高扩展性 。引入隐喻，采用完全的面向对象设计，构思先进。",
                "采用基于 不限制个数 的词典文件对文章进行有效切分，使能够将对词汇分类定义。",
                "能够对未知的词汇进行合理解析",
                "他说的确实在理"};
        for (String value : s) {
            System.out.println(value);
            StringReader sr = new StringReader(value);

            PaodingTokenizer tokenizer = new PaodingTokenizer(sr,new Paoding(),new MostWordsTokenCollector());
            boolean hasnext = tokenizer.incrementToken();

            while (hasnext) {

                CharTermAttribute ta = tokenizer.getAttribute(CharTermAttribute.class);

                System.out.println(ta.toString());

                hasnext = tokenizer.incrementToken();

            }
        }

    }


      /*
     *param   分词
     */
    public List getname(String param) throws IOException{

        System.setProperty("paoding.dic.home.config-first", "D:/Projects/Java Related/ElasticSearch/plugins/elasticsearch-analysis-paoding/config/paoding/dic");

        //分词(庖丁解牛分词法)
        Analyzer ika = new PaodingAnalyzer();
        List<String> keys = new ArrayList<String>();
            TokenStream ts = null;

            try{
                Reader r = new StringReader(param);
                ts = ika.tokenStream("TestField", r);
                CharTermAttribute termAtt = (CharTermAttribute) ts.getAttribute(CharTermAttribute.class);
                TypeAttribute typeAtt = (TypeAttribute) ts.getAttribute(TypeAttribute.class);
                String key = null;
                while (ts.incrementToken()) {
                    if ("word".equals(typeAtt.type())) {
                        key = termAtt.toString();
                        if (key.length() >= 2) {
                            keys.add(key);
                        }
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                if (ts != null) {
                    ts.close();
                }
            }

            Map<String, Integer> keyMap = new HashMap<String, Integer>();
            Integer $ = null;
            //计算每个词出现的次数
            for (String key : keys) {
                keyMap.put(key, ($ = keyMap.get(key)) == null ? 1 : $ + 1);
            }
            List<Map.Entry<String, Integer>> keyList = new ArrayList<Map.Entry<String, Integer>>(keyMap.entrySet());
            //进行排序
            Collections.sort(keyList, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return (o2.getValue() - o1.getValue());
                }
            });
            //取出关键词
            String id = null;
            String str = "";
            List list = new ArrayList();
            if(keyList.size() >0){
                for (int i = 0;i < keyList.size(); i++) {
                     id = keyList.get(i).toString();
                     String[] strs = id.split("\\=");
                     str = strs[0];
                     list.add(strs[0]);
                     System.out.println("id:"+id);
                }
            }
            return list;
    }

    @Test
    public void Test1() throws IOException {
//        System.out.println(getname("test北京埃保常"));
    }

}

