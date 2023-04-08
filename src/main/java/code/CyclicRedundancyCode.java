package code;

import channel.error.ErrorChannelModel;
import lombok.Getter;
import lombok.Setter;
import math.BigInt;
import util.SyntheticDataGenerator;

@Getter
@Setter
public final class CyclicRedundancyCode implements Code {

    private int k;
    private int n;
    private BigInt generatorPolynomial;

    public CyclicRedundancyCode(BigInt generatorPolynomial) {
        setGeneratorPolynomial(generatorPolynomial);
    }

    public void encode(BigInt message, int k) {
        int shiftValue = generatorPolynomial.getLeftMostSetBit() - 1;
        this.k = k;
        n = k + shiftValue;
        message.shiftLeft(shiftValue);
        message.add(getPolynomialArithmeticModulo2(message, generatorPolynomial));
    }

    public static BigInt getPolynomialArithmeticModulo2(BigInt dividend, BigInt divisor) {
        BigInt remainder = new BigInt(dividend);
        int remainderLeftMostSetBit = remainder.getLeftMostSetBit();
        int divisorLeftMostSetBit = divisor.getLeftMostSetBit();

        int i;
        while (remainderLeftMostSetBit >= divisorLeftMostSetBit) {
            i = 0;
            while (i < divisorLeftMostSetBit) {
                if (divisor.testBit(divisorLeftMostSetBit - 1 - i)) {
                    remainder.flipBit(remainderLeftMostSetBit - 1 - i);
                }
                i++;
            }
            remainderLeftMostSetBit = remainder.getLeftMostSetBit();
        }
        return remainder;
    }

    public boolean isCorrupted(BigInt encodedMessage) {
        return !getPolynomialArithmeticModulo2(encodedMessage, generatorPolynomial).isZero();
    }

    public void decode(BigInt encodedMessage, int n) {
        if (isCorrupted(encodedMessage)) {
            throw new RuntimeException("Could not decode message, the encoded message is corrupted");
        }
        int shiftValue = generatorPolynomial.getLeftMostSetBit() - 1;
        this.n = n;
        k = n - shiftValue;
        encodedMessage.shiftRight(shiftValue);
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
