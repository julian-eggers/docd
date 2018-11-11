package com.itelg.docker.docd.domain;

public enum ServiceSelectionMode
{
    BLACKLIST,
    WHITELIST;

    public static final String WHITELIST_ALLOWED_LABEL = "DAD_SELECTION_ALLOWED";

    public static final String BLACKLIST_BLOCKED_LABEL = "DAD_SELECTION_BLOCKED";
}