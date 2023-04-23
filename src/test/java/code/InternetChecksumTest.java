package code;

import math.BigInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;
import service.code.InternetChecksum;

public class InternetChecksumTest {

    @ParameterizedTest
    @CsvSource({
            "00000000000000001111111111111111,0000000000000000",
            "0110011001100000010101010101010110001111000011001011010100111101,011001100110000001010101010101011000111100001100"
    })
    public void encode(String expected, String message) {
        BigInt encodedMessage = new BigInt(Long.parseLong(message, 2));
        InternetChecksum code = new InternetChecksum();
        code.encode(encodedMessage, message.length());
        Assertions.assertEquals(new BigInt(Long.parseLong(expected, 2)), encodedMessage);
    }

    @ParameterizedTest
    @CsvSource({
            "0000000000000000,00000000000000001111111111111111",
            "011001100110000001010101010101011000111100001100,0110011001100000010101010101010110001111000011001011010100111101"
    })
    public void decode(String expected, String message) {
        BigInt encodedMessage = new BigInt(Long.parseLong(message, 2));
        InternetChecksum code = new InternetChecksum();
        code.decode(encodedMessage, message.length());
        Assertions.assertEquals(new BigInt(Long.parseLong(expected, 2)), encodedMessage);
    }

    @Test
    public void decode_whenMessageIsCorrupted_shouldThrowException() {
        InternetChecksum code = new InternetChecksum();
        Assertions.assertThrows(RuntimeException.class, () -> {
            String message = "00000100000000001111111111111111";
            code.decode((new BigInt(Long.parseLong(message, 2))), message.length());
        });
    }

    @ParameterizedTest
    @CsvSource({
            "1111111111111111,0000000000000000",
            "1011010100111101,011001100110000001010101010101011000111100001100"
    })
    public void getChecksum(String expected, String message) {
        InternetChecksum code = new InternetChecksum();
        Assertions.assertEquals(new BigInt(Long.parseLong(expected, 2)), code.getChecksum(new BigInt(Long.parseLong(message, 2))));
    }

    @ParameterizedTest
    @CsvSource({
            "false,00000000000000001111111111111111",
            "false,0110011001100000010101010101010110001111000011001011010100111101",
            "true,00000000100000001111111111111111",
            "true,0110011001100000010101010101010110001111000011001011010000111101",


            //All message belows are based on the message:
            //     011001100110000001010101010101011000111100001100 with the checksum: 1011010100111101
            "false,0111011001100000010001010101010110001111000011001011010100111101",  //Corrupted, the 4th bit of the first and second word were flipped
            "true,1110011001100000110101010101010110001111000011001011010100111101",   //Corrupted, the first bit of the first and second word were flipped
            "false,0110011001100000010101010101010110001111000011011011010100111100",  //Corrupted, the 16th bit of the last word and the checksum were flipped
            "false,0110011001100000010101010101010110001111000010101011010100111111",  //Corrupted, the 14th and 15th bit of the last word and the 15th bit of the checksum were flipped
    })
    public void isCorrupted(boolean isCorrupted, String encodedMessage) {
        InternetChecksum code = new InternetChecksum();
        Assertions.assertEquals(isCorrupted, code.isCorrupted(BigInt.from(new BigInteger(encodedMessage, 2))));
    }
}