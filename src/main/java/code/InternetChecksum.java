package code;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.SyntheticDataGenerator;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InternetChecksum {

    public static final String REGEX_STRING_16_CHARS = "(?<=\\G.{16})";
    private static final BigInteger MASK_16_BITS = BigInteger.valueOf(0xffff);

    public static String encode(String message) {
        return message + getChecksum(message);
    }

    public static BigInteger encode(BigInteger message) {
        return message.shiftLeft(16).add(getChecksum(message));
    }

    public static String decode(String encodedMessage) {
        if (isCorrupted(encodedMessage)) {
            throw new RuntimeException("Could not decode message, the encoded message is corrupted");
        }
        return encodedMessage.substring(0, encodedMessage.length() - 16);
    }

    public static String getChecksum(String message) {
        return Integer.toBinaryString(getChecksumInt(message));
    }

    public static BigInteger getChecksum(BigInteger message) {
        return getSumOfWords(message).not().and(MASK_16_BITS);
    }

    private static int getChecksumInt(String message) {
        return (~getSumOfWords(message)) & 0xffff;
    }

    public static boolean isCorrupted(String encodedMessage) {
        return getSumOfWords(encodedMessage.substring(0, encodedMessage.length() - 16)) + Integer.parseInt(encodedMessage.substring(encodedMessage.length() - 16), 2) != 0xffff;
    }

    private static int getSumOfWords(String message) {
        //TODO: works only if message.length() % 16 == 0
        String[] words = message.split(REGEX_STRING_16_CHARS);
        int checksum = Integer.parseInt(words[0], 2);
        for (int i = 1; i < words.length; i++) {
            checksum += Integer.parseInt(words[i], 2);
        }

        return (checksum & 0xffff) + (checksum >> 16);
    }

    private static BigInteger getSumOfWords(BigInteger message) {
        int length = ((int) Math.ceil((float) message.bitLength() / 16)) * 16;
        BigInteger result = message;
        while (length > 16) {
            length -= 16;
            result = result.shiftRight(16).add(result.mod(BigInteger.ONE.shiftLeft(16)));
        }

        return result.and(MASK_16_BITS).add(result.shiftRight(16));
    }

    public static double getProbabilityOfSuccess(int iterations, double p, int messageBitSize) {
        String message = SyntheticDataGenerator.getRandomWord(messageBitSize);
        String encodedMessage = InternetChecksum.encode(message);
        int nbMessageWithIntegrity = 0;
        int nbCorruptedMessageCorrectlyDetected = 0;

        String corruptedMessage;
        for (int i = 0; i < iterations; i++) {
            corruptedMessage = SyntheticDataGenerator.corruptWord(encodedMessage, p);
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
