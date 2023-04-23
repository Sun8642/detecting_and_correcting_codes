package service.code;

import service.channel.error.ErrorChannelModel;
import lombok.Getter;
import lombok.Setter;
import math.BigInt;
import util.ProbabilityError;
import util.SyntheticDataGenerator;

@Getter
@Setter
public class ParityBitCode implements Code {

    private int k;
    private int n;

    public static double getProbabilityOfDetectingError(int N, double p) {
        double probabilityOfDetectingError = 0.d;
        for (int k = 2; k <= N; k += 2) {
            probabilityOfDetectingError += ProbabilityError.getProbabilityOfKErrors(N, p, k);
        }
        return 1 - probabilityOfDetectingError;
    }

    public void encode(BigInt message, int k) {
        this.k = k;
        n = k + 1;
        message.shiftLeft(1);
        if (message.getBitCount() % 2 != 0) {
            message.add(1);
        }
    }

    public void decode(BigInt encodedMessage, int n) {
        if (isCorrupted(encodedMessage)) {
            throw new RuntimeException("Could not decode message, the encoded message is corrupted");
        }
        this.n = n;
        k = n - 1;
        encodedMessage.shiftRight(1);
    }

    public boolean isCorrupted(BigInt message) {
        return message.getBitCount() % 2 != 0;
    }

    public double[] getErrorDetectionRate(int iterations, double p, int messageBitSize, ErrorChannelModel errorChannelModel) {
        BigInt message;
        BigInt encodedMessage;
        BigInt corruptedMessage;
        int nbMessageWithIntegrity = 0;
        int nbCorruptedMessageCorrectlyDetected = 0;

        for (int i = 0; i < iterations; i++) {
            message = SyntheticDataGenerator.getRandomWord(messageBitSize);
            encodedMessage = new BigInt(message);
            encode(encodedMessage, messageBitSize);
            corruptedMessage = new BigInt(encodedMessage);
            errorChannelModel.corrupt(corruptedMessage, n, p);

            if (encodedMessage.equals(corruptedMessage)) {
                nbMessageWithIntegrity++;
            } else {
                if (isCorrupted(corruptedMessage)) {
                    nbCorruptedMessageCorrectlyDetected++;
                }
            }
        }
        if (iterations - nbMessageWithIntegrity == 0) {
            return new double[]{1.d};
        }
        return new double[]{(double) nbCorruptedMessageCorrectlyDetected / (iterations - nbMessageWithIntegrity)};
    }
}
