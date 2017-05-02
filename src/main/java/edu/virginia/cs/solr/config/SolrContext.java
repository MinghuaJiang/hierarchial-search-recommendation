package edu.virginia.cs.solr.config;

/**
 * Created by cutehuazai on 5/1/17.
 */


import edu.virginia.cs.solr.index.IndexService;
import edu.virginia.cs.solr.index.IndexServiceImpl;
import edu.virginia.cs.solr.parser.StreamBasedXMLParser;
import edu.virginia.cs.solr.repository.QuestionRepository;
import edu.virginia.cs.solr.repository.QuestionRepositoryImpl;
import edu.virginia.cs.solr.repository.TagRepository;
import edu.virginia.cs.solr.repository.TagRepositoryImpl;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
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
        return new HttpSolrClient(solrHost);
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

    @Bean
    public QuestionRepository questionRepository() throws Exception {
        return new SolrRepositoryFactory(solrTemplate())
                .getRepository(QuestionRepository.class, new QuestionRepositoryImpl(solrTemplate()));
    }

    @Bean
    public IndexService indexService() throws Exception {
        return new IndexServiceImpl();
    }

    @Bean
    public StreamBasedXMLParser getParser(){
        return new StreamBasedXMLParser();
    }
}
