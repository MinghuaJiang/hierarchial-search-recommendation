package edu.virginia.cs.core.config;

import edu.virginia.cs.core.algorithm.HierarchyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.io.File;

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
        return builder;
    }
}
