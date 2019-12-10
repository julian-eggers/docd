package com.itelg.docker.docd.parser;

import com.itelg.docker.docd.domain.WebHookEvent;

public interface WebHookEventParser
{
    WebHookEvent parse(String json);
}
