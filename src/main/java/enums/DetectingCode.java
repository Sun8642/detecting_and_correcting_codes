package enums;

import lombok.Getter;

@Getter
public enum DetectingCode {
    PARITY_BIT_CODE("parityBit", false),
    CYCLIC_REDUNDANCY_CODE("CRC", false),
    INTERNET_CHECKSUM("internetChecksum", false),
    HAMMING_CODE("hamming", true);

    private final String argumentName;
    private final boolean canCorrectError;

    DetectingCode(String argumentName, boolean canCorrectError) {
        this.argumentName = argumentName;
        this.canCorrectError = canCorrectError;
    }

    public static String getArgumentNamesForConsole() {
        StringBuilder stringBuilder = new StringBuilder();
        for (DetectingCode detectingCode : values()) {
            stringBuilder.append(" \n - ").append(detectingCode.argumentName);
        }
        return stringBuilder.toString();
    }
}
