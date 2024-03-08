package com.suse.manager.webui.utils.salt.custom;

import com.suse.salt.netapi.results.Ret;
import com.suse.salt.netapi.results.StateApplyResult;
import com.suse.utils.Json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AppStreamsChangeSlsResult {

    private class ChangesResult {
        List<String> enabled;
        List<String> disabled;
        @SerializedName("currently_enabled")
        Set<Map<String, String>> currentlyEnabled;
    }

    @SerializedName("appstreams_|-change_appstreams_|-change_appstreams_|-change")
    private Optional<StateApplyResult<Ret<ChangesResult>>> changes = Optional.empty();

    public Optional<StateApplyResult<Ret<ChangesResult>>> getChanges() {
        return changes;
    }

    public Set<Map<String, String>> getCurrentlyEnabled() {
        return changes.get().getChanges().getRet().currentlyEnabled;
    }

    public static void main(String[] args) {
        String json = "{\"appstreams_|-change_appstreams_|-change_appstreams_|-change\":{\"name\":\"change_appstreams\",\"result\":true,\"changes\":{\"ret\":{\"disabled\":[\"nginx\"],\"enabled\":[\"maven:3.8\"],\"currently_enabled\":[{\"name\":\"maven\",\"stream\":\"3.8\",\"version\":\"9020020230511160017\",\"context\":\"4b0b4b45\",\"architecture\":\"x86_64\"}]}},\"comment\":\"Successfully changed AppStreams state.\",\"__sls__\":\"appstreams.change\",\"__run_num__\":0,\"start_time\":\"21:30:01.760012\",\"duration\":1347.127,\"__id__\":\"change_appstreams\"}}";

        JsonParser parser = new JsonParser();
        JsonElement jsonResult = parser.parse(json);

        var result = Json.GSON.fromJson(jsonResult, AppStreamsChangeSlsResult.class);

        java.lang.System.out.println(result.getChanges().get().getChanges().getRet().currentlyEnabled);
    }
}
