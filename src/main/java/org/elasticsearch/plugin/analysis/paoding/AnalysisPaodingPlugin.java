package org.elasticsearch.plugin.analysis.paoding;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.PaodingAnalysisBinderProcessor;
import org.elasticsearch.plugins.AbstractPlugin;

/**
 * Author:medcl
 */
public class AnalysisPaodingPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "analysis-paoding";
    }

    @Override
    public String description() {
        return "Lucene中文分词“庖丁解牛” Paoding Analysis";
    }

    public void onModule(AnalysisModule module) {
        if (module instanceof AnalysisModule) {
            AnalysisModule analysisModule = (AnalysisModule) module;
            module.addProcessor(new PaodingAnalysisBinderProcessor());
        }

    }
}
