package edu.virginia.cs.solr.repository;

import edu.virginia.cs.solr.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cutehuazai on 4/28/17.
 */
@Repository
public class TagRepositoryImpl implements TagRepositoryCustom{
    private SolrTemplate template;
    public TagRepositoryImpl(SolrTemplate template){
        this.template = template;
    }
    @Override
    public Tag findByName(String name) {
        return template.queryForObject(new SimpleQuery("tagName:" + name), Tag.class);
    }
}
