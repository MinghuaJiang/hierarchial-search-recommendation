package edu.virginia.cs.solr.model;

import java.util.List;

/**
 * Created by cutehuazai on 5/1/17.
 */
public class QuestionResult {
    private long totalCount;
    private int totalPage;
    private List<Question> questions;
    public QuestionResult(long totalCount, int totalPage, List<Question> questions){
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.questions = questions;
    }
}
