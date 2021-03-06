package com.github.seratch.jslack;

import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.emoji.EmojiListRequest;
import com.github.seratch.jslack.api.methods.response.emoji.EmojiListResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import testing.Constants;
import testing.SlackTestConfig;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@Slf4j
public class Slack_emoji_Test {

    Slack slack = Slack.getInstance(SlackTestConfig.get());

    @Test
    public void emoji() throws IOException, SlackApiException {
        String token = System.getenv(Constants.SLACK_TEST_OAUTH_ACCESS_TOKEN);
        {
            EmojiListResponse response = slack.methods().emojiList(EmojiListRequest.builder().token(token).build());
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
            assertThat(response.getEmoji(), is(notNullValue()));
        }
    }

}