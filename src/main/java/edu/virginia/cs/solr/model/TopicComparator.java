package edu.virginia.cs.solr.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by cutehuazai on 5/4/17.
 */
public class TopicComparator implements Comparator<Topic>, Serializable{
    @Override
    public int compare(Topic t1, Topic t2) {
        return (int) (t1.getDocumentFrequency() - t2.getDocumentFrequency());
    }
}
