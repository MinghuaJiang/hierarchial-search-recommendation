package edu.virginia.cs.core.algorithm;

import edu.virginia.cs.solr.model.Tag;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by VINCENTWEN on 4/30/17.
 */
public class DocIterator implements Iterator<ScoredTag>{
    private int cursor;

    public DocIterator() {
        this.cursor = 0;
    }

    public boolean hasNext() {
        return true;
    }

    public ScoredTag next() {
        if(this.hasNext()) {
            ScoredTag current = new ScoredTag(new Tag());
            cursor ++;
            return current;
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
