package edu.virginia.cs.core.config;

import edu.virginia.cs.core.algorithm.HierarchyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by cutehuazai on 5/3/17.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class CoreContext {
    @Resource
    private Environment environment;
    @Bean
    public HierarchyBuilder getHierarchyBuilder(){
        String outputFile = environment.getRequiredProperty("hierarchy.outputfile");
        HierarchyBuilder builder = new HierarchyBuilder(outputFile);
        File file = new File(outputFile);
        try {
            builder.buildHierachy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }
}
