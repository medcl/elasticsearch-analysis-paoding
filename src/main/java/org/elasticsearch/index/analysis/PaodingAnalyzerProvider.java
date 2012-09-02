package org.elasticsearch.index.analysis;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.File;

/**
 * Author:medcl
 */
public class PaodingAnalyzerProvider extends AbstractIndexAnalyzerProvider<PaodingAnalyzer> {

    private final PaodingAnalyzer analyzer;

    @Inject
    public PaodingAnalyzerProvider(Index index, @IndexSettings Settings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);

        String config_path= new File(env.configFile(),"paoding/paoding-analyzer.properties").getPath();
        String dict_path= new File(env.configFile(),"paoding/dic").getPath();
        System.out.println(config_path);
        analyzer = new PaodingAnalyzer(config_path,dict_path);
    }

    @Override
    public PaodingAnalyzer get() {
        return this.analyzer;
    }
}
