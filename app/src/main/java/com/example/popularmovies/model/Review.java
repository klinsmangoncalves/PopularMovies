package com.example.popularmovies.model;

import java.util.List;

public class Review {
    private Long id;
    private int page;
    private List<ReviewDetail> results;

    public class ReviewDetail{
        String author;
        String content;
        String id;
        String url;

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }
    }

    public List<ReviewDetail> getResults() {
        return results;
    }

    public void setResults(List<ReviewDetail> results) {
        this.results = results;
    }
}
