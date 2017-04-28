package edu.virginia.cs.solr.config;

import edu.virginia.cs.solr.repository.TagRepository;
import edu.virginia.cs.solr.repository.TagRepositoryImpl;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.io.SolrClientCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.SolrRepository;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.apache.solr.client.solrj.*;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;

import javax.annotation.Resource;

/**
 * Created by cutehuazai on 4/27/17.
 */

@Configuration
@EnableSolrRepositories(basePackages={"edu.virginia.cs.solr.repository"}, multicoreSupport=true)
@PropertySource("classpath:application.properties")
public class SolrContext {
    @Resource
    private Environment environment;

    @Bean
    public SolrClient solrClient() {
        String solrHost = environment.getRequiredProperty("solr.server.url");
        return new HttpSolrClient.Builder(solrHost).build();
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrClient());
    }

    @Bean
    public TagRepository tagRepository() throws Exception {
        return new SolrRepositoryFactory(solrTemplate())
                .getRepository(TagRepository.class, new TagRepositoryImpl(solrTemplate()));
    }
}
