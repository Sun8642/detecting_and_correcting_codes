package code;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import math.BigInt;
import util.BitUtil;
import util.SyntheticDataGenerator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HammingCode {

    public static void encode(BigInt message, boolean parity, int k) {
        int numberOfRedundancyBitsToAdd = numberOfRedundancyBitsToAdd(k);

        //Transform block into coded block with all the redundancy bit initialized at 0 (to have the final positions of non redundancy bits)
        for (int i = 0; i < numberOfRedundancyBitsToAdd; i++) {
            message.insertBit((int) (Math.pow(2.d, i)) - 1, false);
        }

        int leftMostSetBit = message.getLeftMostSetBit();

        //Replace redundancy bits if needed
        for (int i = 0; i < numberOfRedundancyBitsToAdd; i++) {
            //For each position of bit in the message, we need to compute the sum of the ith bit representation of the position
            int bitPosition = 1 << i;
            int numberOfOneForBitPosition = 0;

            //Calculate the number of bit set
            for (int j = 2; j < leftMostSetBit; j++) {
                if (message.testBit(j)) {
                    if ((bitPosition & (j + 1)) != 0) {
                        numberOfOneForBitPosition++;
                    }
                }
            }
            if ((parity && numberOfOneForBitPosition % 2 == 1) || (!parity && numberOfOneForBitPosition % 2 == 0)) {
                //The redundancy bit need to be 1
                message.setBit(bitPosition - 1);
            }
        }
    }

    /**
     *
     * @param encodedMessage the encoded message which need to be decoded
     * @param parity
     * @return true if an error was detected
     */
    public static boolean decode(BigInt encodedMessage, boolean parity) {
        int leftMostSetBit = encodedMessage.getLeftMostSetBit();
        int numberOfRedundancyBitsAdded = BitUtil.leftMostSetBit(leftMostSetBit);

        int errorBitPosition = 0;

        //Replace redundancy bits if needed
        for (int i = 0; i < numberOfRedundancyBitsAdded; i++) {
            //For each position of bit in the message, we need to compute the sum of the ith bit representation of the position
            int bitPosition = 1 << i;
            int numberOfOneForBitPosition = 0;

            //Calculate the number of bit set
            for (int j = 2; j < leftMostSetBit; j++) {
                if (encodedMessage.testBit(j)) {

                    //We need to ignore redundancy bit, so we check if the bit that is one is not a power of 2
                    if ((bitPosition & (j + 1)) != 0 && BitUtil.isNotPowerOfTwo(j + 1)) {
                        numberOfOneForBitPosition++;
                    }
                }
            }

            if (isRedundancyBitIncorrect(bitPosition, numberOfOneForBitPosition, encodedMessage, parity)) {
                errorBitPosition += bitPosition;
            }
        }
        if (errorBitPosition != 0) {
            encodedMessage.flipBit(errorBitPosition - 1);
        }

        //Remove redundancy bits
        int indexToRemove = 1 << (numberOfRedundancyBitsAdded - 1);
        for (int i = 0; i < numberOfRedundancyBitsAdded; i++) {
            encodedMessage.removeBit(indexToRemove - 1);
            indexToRemove >>= 1;
        }

        return errorBitPosition != 0;
    }

    private static boolean isRedundancyBitIncorrect(int bitPosition, int numberOfOneForBitPosition, BigInt encodedMessage, boolean parity) {
        boolean isBitSetAtPosition = encodedMessage.testBit(bitPosition - 1);
        if (parity) {
            return (numberOfOneForBitPosition % 2 == 1 && !isBitSetAtPosition) ||
                    (numberOfOneForBitPosition % 2 == 0 && isBitSetAtPosition);
        } else {
            return (numberOfOneForBitPosition % 2 == 1 && isBitSetAtPosition) ||
                    (numberOfOneForBitPosition % 2 == 0 && !isBitSetAtPosition);
        }
    }

    public static int numberOfRedundancyBitsToAdd(int messageLength) {
        int n = 0;
        int power = 1;
        while (power < (messageLength + n + 1)) {
            n++;
            power *= 2;
        }
        return n;
    }

    public static boolean isKValid(int k) {
        return k >= 4 && BitUtil.isPowerOfTwo(k + numberOfRedundancyBitsToAdd(k) + 1);
    }

    public static double[] getErrorDetectionRate(int iterations, double p, int messageBitSize) {
        BigInt encodedMessage = SyntheticDataGenerator.getRandomWord(messageBitSize);
        encode(encodedMessage, true, messageBitSize);
        int nbMessageWithIntegrity = 0;
        int nbCorruptedMessageCorrectlyDetected = 0;
        int nbCorruptedMessageCorrectlyCorrected = 0;

        BigInt corruptedMessage = new BigInt(encodedMessage);
        for (int i = 0; i < iterations; i++) {
            SyntheticDataGenerator.corruptWord(corruptedMessage, p);
            if (encodedMessage.equals(corruptedMessage)) {
                nbMessageWithIntegrity++;
            } else {
                if (decode(corruptedMessage, true)) {
                    nbCorruptedMessageCorrectlyDetected++;
                }
                if (corruptedMessage.equals(encodedMessage)) {
                    nbCorruptedMessageCorrectlyCorrected++;
                }
            }
        }
        if (iterations - nbMessageWithIntegrity == 0) {
            return new double[]{1.d, 1.d};
        }
        return new double[]{
                (double) nbCorruptedMessageCorrectlyDetected / (iterations - nbMessageWithIntegrity),
                (double) nbCorruptedMessageCorrectlyCorrected / (iterations - nbMessageWithIntegrity)
        };
    }
}
