package com.bigbade.skriptbot.utils;

import com.github.cliftonlabs.json_simple.JsonKey;
import lombok.Getter;

import java.util.Collections;

public enum JsonKeys {
    DOC("doc", null),
    URL("url", null),
    DATA("data", Collections.emptyMap()),
    DESC("desc", null),
    NAME("name", null),
    ADDON("addon", null),
    BYTES("bytes", 0L),
    ERROR("error", null),
    ASSETS("assets", null),
    EXAMPLE("example", null),
    PLUGIN("plugin", null),
    PATTERN("pattern", null),
    RETURNED("returned", 0),
    SUCCESS("success", false),
    VERSION("version", null),
    DOWNLOAD("download", null),
    RESPONSE("response", null),
    TAG_NAME("tag_name", null),
    DESCRIPTION("description", null),
    CONTENT_TYPE("content_type", null);

    @Getter
    private final JsonKey key;

    JsonKeys(String key, Object defaultValue) {
        this.key = new JsonKey() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Object getValue() {
                return defaultValue;
            }
        };
    }
}
