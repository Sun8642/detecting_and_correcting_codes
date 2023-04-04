package code;

import channel.error.ErrorChannelModel;
import lombok.Getter;
import lombok.Setter;
import math.BigInt;
import util.BitUtil;
import util.SyntheticDataGenerator;

@Getter
@Setter
public final class HammingCode implements Code {

    private int k;
    private int n;
    private boolean parity = true;
    private boolean isErrorDetected;

    @Override
    public boolean canCorrectError() {
        return true;
    }

    public void encode(BigInt message, int k) {
        setK(k);
        int numberOfRedundancyBitsToAdd = numberOfRedundancyBitsToAdd(k);
        n = k + numberOfRedundancyBitsToAdd;

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

    public void setK(int k) {
        if (!HammingCode.isKValid(k)) {
            throw new IllegalArgumentException("The number of bits per message plus the number of redundancy bits for " +
                    "this number minus one must be equals to a power of 2 (e.g. code(7, 4), code(15,11), ...).");
        }
        this.k = k;
    }

    /**
     * Decode a message which was coded with the hamming code
     *
     * @param encodedMessage the encoded message which need to be decoded
     */
    public void decode(BigInt encodedMessage, int n) {
        this.n = n;
        int leftMostSetBit = encodedMessage.getLeftMostSetBit();
        int numberOfRedundancyBitsAdded = numberOfRedundancyBitsAdded(n);
        setK(n - numberOfRedundancyBitsAdded);

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

        isErrorDetected = errorBitPosition != 0;
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

    /**
     * This method wil return the number of redundancy bits added in given the parameter n.
     * This method will only work for perfect hamming code.
     *
     * @param encodedMessageLength the length of an encoded message
     * @return the number of redundancy bits added in an encoded message
     */
    public static int numberOfRedundancyBitsAdded(int encodedMessageLength) {
//        return (int) Math.sqrt(((double) encodedMessageLength + 1));
//        return (int) Math.lo(((double) encodedMessageLength + 1));
        return BitUtil.binLog(encodedMessageLength + 1);
    }

    public static boolean isKValid(int k) {
        return k >= 4 && BitUtil.isPowerOfTwo(k + numberOfRedundancyBitsToAdd(k) + 1);
    }

    public double[] getErrorDetectionRate(int iterations, double p, int messageBitSize, ErrorChannelModel errorChannelModel) {
        BigInt message;
        BigInt encodedMessage;
        BigInt corruptedMessage;
        int nbMessageWithIntegrity = 0;
        int nbCorruptedMessageCorrectlyDetected = 0;
        int nbCorruptedMessageCorrectlyCorrected = 0;

        for (int i = 0; i < iterations; i++) {
            message = SyntheticDataGenerator.getRandomWord(messageBitSize);
            encodedMessage = new BigInt(message);
            encode(encodedMessage, messageBitSize);
            corruptedMessage = new BigInt(encodedMessage);
            errorChannelModel.corrupt(corruptedMessage, n, p);

            if (encodedMessage.equals(corruptedMessage)) {
                nbMessageWithIntegrity++;
            } else {
                decode(corruptedMessage, n);
                if (isErrorDetected) {
                    nbCorruptedMessageCorrectlyDetected++;
                }
                if (corruptedMessage.toString().equals(message.toString())) {
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
