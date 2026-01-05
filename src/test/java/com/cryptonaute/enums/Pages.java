package com.cryptonaute.enums;

public enum Pages {

    AUTHOR("archive author"),
    CATEGORY("archive category"),
    LOGIN("login"),
    NOT_FOUND("error404"),
    PAGE("page-template-default"),
    PARENT_PAGE("page-template"),
    POST("post-template-default"),
    WP_ADMIN("wp-admin"),
    UNDEFINED("undefined");

    private final String bodyClassValue;

    Pages(String bodyClassValue) {
        this.bodyClassValue = bodyClassValue;
    }

    public String getBodyClassValue() {
        return bodyClassValue;
    }
}
