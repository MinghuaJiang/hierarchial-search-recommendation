package edu.virginia.cs.search.impl;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.Tag;
import edu.virginia.cs.search.api.TagSearch;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cutehuazai on 4/29/17.
 */
public class TagSearchImpl implements TagSearch{
    @Autowired
    private StackExchangeApiQueryFactory factory;

    public TagSearchImpl(StackExchangeApiQueryFactory factory) {
        this.factory = factory;
    }

    public List<Tag> getAllTags(){
        List<Tag> result = new ArrayList<Tag>();
        Paging paging = new Paging(1, 100);
        PagedList<Tag> tags = factory.newTagApiQuery().withSort(Tag.SortOrder.NAME_ASCENDING).withPaging(paging).list();
        result.addAll(tags);
        while (tags.hasMore()){
            paging = new Paging(tags.getPage() + 1, 100);
            tags = factory.newTagApiQuery().withSort(Tag.SortOrder.NAME_ASCENDING).withPaging(paging).list();
            result.addAll(tags);
        }
        return result;
    }
}
