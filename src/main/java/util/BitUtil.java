package util;

import java.util.BitSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BitUtil {

    public static int leftMostSetBit(int number) {
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

    public static BigInteger insertBit(BigInteger bigInteger, int bitPosition, boolean bitSet) {
        BigInteger mask = BigInteger.ONE.shiftLeft(bitPosition).subtract(BigInteger.ONE);
        BigInteger rightVal = bigInteger.and(mask);
        return bigInteger.subtract(rightVal).shiftLeft(1).add(rightVal).add(bitSet ? BigInteger.ONE.shiftLeft(bitPosition) : BigInteger.ZERO);
    }

    public static long insertBit(long number, int bitPosition, boolean bitSet) {
        long mask = (1L << bitPosition) - 1;
        long rightVal = number & mask;
        return ((number - rightVal) << 1) + rightVal + (bitSet ? (1L << bitPosition) : 0L);
    }
}
