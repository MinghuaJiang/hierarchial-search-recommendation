package edu.virginia.cs.solr.repository;

import edu.virginia.cs.solr.model.Tag;
import edu.virginia.cs.solr.model.Topic;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * Created by cutehuazai on 4/29/17.
 */
public interface TagSearch {
    public Tag getTagByName(String tagName);
    public List<Tag> getAllTags();
    public TreeSet<Tag> getRankingTags(Set<Topic> topK) throws IOException, SolrServerException;
}
