package code;

import math.BigInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

public class CyclicRedundancyCodeTest {

    @ParameterizedTest
    @CsvSource({
            "1011010,10110,101",
            "10110100,101101,101",
            "100100001,100100,1101",
            "10011101100,10011101,1001",
            "1101010000000010111110,110101,11000000000000101"
    })
    public void encode(String expected, String message, String generatorPolynomial) {
        BigInt encodedMessage = new BigInt(Long.parseLong(message, 2));
        CyclicRedundancyCode.encode(encodedMessage, new BigInt(Long.parseLong(generatorPolynomial, 2)));
        Assertions.assertEquals(new BigInt(Long.parseLong(expected, 2)), encodedMessage);
    }

    @Test
    public void decode() {
        BigInt message = new BigInt(Long.parseLong("1011010", 2));
        CyclicRedundancyCode.decode(message, new BigInt(Long.parseLong("101", 2)));
        Assertions.assertEquals(new BigInt(Long.parseLong("10110", 2)), message);
    }

    @Test
    public void decode_whenMessageIsCorrupted_shouldThrowException() {
        Assertions.assertThrows(RuntimeException.class, () -> CyclicRedundancyCode.decode(
                new BigInt(Long.parseLong("1011011", 2)),
                new BigInt(Long.parseLong("101", 2)))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1011010,101",
            "10110100,101",
            "100100001,1101",
            "10011101100,1001",
            "1101010000000010111110,11000000000000101"
    })
    public void isCorrupted_whenDataIsNotCorrupted(String encodedMessage, String generatorPolynomial) {
        Assertions.assertFalse(CyclicRedundancyCode.isCorrupted(
                new BigInt(Long.parseLong(encodedMessage, 2)),
                new BigInt(Long.parseLong(generatorPolynomial, 2)))
        );
    }

    @Test
    public void isCorrupted_whenMessageIsCorrupted() {
        Assertions.assertTrue(CyclicRedundancyCode.isCorrupted(
                new BigInt(Long.parseLong("1011011", 2)),
                new BigInt(Long.parseLong("101", 2)))
        );
    }
}