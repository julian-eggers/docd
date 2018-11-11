package com.itelg.docker.docd.domain;

import static com.itelg.docker.docd.domain.ServiceSelectionMode.BLACKLIST_BLOCKED_LABEL;
import static com.itelg.docker.docd.domain.ServiceSelectionMode.WHITELIST_ALLOWED_LABEL;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Service
{
    public static final String DEPLOYMENT_MODE_LABEL = "DAD_DEPLOYMENT_MODE";

    public static final String DEPLOYMENT_COUNT_LABEL = "DAD_DEPLOYMENT_COUNT";

    public static final String LAST_DEPLOYMENT_LABEL = "DAD_LAST_DEPLOYMENT";

    private String id;

    private String name;

    private String image;

    private Map<String, String> labels = new HashMap<>();

    public void addLabel(String key, String value)
    {
        labels.put(key, value);
    }

    public void removeLabel(String key)
    {
        labels.remove(key);
    }

    public boolean hasLabel(String key)
    {
        return labels.containsKey(key);
    }

    public String getLabelByKey(String key)
    {
        return labels.get(key);
    }

    public Service whitelisted()
    {
        addLabel(WHITELIST_ALLOWED_LABEL, "true");
        return this;
    }

    public Service blacklisted()
    {
        addLabel(BLACKLIST_BLOCKED_LABEL, "true");
        return this;
    }
}