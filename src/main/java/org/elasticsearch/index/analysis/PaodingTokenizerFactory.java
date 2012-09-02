package org.elasticsearch.index.analysis;

import net.paoding.analysis.analyzer.PaodingTokenizer;
import net.paoding.analysis.analyzer.TokenCollector;
import net.paoding.analysis.analyzer.impl.MaxWordLengthTokenCollector;
import net.paoding.analysis.analyzer.impl.MostWordsTokenCollector;
import net.paoding.analysis.knife.Paoding;
import net.paoding.analysis.knife.PaodingMaker;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.File;
import java.io.Reader;

/**
 * Author:medcl
 */
public class PaodingTokenizerFactory extends AbstractTokenizerFactory {

    private String seg_type;
    private TokenCollector collector;
    private Paoding paoding;

    @Inject
    public PaodingTokenizerFactory(Index index, @IndexSettings Settings indexSettings,Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        seg_type = settings.get("collector", "most_word");
        if(seg_type.equals("most_word")){
            collector = new MostWordsTokenCollector();
        }else if(seg_type.equals("max_word_len")){
            collector = new MaxWordLengthTokenCollector();
        }else {
            //default is max_word_len
            collector = new MaxWordLengthTokenCollector();
        }
        String config_path= new File(env.configFile(),"paoding/paoding-analyzer.properties").getPath();
        String dict_path= new File(env.configFile(),"paoding/dic").getPath();
        paoding =PaodingMaker.make(config_path,dict_path);
    }

    @Override
    public Tokenizer create(Reader reader) {
//      logger.info(paoding+","+collector);
      return new PaodingTokenizer(reader,paoding,collector);
    }
}

