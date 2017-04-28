package edu.virginia.cs.solr.repository;

import edu.virginia.cs.solr.model.Tag;

/**
 * Created by cutehuazai on 4/28/17.
 */
public interface TagRepositoryCustom {
    public Tag findByName(String name);
}
