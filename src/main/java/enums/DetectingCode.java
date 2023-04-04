package enums;

import lombok.Getter;

@Getter
public enum DetectingCode {
    PARITY_BIT_CODE("parityBit"),
    CYCLIC_REDUNDANCY_CODE("CRC"),
    INTERNET_CHECKSUM("internetChecksum"),
    HAMMING_CODE("hamming");

    private final String argumentName;

    DetectingCode(String argumentName) {
        this.argumentName = argumentName;
    }

    public static String getArgumentNamesForConsole() {
        StringBuilder stringBuilder = new StringBuilder();
        for (DetectingCode detectingCode : values()) {
            stringBuilder.append(" \n - ").append(detectingCode.argumentName);
        }
        return stringBuilder.toString();
    }
}
