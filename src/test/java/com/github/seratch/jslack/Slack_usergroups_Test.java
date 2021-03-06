package com.github.seratch.jslack;

import com.github.seratch.jslack.api.methods.request.usergroups.*;
import com.github.seratch.jslack.api.methods.request.usergroups.users.UsergroupUsersListRequest;
import com.github.seratch.jslack.api.methods.request.usergroups.users.UsergroupUsersUpdateRequest;
import com.github.seratch.jslack.api.methods.request.users.UsersListRequest;
import com.github.seratch.jslack.api.methods.response.usergroups.*;
import com.github.seratch.jslack.api.methods.response.usergroups.users.UsergroupUsersListResponse;
import com.github.seratch.jslack.api.methods.response.usergroups.users.UsergroupUsersUpdateResponse;
import com.github.seratch.jslack.api.methods.response.users.UsersListResponse;
import com.github.seratch.jslack.api.model.Usergroup;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import testing.Constants;
import testing.SlackTestConfig;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@Slf4j
public class Slack_usergroups_Test {

    Slack slack = Slack.getInstance(SlackTestConfig.get());
    String token = System.getenv(Constants.SLACK_TEST_OAUTH_ACCESS_TOKEN);

    @Test
    public void create() throws Exception {
        UsergroupsCreateResponse response = slack.methods().usergroupsCreate(UsergroupsCreateRequest.builder()
                .token(token)
                .name("usergroup-" + System.currentTimeMillis())
                .build());
        assertThat(response.isOk(), is(false));
        assertThat(response.getError(), is(anyOf(
                // For a good old token, "paid_teams_only" can be returned as the error
                equalTo("paid_teams_only"),
                // As of 2018, this code is generally returned for newly created token
                equalTo("missing_scope")
        )));
    }

    @Test
    public void list() throws Exception {
        UsergroupsListResponse response = slack.methods().usergroupsList(UsergroupsListRequest.builder().token(token).build());
        assertThat(response.getError(), is(nullValue()));
        assertThat(response.isOk(), is(true));
    }

    @Test
    public void usergroups() throws Exception {
        UsergroupsListResponse usergroups = slack.methods().usergroupsList(UsergroupsListRequest.builder().token(token).build());
        if (usergroups.isOk() && usergroups.getUsergroups().size() > 0) {
            UsergroupUsersListResponse response = slack.methods().usergroupUsersList(
                    UsergroupUsersListRequest.builder()
                            .token(token)
                            .includeDisabled(false)
                            .usergroup(usergroups.getUsergroups().get(0).getId())
                            .build());
            assertThat(response.getError(), is(nullValue()));
        }

        Usergroup usergroup = null;
        {
            UsergroupsCreateResponse response = slack.methods().usergroupsCreate(UsergroupsCreateRequest.builder()
                    .token(token)
                    .name("usergroup-" + System.currentTimeMillis())
                    .description("Something wrong")
                    .build());
            assertThat(response.getError(), is(nullValue()));
            usergroup = response.getUsergroup();
        }
        {
            UsergroupsDisableResponse response = slack.methods().usergroupsDisable(UsergroupsDisableRequest.builder()
                    .token(token)
                    .usergroup(usergroup.getId())
                    .build());
            assertThat(response.getError(), is(nullValue()));
        }
        {
            UsergroupsEnableResponse response = slack.methods().usergroupsEnable(UsergroupsEnableRequest.builder()
                    .token(token)
                    .usergroup(usergroup.getId())
                    .build());
            assertThat(response.getError(), is(nullValue()));
        }
        {
            UsergroupsUpdateResponse response = slack.methods().usergroupsUpdate(UsergroupsUpdateRequest.builder()
                    .token(token)
                    .usergroup(usergroup.getId())
                    .description("updated")
                    .build());
            assertThat(response.getError(), is(nullValue()));
        }
        {
            UsersListResponse usersListResponse = slack.methods().usersList(UsersListRequest.builder()
                    .token(token)
                    .limit(3)
                    .build());
            List<String> userIds = usersListResponse.getMembers().stream().map(u -> u.getId()).collect(toList());
            UsergroupUsersUpdateResponse response = slack.methods().usergroupUsersUpdate(UsergroupUsersUpdateRequest.builder()
                    .token(token)
                    .usergroup(usergroup.getId())
                    .users(userIds)
                    .build());
            assertThat(response.getError(), is(nullValue()));
        }
    }

    @Test
    public void users_failure() throws Exception {
        UsergroupUsersListResponse response = slack.methods().usergroupUsersList(
                UsergroupUsersListRequest.builder()
                        .token(token)
                        .includeDisabled(false)
                        .usergroup("dummy")
                        .build());
        assertThat(response.isOk(), is(false));
//            assertThat(response.getError(), is("missing_required_argument"));
        // As of 2018/05, the error message has been changed
        assertThat(response.getError(), is("no_such_subteam"));
    }

}