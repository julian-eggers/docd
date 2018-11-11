package com.itelg.docker.docd.parser;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.itelg.docker.docd.domain.WebHookEvent;

import lombok.SneakyThrows;

@Component
public class DockerhubWebhookEventParser
{
    @SneakyThrows
    public WebHookEvent parse(String json)
    {
        JSONObject object = new JSONObject(json);

        WebHookEvent event = new WebHookEvent();
        event.setNamespace(object.getJSONObject("repository").getString("namespace"));
        event.setRepositoryName(object.getJSONObject("repository").getString("name"));
        event.setTag(object.getJSONObject("push_data").getString("tag"));
        event.setImage(event.getNamespace() + "/" + event.getRepositoryName() + ":" + event.getTag());

        return event;
    }
}