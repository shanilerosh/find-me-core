package com.findmecore.findmecore.dto;

/**
 * @author ShanilErosh
 */
public enum ReactionType {

    LIKE("LIKE"),HEART("HEART");

    private String reactionType;

    public String getReactionType() {
        return reactionType;
    }

    ReactionType(final String postVisibility) {
        this.reactionType = postVisibility;
    }
}
