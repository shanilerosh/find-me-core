package com.findmecore.findmecore.dto;

/**
 * @author ShanilErosh
 */
public enum PostVisibility {

    PUBLIC("PUBLIC"),ME_ONLY("ME_ONLY"),FRIENDS_ONLY("Friend Only");

    private String postVisibility;

    public String getPostVisibility() {
        return postVisibility;
    }

    PostVisibility(final String postVisibility) {
        this.postVisibility = postVisibility;
    }
}
