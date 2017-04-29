package edu.virginia.cs.solr.service;

import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.parser.DataSetParser;
import edu.virginia.cs.solr.repository.TagRepository;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cutehuazai on 4/28/17.
 */
@Service
@PropertySource("classpath:application.properties")
public class IndexServiceImpl implements IndexService{
    @Resource
    private TagRepository tagRepository;

    @Resource
    private Environment environment;

    private static final String TAG_FILE="dataset.tagfile";

    public void buildIndex(){
        try {
            List<Tag> tags = DataSetParser.parseTag(environment.getRequiredProperty(TAG_FILE));
            tagRepository.save(tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
