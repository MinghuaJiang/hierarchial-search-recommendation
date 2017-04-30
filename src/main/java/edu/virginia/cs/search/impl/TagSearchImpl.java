package edu.virginia.cs.search.impl;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;
import edu.virginia.cs.search.api.TagSearch;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cutehuazai on 4/29/17.
 */
public class TagSearchImpl implements TagSearch{
    @Autowired
    private StackExchangeApiQueryFactory factory;

    public TagSearchImpl(StackExchangeApiQueryFactory factory) {
        this.factory = factory;
    }

    public String findByName(String tagName){
        PagedList<Tag> tags = factory.newTagApiQuery().withName(tagName).list();
        for (Tag tag : tags) {
            System.out.println(tag.getName());
        }
        return "";
    }
}
