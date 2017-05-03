package edu.virginia.cs.solr.repository;

import edu.virginia.cs.solr.model.Tag;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * Created by cutehuazai on 5/1/17.
 */
public interface TagRepository extends TagSearch, SolrCrudRepository<Tag, String> {
}
