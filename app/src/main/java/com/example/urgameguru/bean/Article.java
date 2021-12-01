package com.example.urgameguru.bean;

public class Article {
    private String articleName;
    private String articleUri;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


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

    private Article() {}

    public Article(String name, String uri, String user_name) {
        if (name.trim().equals("")) {
            name = "No Title";
        }

        articleName = name;
        articleUri = uri;
        userName = user_name;
    }
}
