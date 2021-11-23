package com.example.urgameguru.bean;

public class Article {
    private String articleName;
    private String articleUri;

    private Article() {}

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleUri() {
        return articleUri;
    }

    public void setArticleUri(String articleUri) {
        this.articleUri = articleUri;
    }

    public Article(String name, String uri) {
        if (name.trim().equals("")) {
            name = "No Title";
        }

        articleName = name;
        articleUri = uri;
    }
}
