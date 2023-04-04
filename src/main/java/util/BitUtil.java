package util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BitUtil {

    public static int leftMostSetBit(long number) {
        int posLeftMostSetBit = 0;
        while (number > 0) {
            number = number >> 1;
            posLeftMostSetBit++;
        }
        return posLeftMostSetBit;
    }

    public static boolean isPowerOfTwo(int number) {
        return (number != 0) && (number & (number - 1)) == 0;
    }

    public static boolean isNotPowerOfTwo(int number) {
        return !(isPowerOfTwo(number));
    }

    //https://stackoverflow.com/questions/3305059/how-do-you-calculate-log-base-2-in-java-for-integers
    public static int binLog(int bits) { // returns 0 for bits=0
        int log = 0;
        if ((bits & 0xffff0000) != 0) {
            bits >>>= 16;
            log = 16;
        }
        if (bits >= 256) {
            bits >>>= 8;
            log += 8;
        }
        if (bits >= 16) {
            bits >>>= 4;
            log += 4;
        }
        if (bits >= 4) {
            bits >>>= 2;
            log += 2;
        }
        return log + (bits >>> 1);
    }
}
