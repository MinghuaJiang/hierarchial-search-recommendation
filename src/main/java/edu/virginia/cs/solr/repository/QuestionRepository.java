package edu.virginia.cs.solr.repository;

import edu.virginia.cs.solr.model.Question;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * Created by cutehuazai on 5/1/17.
 */
public interface QuestionRepository extends QuestionSearch, SolrCrudRepository<Question, String> {
}
