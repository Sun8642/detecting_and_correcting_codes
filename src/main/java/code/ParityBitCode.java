package code;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import math.BigInt;
import util.ProbabilityError;
import util.SyntheticDataGenerator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParityBitCode {

    public static double getProbabilityOfDetectingError(int N, double p) {
        double probabilityOfDetectingError = 0.d;
        for (int k = 2; k <= N; k += 2) {
            probabilityOfDetectingError += ProbabilityError.getProbabilityOfKErrors(N, p, k);
        }
        return 1 - probabilityOfDetectingError;
    }

    public static void encode(BigInt message) {
        message.shiftLeft(1);
        if (message.getBitCount() % 2 != 0) {
            message.add(1);
        }
    }

    public static void decode(BigInt encodedMessage) {
        if (isCorrupted(encodedMessage)) {
            throw new RuntimeException("Could not decode message, the encoded message is corrupted");
        }
        encodedMessage.shiftRight(1);
    }

    public static boolean isCorrupted(BigInt message) {
        return message.getBitCount() % 2 != 0;
    }

    public static double getErrorDetectionRate(int iterations, double p, int messageBitSize) {
        BigInt encodedMessage = SyntheticDataGenerator.getRandomWord(messageBitSize);
        encode(encodedMessage);
        int nbMessageWithIntegrity = 0;
        int nbCorruptedMessageCorrectlyDetected = 0;

        BigInt corruptedMessage = new BigInt(encodedMessage);
        for (int i = 0; i < iterations; i++) {
            SyntheticDataGenerator.corruptWord(corruptedMessage, p);
            if (encodedMessage.equals(corruptedMessage)) {
                nbMessageWithIntegrity++;
            } else {
                if (isCorrupted(corruptedMessage)) {
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
