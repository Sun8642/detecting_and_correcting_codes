package code;

import math.BigInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class HammingCodeTest {

    @ParameterizedTest
    @CsvSource({
            //code(7,4)
            "0000000,0000,true",
            "0001011,0000,false",
            "1111111,1111,true",
            "1110100,1111,false",
            "1010010,1010,true",
            "1011001,1010,false",

            //code(15,11)
            "000000000000000,00000000000,true",
            "000000010001011,00000000000,false",
            "111111111111111,11111111111,true",
            "111111101110100,11111111111,false",
            "100011011000110,10001101001,true",
            "100011001001101,10001101001,false",
    })
    public void encode(String expected, String messageToEncode, boolean parity) {
        BigInt messageToEncodeBigInt = new BigInt(Long.parseLong(messageToEncode, 2));
        HammingCode code = new HammingCode();
        code.setParity(parity);
        code.encode(messageToEncodeBigInt, messageToEncode.length());
        Assertions.assertEquals(new BigInt(Long.parseLong(expected, 2)), messageToEncodeBigInt);
    }

    @ParameterizedTest
    @CsvSource({
            //code(7,4) No error
            "false,0000,0000000,true",
            "false,0000,0001011,false",
            "false,1111,1111111,true",
            "false,1111,1110100,false",
            "false,1010,1010010,true",
            "false,1010,1011001,false",

            //code(7,4) with error(s)
            "true,0000,0000100,true",   //original message: 0000000 a message bit was modified
            "true,0000,0001001,false",  //original message: 0001011 a redundancy bit was modified
            "true,1010,1001001,false",  //original message: 0001011 2 bits modified -> original message cannot be retrieved

            //code(15,11) No error
            "false,00000000000,000000000000000,true",
            "false,00000000000,000000010001011,false",
            "false,11111111111,111111111111111,true",
            "false,11111111111,111111101110100,false",
            "false,10001101001,100011011000110,true",
            "false,10001101001,100011001001101,false",

            //code(15,11) with error(s)
            "true,10001101001,110011011000110,true",    //original message: 100011011000110 a message bit was modified
            "true,10001101001,100011001001100,false",   //original message: 100011001001101 a redundancy bit was modified
            "true,10010100001,100101001001101,false",   //original message: 100011001001101 2 bits modified -> original message cannot be retrieved
    })
    public void decode(boolean isErrorDetectedExpected, String decodedMessageExpected, String encodedMessage, boolean parity) {
        BigInt message = new BigInt(Long.parseLong(encodedMessage, 2));
        BigInt expectedMessage = new BigInt(Long.parseLong(decodedMessageExpected, 2));
        HammingCode code = new HammingCode();
        code.setParity(parity);
        code.decode(message, encodedMessage.length());
        Assertions.assertEquals(isErrorDetectedExpected, code.isErrorDetected());
        Assertions.assertEquals(expectedMessage, message);
    }

    @ParameterizedTest
    @CsvSource({
            "3,4",
            "4,11",
            "5,26",
            "6,57",
            "7,120",
    })
    public void numberOfRedundancyBitsToAdd(int expected, int messageLength) {
        Assertions.assertEquals(expected, HammingCode.numberOfRedundancyBitsToAdd(messageLength));
    }

    @ParameterizedTest
    @CsvSource({
            "3,7",
            "4,15",
            "5,31",
            "6,63",
            "7,127",
    })
    public void numberOfRedundancyBitsAdded(int expected, int encodedMessageLength) {
        Assertions.assertEquals(expected, HammingCode.numberOfRedundancyBitsAdded(encodedMessageLength));
    }
}
