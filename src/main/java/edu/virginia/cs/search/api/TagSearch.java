package edu.virginia.cs.search.api;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Tag;

import java.util.List;

/**
 * Created by cutehuazai on 4/29/17.
 */
public interface TagSearch {
    public List<Tag> getAllTags();
}
