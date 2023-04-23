package service.code;

import service.channel.error.ErrorChannelModel;
import lombok.Getter;
import lombok.Setter;
import math.BigInt;
import util.BitUtil;
import util.SyntheticDataGenerator;

@Getter
@Setter
public final class InternetChecksum implements Code {

    private static final BigInt MASK_16_BITS_BIG_INT = new BigInt(0xffff);

    private int k;
    private int n;

    public void encode(BigInt message, int k) {
        setK(k);
        n = k + 16;
        BigInt checksum = getChecksum(message);
        message.shiftLeft(16);
        message.add(checksum);
    }

    public void setK(int k) {
        if (k % 16 != 0) {
            throw new IllegalArgumentException("The number of bits per message must be a multiple of 16.");
        }
        this.k = k;
    }

    public void decode(BigInt encodedMessage, int n) {
        if (isCorrupted(encodedMessage)) {
            throw new RuntimeException("Could not decode message, the encoded message is corrupted");
        }
        this.n = n;
        k = n - 16;
        encodedMessage.shiftRight(16);
    }

    public BigInt getChecksum(BigInt message) {
        BigInt sumOfWords = getSumOfWords(message);
        sumOfWords.not();
        sumOfWords.and(MASK_16_BITS_BIG_INT);
        return sumOfWords;
    }

    public boolean isCorrupted(BigInt encodedMessage) {
        BigInt decodedMessage = new BigInt(encodedMessage);
        decodedMessage.shiftRight(16);

        BigInt encodedMessageChecksum = new BigInt(MASK_16_BITS_BIG_INT);
        encodedMessageChecksum.and(encodedMessage);

        return !getChecksum(decodedMessage).equals(encodedMessageChecksum);
    }

    private BigInt getSumOfWords(BigInt message) {
        long result = 0;
        for (int i = 0; i < message.getDig().length; i++) {
            result += message.getDig()[i];
        }

        while (BitUtil.leftMostSetBit(result) > 16) {
            result = (result >> 16) + (result & 0xffff);
        }

        return new BigInt(result);
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
