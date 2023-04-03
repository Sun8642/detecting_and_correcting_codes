package enums;

import lombok.Getter;

@Getter
public enum MainCommand {
    ENCODE("encode"),
    DECODE("decode"),
    GENERATE_MESSAGE("generateMessage"),
    CORRUPT_MESSAGE("corruptMessage"),
    GENERATE_GRAPH("generateErrorDetectingRateGraph");

    private final String argumentName;

    MainCommand(String argumentName) {
        this.argumentName = argumentName;
    }

    public static String getArgumentNamesForConsole() {
        StringBuilder stringBuilder = new StringBuilder();
        for (MainCommand mainCommand : values()) {
            stringBuilder.append(" \n - ").append(mainCommand.argumentName);
        }
        return stringBuilder.toString();
    }
}
