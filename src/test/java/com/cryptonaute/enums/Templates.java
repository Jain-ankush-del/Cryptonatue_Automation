package com.cryptonaute.enums;

public enum Templates {

    ABOUT_US("page-template-page-advertiser"),
    AUTHORS_LISTING("page-template-page-authors"),
    CATEGORY("page-template-page-category-template"),
    HOMEPAGE("page-template-home"),
    NEWS_TEMPLATE("page-template-page-news"),
    UNDEFINED("undefined");

    private final String bodyClassValue;

    Templates(String bodyClassValue) {
        this.bodyClassValue = bodyClassValue;
    }

    public String getBodyClassValue() {
        return bodyClassValue;
    }
}
