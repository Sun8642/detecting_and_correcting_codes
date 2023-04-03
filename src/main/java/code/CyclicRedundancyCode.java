package code;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import math.BigInt;
import util.SyntheticDataGenerator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyclicRedundancyCode {

    public static void encode(BigInt message, BigInt generatorPolynomial) {
        message.shiftLeft(generatorPolynomial.getLeftMostSetBit() - 1);
        message.add(getPolynomialArithmeticModulo2(message, generatorPolynomial));
    }

    public static BigInt getPolynomialArithmeticModulo2(BigInt dividend, BigInt divisor) {
        BigInt remainder = new BigInt(dividend);
        divisor = new BigInt(divisor);
        int remainderLeftMostSetBit = remainder.getLeftMostSetBit();
        int newRemainderLeftMostSetBit;
        int divisorLeftMostSetBit = divisor.getLeftMostSetBit();

        if (remainderLeftMostSetBit > divisorLeftMostSetBit) {
            divisor.shiftLeft(remainderLeftMostSetBit - divisorLeftMostSetBit);
        }

        while (remainderLeftMostSetBit >= divisorLeftMostSetBit) {
//            remainder.xor(divisor);
            remainder.xor2(divisor);
            newRemainderLeftMostSetBit = remainder.getLeftMostSetBit();
            divisor.shiftRight(remainderLeftMostSetBit - newRemainderLeftMostSetBit);
            remainderLeftMostSetBit -= (remainderLeftMostSetBit - newRemainderLeftMostSetBit);
        }
        return remainder;
    }

    public static boolean isCorrupted(BigInt encodedMessage, BigInt generatorPolynomial) {
        return !getPolynomialArithmeticModulo2(encodedMessage, generatorPolynomial).isZero();
    }

    public static void decode(BigInt encodedMessage, BigInt generatorPolynomial) {
        if (isCorrupted(encodedMessage, generatorPolynomial)) {
            throw new RuntimeException("Could not decode message, the encoded message is corrupted");
        }
        encodedMessage.shiftRight(generatorPolynomial.getLeftMostSetBit() - 1);
    }

    public static double getErrorDetectionRate(int iterations, double p, int messageBitSize, BigInt generatorPolynomial) {
        BigInt message;
        BigInt encodedMessage;
        BigInt corruptedMessage;
        int nbMessageWithIntegrity = 0;
        int nbCorruptedMessageCorrectlyDetected = 0;

        for (int i = 0; i < iterations; i++) {
            message = SyntheticDataGenerator.getRandomWord(messageBitSize);
            encodedMessage = new BigInt(message);
            encode(encodedMessage, generatorPolynomial);
            corruptedMessage = new BigInt(encodedMessage);
            SyntheticDataGenerator.corruptWord(corruptedMessage, p);

            if (encodedMessage.equals(corruptedMessage)) {
                nbMessageWithIntegrity++;
            } else {
                if (isCorrupted(corruptedMessage, generatorPolynomial)) {
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
