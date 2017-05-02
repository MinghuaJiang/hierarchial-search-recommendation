package edu.virginia.cs.solr.index;

import edu.virginia.cs.solr.model.Question;
import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.parser.JaxbXMLParser;
import edu.virginia.cs.solr.repository.QuestionRepository;
import edu.virginia.cs.solr.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * Created by cutehuazai on 5/1/17.
 */
public class IndexServiceImpl implements IndexService{
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private Environment environment;

    @Override
    public void buildTagIndex() {
        try {
            List<Tag> tags = JaxbXMLParser.parseTag(environment.getRequiredProperty("dataset.tagfile"));
            tagRepository.save(tags);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void buildQuestionIndex(List<Question> questions) {
        questionRepository.save(questions);
    }
}
