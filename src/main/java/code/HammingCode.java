package code;

import java.util.BitSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.HammingResponse;
import util.BitUtil;
import util.SyntheticDataGenerator;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HammingCode {

    public static int hammingDistance(String codeWord1, String codeWord2) {
        return Integer.bitCount(Integer.parseInt(codeWord1, 2) ^ Integer.parseInt(codeWord2, 2));
    }

    public static int hammingDistance(String codeWord) {
        return hammingDistance(codeWord, "0");
    }

    public static String encode(String message, boolean parity) {
        int numberOfRedundancyBitsToAdd = numberOfRedundancyBitsToAdd(message.length());
        StringBuilder encodedMessage = new StringBuilder(message);

        //Transform block into coded block with all the redundancy bit initialized at 0 (to have the final positions of non redundancy bits)
        for (int i = 0; i < numberOfRedundancyBitsToAdd; i++) {
            encodedMessage.insert((int) (encodedMessage.length() - Math.pow(2.d, i)) + 1, '0');
        }

        //Replace redundancy bits if needed
        for (int i = 0; i < numberOfRedundancyBitsToAdd; i++) {
            //For each position of bit in the message, we need to compute the sum of the ith bit representation of the position
            int bitPosition = 1 << i;
            int numberOfOneForBitPosition = 0;

            //Calculate the number of bit set
            for (int j = 1; j <= encodedMessage.length(); j++) {
                if (encodedMessage.charAt(encodedMessage.length() - j) == '1') {
                    if ((bitPosition & j) != 0) {
                        numberOfOneForBitPosition++;
                    }
                }
            }
            if ((parity && numberOfOneForBitPosition % 2 == 1) || (!parity && numberOfOneForBitPosition % 2 == 0)) {
                //The redundancy bit need to be 1
                int redundancyBitPositionInCodedMessage = encodedMessage.length() - bitPosition;
                encodedMessage.replace(redundancyBitPositionInCodedMessage, redundancyBitPositionInCodedMessage + 1, "1");
            }
        }

        return encodedMessage.toString();
    }

    public static BigInteger encode(BigInteger message, boolean parity, int k) {
        int numberOfRedundancyBitsToAdd = numberOfRedundancyBitsToAdd(k);

        //Transform block into coded block with all the redundancy bit initialized at 0 (to have the final positions of non redundancy bits)
        for (int i = 0; i < numberOfRedundancyBitsToAdd; i++) {
            message = BitUtil.insertBit(message, (int) (Math.pow(2.d, i)) - 1, false);
        }

        //Replace redundancy bits if needed
        for (int i = 0; i < numberOfRedundancyBitsToAdd; i++) {
            //For each position of bit in the message, we need to compute the sum of the ith bit representation of the position
            int bitPosition = 1 << i;
            int numberOfOneForBitPosition = 0;

            //Calculate the number of bit set
            for (int j = 2; j < message.bitLength(); j++) {
                if (message.testBit(j)) {
                    if ((bitPosition & (j + 1)) != 0) {
                        numberOfOneForBitPosition++;
                    }
                }
            }
            if ((parity && numberOfOneForBitPosition % 2 == 1) || (!parity && numberOfOneForBitPosition % 2 == 0)) {
                //The redundancy bit need to be 1
                message = message.setBit(bitPosition - 1);
            }
        }

        return message;
    }

    public static long encode(long message, boolean parity, int k) {
        int numberOfRedundancyBitsToAdd = numberOfRedundancyBitsToAdd(k);
        int n = k + numberOfRedundancyBitsToAdd;

        //Transform block into coded block with all the redundancy bit initialized at 0 (to have the final positions of non redundancy bits)
        for (int i = 0; i < numberOfRedundancyBitsToAdd; i++) {
            message = BitUtil.insertBit(message, (int) (Math.pow(2.d, i)) - 1, false);
        }

        //Replace redundancy bits if needed
        for (int i = 0; i < numberOfRedundancyBitsToAdd; i++) {
            //For each position of bit in the message, we need to compute the sum of the ith bit representation of the position
            int bitPosition = 1 << i;
            int numberOfOneForBitPosition = 0;

            //Calculate the number of bit set
            for (int j = 3; j <= n; j++) {
                if ((message & (1L << (j - 1))) != 0 && ((j & bitPosition) != 0)) {
                    numberOfOneForBitPosition++;
                }
            }
            if ((parity && numberOfOneForBitPosition % 2 == 1) || (!parity && numberOfOneForBitPosition % 2 == 0)) {
                //The redundancy bit need to be 1
                message = message | (1L << (bitPosition - 1));
            }
        }

        return message;
    }

    //Only works for correct hamming code (where coded message length is equal to a power of 2 minus 1
    public static HammingResponse decode(String encodedMessage, boolean parity) {
        int numberOfRedundancyBitsAdded = BitUtil.leftMostSetBit(encodedMessage.length());
        StringBuilder encodedMessageStrBuilder = new StringBuilder(encodedMessage);

        int errorBitPosition = 0;

        //Replace redundancy bits if needed
        for (int i = 0; i < numberOfRedundancyBitsAdded; i++) {
            //For each position of bit in the message, we need to compute the sum of the ith bit representation of the position
            int bitPosition = 1 << i;
            int numberOfOneForBitPosition = 0;

            //Calculate the number of bit set
            for (int j = 1; j <= encodedMessageStrBuilder.length(); j++) {
                if (encodedMessageStrBuilder.charAt(encodedMessageStrBuilder.length() - j) == '1') {

                    //We need to ignore redundancy bit, so we check if the bit that is one is not a power of 2
                    if ((bitPosition & j) != 0 && BitUtil.isNotPowerOfTwo(j)) {
                        numberOfOneForBitPosition++;
                    }
                }
            }

            if (isRedundancyBitIncorrect(bitPosition, numberOfOneForBitPosition, encodedMessage, parity)) {
                errorBitPosition += bitPosition;
            }
        }
        if (errorBitPosition != 0) {
            int indexToFix = encodedMessageStrBuilder.length() - errorBitPosition;
            encodedMessageStrBuilder.replace(indexToFix, indexToFix + 1, encodedMessageStrBuilder.charAt(indexToFix) == '0' ? "1" : "0");
        }

        //Remove redundancy bits
        for (int i = 0; i < numberOfRedundancyBitsAdded; i++) {
            int bitPosition = 1 << i;
            int indexToRemove = encodedMessage.length() - bitPosition;
            encodedMessageStrBuilder.replace(indexToRemove, indexToRemove + 1, "");
        }

        return new HammingResponse(errorBitPosition != 0, encodedMessageStrBuilder.toString());
    }

    private static boolean isRedundancyBitIncorrect(int bitPosition, int numberOfOneForBitPosition, String encodedMessage, boolean parity) {
        boolean isBitSetAtPosition = encodedMessage.charAt(encodedMessage.length() - bitPosition) == '1';
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

    public static double[] getProbabilityOfSuccess(int iterations, double p, int messageBitSize) {
        String message = SyntheticDataGenerator.getRandomWord(messageBitSize);
        String encodedMessage = encode(message, true);
        int nbMessageWithIntegrity = 0;
        int nbCorruptedMessageCorrectlyDetected = 0;
        int nbCorruptedMessageCorrectlyCorrected = 0;

        String corruptedMessage;
        for (int i = 0; i < iterations; i++) {
            corruptedMessage = SyntheticDataGenerator.corruptWord(encodedMessage, p);
            if (encodedMessage.equals(corruptedMessage)) {
                nbMessageWithIntegrity++;
            } else {
                HammingResponse hammingResponse = decode(corruptedMessage, true);
                if (hammingResponse.getDecodedMessage().equals(message)) {
                    nbCorruptedMessageCorrectlyCorrected++;
                }
                if (hammingResponse.isErrorDetected()) {
                    nbCorruptedMessageCorrectlyDetected++;
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
