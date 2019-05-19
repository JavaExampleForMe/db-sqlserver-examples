package jdbc.connection.pool;

import java.util.Arrays;

public enum StatusType {
    Up(1),
    Down(2);

    private final int code;
    StatusType(final int newCode) {
        code = newCode;
    }
    public static StatusType fromCode(int code) {
        return Arrays.stream(StatusType.values())
                .filter((StatusType b) -> b.code == code)
                .findFirst()
                .orElse(null);
    }

}
