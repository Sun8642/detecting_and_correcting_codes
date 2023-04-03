package code;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import math.BigInt;
import util.BitUtil;
import util.SyntheticDataGenerator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InternetChecksum {

    private static final BigInt MASK_16_BITS_BIG_INT = new BigInt(0xffff);

    public static void encode(BigInt message) {
        BigInt checksum = getChecksum(message);
        message.shiftLeft(16);
        message.add(checksum);
    }

    public static void decode(BigInt encodedMessage) {
        if (isCorrupted(encodedMessage)) {
            throw new RuntimeException("Could not decode message, the encoded message is corrupted");
        }
        encodedMessage.shiftRight(16);
    }

    public static BigInt getChecksum(BigInt message) {
        BigInt sumOfWords = getSumOfWords(message);
        sumOfWords.not();
        sumOfWords.and(MASK_16_BITS_BIG_INT);
        return sumOfWords;
    }

    public static boolean isCorrupted(BigInt encodedMessage) {
        BigInt decodedMessage = new BigInt(encodedMessage);
        decodedMessage.shiftRight(16);

        BigInt encodedMessageChecksum = new BigInt(MASK_16_BITS_BIG_INT);
        encodedMessageChecksum.and(encodedMessage);

        return !getChecksum(decodedMessage).equals(encodedMessageChecksum);
    }

    private static BigInt getSumOfWords(BigInt message) {
        long result = 0;
        for (int i = 0; i < message.getDig().length; i++) {
            result += message.getDig()[i];
        }

        while (BitUtil.leftMostSetBit(result) > 16) {
            result = (result >> 16) + (result & 0xffff);
        }

        return new BigInt(result);
    }

    public static double getErrorDetectionRate(int iterations, double p, int messageBitSize) {
        BigInt message;
        BigInt encodedMessage;
        BigInt corruptedMessage;
        int nbMessageWithIntegrity = 0;
        int nbCorruptedMessageCorrectlyDetected = 0;

        for (int i = 0; i < iterations; i++) {
            message = SyntheticDataGenerator.getRandomWord(messageBitSize);
            encodedMessage = new BigInt(message);
            encode(encodedMessage);
            corruptedMessage = new BigInt(encodedMessage);
            SyntheticDataGenerator.corruptWord(corruptedMessage, p);

            if (encodedMessage.equals(corruptedMessage)) {
                nbMessageWithIntegrity++;
            } else {
                if (InternetChecksum.isCorrupted(corruptedMessage)) {
                    nbCorruptedMessageCorrectlyDetected++;
                }
            }
        }
        if (iterations - nbMessageWithIntegrity == 0) {
            return 1.d;
        }
        return (double) nbCorruptedMessageCorrectlyDetected / (iterations - nbMessageWithIntegrity);
    }
}
