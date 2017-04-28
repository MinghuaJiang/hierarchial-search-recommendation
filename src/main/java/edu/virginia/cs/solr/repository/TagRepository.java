package edu.virginia.cs.solr.repository;

import edu.virginia.cs.solr.model.Tag;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

/**
 * Created by cutehuazai on 4/28/17.
 */
public interface TagRepository extends TagRepositoryCustom, SolrCrudRepository<Tag, String> {
    //@Query(name = "Tag.findByNamedQuery")
    //public List<Tag> findByNamedQuery(String searchTerm);
}
