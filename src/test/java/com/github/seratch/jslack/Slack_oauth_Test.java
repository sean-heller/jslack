package com.github.seratch.jslack;

import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.oauth.OAuthAccessRequest;
import com.github.seratch.jslack.api.methods.request.oauth.OAuthTokenRequest;
import com.github.seratch.jslack.api.methods.response.oauth.OAuthAccessResponse;
import com.github.seratch.jslack.api.methods.response.oauth.OAuthTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import testing.SlackTestConfig;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
public class Slack_oauth_Test {

    Slack slack = Slack.getInstance(SlackTestConfig.get());

    // TODO: valid test; currently just checking the API access
    @Test
    public void access() throws IOException, SlackApiException {
        {
            OAuthAccessResponse response = slack.methods().oauthAccess(OAuthAccessRequest.builder()
                    .clientId("3485157640.XXXX")
                    .clientSecret("XXXXX")
                    .code("")
                    .redirectUri("http://seratch.net/foo")
                    .build());
            assertThat(response.getError(), is("invalid_code"));
            assertThat(response.isOk(), is(false));
        }
    }

    // TODO: valid test; currently just checking the API access
    @Test
    public void token() throws IOException, SlackApiException {
        {
            OAuthTokenResponse response = slack.methods().oauthToken(OAuthTokenRequest.builder()
                    .clientId("3485157640.XXXX")
                    .clientSecret("XXXXX")
                    .code("")
                    .redirectUri("http://seratch.net/foo")
                    .build());
            assertThat(response.getError(), is("invalid_code"));
            assertThat(response.isOk(), is(false));
        }
    }

}