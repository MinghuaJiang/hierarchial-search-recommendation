package edu.virginia.cs.solr.index;

import edu.virginia.cs.solr.model.Question;

import java.util.List;

/**
 * Created by cutehuazai on 5/1/17.
 */
public interface IndexService {
    public void buildTagIndex();
    public void buildQuestionIndex(List<Question> questions);
}
