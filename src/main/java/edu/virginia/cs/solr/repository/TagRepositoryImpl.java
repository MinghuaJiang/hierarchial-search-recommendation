package edu.virginia.cs.solr.repository;

import edu.virginia.cs.search.api.TagSearch;
import edu.virginia.cs.solr.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        List<Tag> result = new ArrayList<Tag>();
        Sort sort = new Sort(Sort.Direction.ASC, "tagName_s");
        Criteria criteria = Criteria.where("tagName_s").isNotNull();
        Pageable pageRequest = new PageRequest(0, 100, sort);
        SimpleQuery query = new SimpleQuery(criteria, pageRequest);
        Page results = template.queryForPage(query, Tag.class);
        result.addAll(results.getContent());
        int pages = results.getTotalPages();
        for(int i = 1;i <= pages;i++){
            pageRequest = pageRequest.next();
            query = new SimpleQuery(criteria, pageRequest);
            results = template.queryForPage(query, Tag.class);
            result.addAll(results.getContent());
        }
        return result;
    }
}
