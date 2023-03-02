package enums;

import lombok.Getter;

@Getter
public enum ErrorChannelModel {
    CONSTANT_ERROR_CHANNEL_MODEL("constantError"),
    BURST_ERROR_CHANNEL_MODEL("burstError");

    private final String argumentName;

    ErrorChannelModel(String argumentName) {
        this.argumentName = argumentName;
    }

    public static String getArgumentNamesForConsole() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ErrorChannelModel detectingCode : values()) {
            stringBuilder.append(" \n - ").append(detectingCode.argumentName);
        }
        return stringBuilder.toString();
    }
}
