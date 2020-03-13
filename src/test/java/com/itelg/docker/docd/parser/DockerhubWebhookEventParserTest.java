package com.itelg.docker.docd.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.itelg.docker.docd.test.support.DomainTestSupport;

public class DockerhubWebhookEventParserTest implements DomainTestSupport
{
    private WebHookEventParser parser = new DockerhubWebhookEventParser();

    @Test
    public void testParse() throws IOException
    {
        try (InputStream inputStream = new ClassPathResource("webhookevent_dockerhub.json").getInputStream())
        {
            var webHookEvent = parser.parse(IOUtils.toString(inputStream, Charset.defaultCharset()));
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getBaseWebHookEvent());
        }
    }
}
