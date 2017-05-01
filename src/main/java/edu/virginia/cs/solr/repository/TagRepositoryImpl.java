package edu.virginia.cs.solr.repository;

import edu.virginia.cs.search.api.TagSearch;
import edu.virginia.cs.solr.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cutehuazai on 5/1/17.
 */
@Repository
public class TagRepositoryImpl implements TagSearch{
    private SolrTemplate template;
    public TagRepositoryImpl(SolrTemplate template){
        this.template = template;
    }

    @Override
    public List<Tag> getAllTags() {
        Criteria conditions = Criteria.where("tagName_t").isNotNull();
        SimpleQuery search = new SimpleQuery(conditions);

        Page results = null;
        return results.getContent();
    }
}
