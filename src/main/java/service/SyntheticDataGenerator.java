package service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import math.BigInt;

import java.util.SplittableRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SyntheticDataGenerator {

    private static final SplittableRandom SPLITTABLE_RANDOM = new SplittableRandom();

    public static BigInt getRandomWord(int numberOfBits) {
        return new BigInt(numberOfBits, SPLITTABLE_RANDOM);
    }

    public static String getRandomWordToString(int numberOfBits) {
        return getRandomWord(numberOfBits).toBinaryString(numberOfBits);
    }
}
