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

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
