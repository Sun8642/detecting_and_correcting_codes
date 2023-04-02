package util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import math.BigInt;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;
import java.util.SplittableRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SyntheticDataGenerator {

    private static final SplittableRandom SPLITTABLE_RANDOM = new SplittableRandom();

    public static BigInt getRandomWord(int numberOfBits) {
        return new BigInt(numberOfBits, SPLITTABLE_RANDOM);
    }

    public static void corruptWord(BigInt message, double p) {
        int leftMostSetBit = message.getLeftMostSetBit();
        for (int i = 0; i < leftMostSetBit; i++) {
            if (Math.random() <= p) {
                message.flipBit(i);
            }
        }
    }

    public static void corruptWord(BigInt message, double p, boolean isBurstError, int burstErrorLength) {
        int leftMostSetBit = message.getLeftMostSetBit();
        for (int i = 0; i < leftMostSetBit; i++) {
            if (Math.random() <= p) {
                if (isBurstError) {
                    message.flipBit(i);
                    for (int j = 1; j < burstErrorLength - 2; j++) {
                        if (Math.random() <= p) {
                            message.flipBit(i + j);
                        }
                    }
                    message.flipBit(i + burstErrorLength - 1);
                    i += (burstErrorLength - 1);
                } else {
                    message.flipBit(i);
                }
            }
        }
    }
}
