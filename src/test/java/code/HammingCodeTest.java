package code;

import math.BigInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class HammingCodeTest {

    @ParameterizedTest
    @CsvSource({
            "01100000100,0110001,true",
            "110011010010,11001010,false"
    })
    public void encode(String expected, String messageToEncode, boolean parity) {
        BigInt messageToEncodeBigInt = new BigInt(Long.parseLong(messageToEncode, 2));
        HammingCode.encode(messageToEncodeBigInt, parity, messageToEncode.length());
        Assertions.assertEquals(new BigInt(Long.parseLong(expected, 2)), messageToEncodeBigInt);
    }

    @ParameterizedTest
    @CsvSource({
            "false,0110001,01100000100,true",     //No error
            "false,11001010,110011010010,false",  //No error
            "true,0110001,01100000101,true",     //One error
            "true,0110001,01100100100,true",     //One error
            "true,0110110,01100110100,true",     //Two error, original message: 0110001, error can't be corrected correctly
    })
    public void decode(boolean isErrorDetectedExpected, String decodedMessageExpected, String encodedMessage, boolean parity) {
        BigInt message = new BigInt(Long.parseLong(encodedMessage, 2));
        BigInt expectedMessage = new BigInt(Long.parseLong(decodedMessageExpected, 2));
        Assertions.assertEquals(isErrorDetectedExpected, HammingCode.decode(message, parity, decodedMessageExpected.length()));
        Assertions.assertEquals(expectedMessage, message);
    }
}
