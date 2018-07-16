package com.example.search.impl.api;

public enum SearchBatchStatusEnum {
    SCHEDULED(1),
    RUNNING(2),
    DONE(3),
    FAILED(4);

    private final int code;

    SearchBatchStatusEnum(final int newCode) {
        code = newCode;
    }

    public int getCode() {
        return code;
    }

    public static SearchBatchStatusEnum fromCode(int code) {
        for (SearchBatchStatusEnum b : SearchBatchStatusEnum.values()) {
            if (b.code == code) {
                return b;
            }
        }
        return null;
    }
}
