package com.itelg.docker.docd.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class WebHookEvent
{
    private String namespace;

    private String repositoryName;

    private String tag;

    private String image;
}