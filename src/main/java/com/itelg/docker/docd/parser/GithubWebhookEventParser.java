package com.itelg.docker.docd.parser;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.itelg.docker.docd.domain.WebHookEvent;

@Component
public class GithubWebhookEventParser implements WebHookEventParser
{
    @Override
    public WebHookEvent parse(String json)
    {
        var object = new JSONObject(json);
        var webHookEvent = new WebHookEvent();

        var registryUrl = object.getJSONObject("package").getJSONObject("registry").getString("url").replace("https://", "").split("/");
        webHookEvent.setNamespace(registryUrl[0] + "/" + registryUrl[1]);
        webHookEvent.setRepositoryName(registryUrl[2]);
        webHookEvent.setTag(object.getJSONObject("package").getJSONObject("package_version").getString("version"));
        webHookEvent.setImage(webHookEvent.getNamespace() + "/" + webHookEvent.getRepositoryName() + ":" + webHookEvent.getTag());

        return webHookEvent;
    }
}
