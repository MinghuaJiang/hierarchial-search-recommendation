package edu.virginia.cs.search.api;

import edu.virginia.cs.solr.model.Tag;

/**
 * Created by cutehuazai on 4/29/17.
 */
public interface TagSearch {
    public Tag findById(String id);
    public Tag findByName(String tagName);
}
