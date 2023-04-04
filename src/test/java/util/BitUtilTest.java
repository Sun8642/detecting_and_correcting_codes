package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BitUtilTest {

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1,1",
            "4,8",
            "4,15"
    })
    public void leftMostSetBit(int expected, int number) {
        Assertions.assertEquals(expected, BitUtil.leftMostSetBit(number));
    }

    @ParameterizedTest
    @CsvSource({
            "false,0",
            "true,1",
            "true,2",
            "false,3",
            "true,16",
            "false,15",
    })
    public void isPowerOfTwo(boolean expected, int number) {
        Assertions.assertEquals(expected, BitUtil.isPowerOfTwo(number));
    }

    @ParameterizedTest
    @CsvSource({
            "true,0",
            "false,1",
            "false,2",
            "true,3",
            "false,16",
            "true,15",
    })
    public void isNotPowerOfTwo(boolean expected, int number) {
        Assertions.assertEquals(expected, BitUtil.isNotPowerOfTwo(number));
    }

    @ParameterizedTest
    @CsvSource({
            "1,2",
            "2,4",
            "3,8",
            "4,16",
            "5,32",
            "6,64",
    })
    public void binLog(int expected, int number) {
        Assertions.assertEquals(expected, BitUtil.binLog(number));
    }
}