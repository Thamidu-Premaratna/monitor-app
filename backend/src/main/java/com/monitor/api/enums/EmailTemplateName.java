package com.monitor.api.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    NOTIFY_EMAIL("notification_email");
    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
