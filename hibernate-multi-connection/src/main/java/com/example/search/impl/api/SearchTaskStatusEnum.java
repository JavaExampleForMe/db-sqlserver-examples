package com.example.search.impl.api;

public enum SearchTaskStatusEnum {
    SCHEDULED(1),
    RUNNING(2),
    PAUSED(3),
    CANCELED(4),
    COMPLETED(5);

    private final int code;

    SearchTaskStatusEnum(final int newCode) {
        code = newCode;
    }

    public int getCode() {
        return code;
    }

    public static SearchTaskStatusEnum fromCode(int code) {
        for (SearchTaskStatusEnum b : SearchTaskStatusEnum.values()) {
            if (b.code == code) {
                return b;
            }
        }
        return null;
    }
}
