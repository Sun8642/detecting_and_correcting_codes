package code;

import math.BigInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import service.code.ParityBitCode;

public class ParityBitCodeTest {

    @ParameterizedTest
    @CsvSource({
            "0.989559,16,0.01",
            "0.671228,16,0.1"
    })
    public void getProbabilityOfDetectingError(double expected, int N, double p) {
        Assertions.assertEquals(expected, ParityBitCode.getProbabilityOfDetectingError(N, p), 0.001d);
    }

    @ParameterizedTest
    @CsvSource({
            "00000,0000",
            "11110,1111",
            "101101,10110",
            "1011010,101101",
    })
    public void encode(String expected, String message) {
        BigInt encodedMessage = new BigInt(Long.parseLong(message, 2));
        ParityBitCode code = new ParityBitCode();
        code.encode(encodedMessage, message.length());
        Assertions.assertEquals(new BigInt(Long.parseLong(expected, 2)), encodedMessage);
    }

    @ParameterizedTest
    @CsvSource({
            "0000,00000",
            "1111,11110",
            "10110,101101",
            "101101,1011010",
    })
    public void decode(String expected, String message) {
        BigInt decodedMessage = new BigInt(Long.parseLong(message, 2));
        ParityBitCode code = new ParityBitCode();
        code.decode(decodedMessage, message.length());
        Assertions.assertEquals(new BigInt(Long.parseLong(expected, 2)), decodedMessage);
    }

    @ParameterizedTest
    @CsvSource({
            "00001",
            "11111",
            "101100",
            "1011011",
    })
    public void decode_whenMessageIsCorrupted_shouldThrowException(String encodedMessage) {
        ParityBitCode code = new ParityBitCode();
        Assertions.assertThrows(RuntimeException.class, () -> code.decode(new BigInt(Long.parseLong(encodedMessage, 2)), encodedMessage.length()));
    }
}