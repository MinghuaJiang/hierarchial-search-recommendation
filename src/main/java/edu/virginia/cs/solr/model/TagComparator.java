package edu.virginia.cs.solr.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by cutehuazai on 5/5/17.
 */
public class TagComparator implements Comparator<Tag>, Serializable{
    @Override
    public int compare(Tag t1, Tag t2) {
        if(t2.getScore() - t1.getScore() <= 0){
            return 1;
        }else return -1;
    }
}
