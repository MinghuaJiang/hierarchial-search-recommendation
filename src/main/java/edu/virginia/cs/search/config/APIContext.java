package edu.virginia.cs.search.config;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.schema.StackExchangeSite;
import edu.virginia.cs.search.api.QuestionSearch;
import edu.virginia.cs.search.impl.QuestionSearchImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Created by cutehuazai on 4/29/17.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class APIContext {
    @Resource
    private Environment environment;

    private static final String API_KEY = "AdhEvgh0PxNpFvkX0JdAHg((";
    @Bean
    public StackExchangeApiQueryFactory getQueryFactory(){
        StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory
                .newInstance(environment.getRequiredProperty("stackoverflow.api.key"),
                        StackExchangeSite.STACK_OVERFLOW);
        return queryFactory;
    }

    @Bean
    public QuestionSearch getQuestionSearch(){
        return new QuestionSearchImpl(getQueryFactory());
    }

}
