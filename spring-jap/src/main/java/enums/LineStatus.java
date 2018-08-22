
package enums;

import java.util.Arrays;
import java.util.Optional;

public enum LineStatus {

    INQUEUE(1, "waiting for schedule run", true),
    IN_PROGRESS(2, "in progress", true),
    FAILED(3, "Failed", false),
    COMPLETED(4, "Completed", false),
    LAST_ROUND_PROCESS_PENDING_BATCHES(6, "last round procesing pending batches", false);

    private int value;
    private String description;

    LineStatus(int value, String description, boolean inProgress) {
        this.value = value;
        this.description = description;
        this.inProgress = inProgress;
    }

    private boolean inProgress;

    public boolean isInProgress() {
        return inProgress;
    }


    public static LineStatus getStatusByValue(int selectedValue) {
        Optional<LineStatus> optionalStatus = Arrays.asList(LineStatus.values())
                .stream().filter(status -> status.value == selectedValue)
                .findAny();
        return optionalStatus.isPresent() ? optionalStatus.get() : null;
    }




    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return description;
    }


}
