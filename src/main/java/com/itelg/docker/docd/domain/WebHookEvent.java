package com.itelg.docker.docd.domain;

import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private String namespace;

    @NotBlank
    private String repositoryName;

    @NotBlank
    private String tag;

    @NotBlank
    private String image;
}
