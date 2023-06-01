package service.code;

import math.BigInt;
import service.channel.error.ErrorChannelModel;

/**
 * Product of a transformation that converts the representation of one information into another,
 * in order to be able to correct or detect possible errors.
 */
public interface Code {

    /**
     * Encodes a given message in order to be able to detect/correct errors that may occur as a result of message corruption.
     *
     * @param message the message to be encoded
     * @param k       the length of the message before it is encoded
     */
    void encode(BigInt message, int k);

    /**
     * Decodes a message if it has not been corrupted or if the error could be corrected.
     *
     * @param message the message to be decoded
     * @param n       the length of the coded message
     */
    void decode(BigInt message, int n);

    /**
     * @return the length of the coded message
     */
    int getN();

    /**
     * @return the length of the decoded message
     */
    int getK();

    /**
     * Allows to obtain the rate of detection and correction of errors in a corrupted message.
     *
     * @param iterations        the number of times a message will be encoded, corrupted and then decoded
     * @param p                 the probability of a bit being corrupted
     * @param messageBitSize    the size of a decoded message (getK())
     * @param errorChannelModel the error model to use to corrupt a message
     * @return an array of double in which, the first element represents the percentage of corrupted message whose error
     * was correctly detected, a second element if the code can correct the errors, this second element represents the
     * percentage of corrupted message whose error was correctly corrected.
     */
    double[] getErrorDetectionRate(int iterations, double p, int messageBitSize, ErrorChannelModel errorChannelModel);

    /**
     * Not every code can correct errors, this method allows us to know if a given code can correct errors or not.
     *
     * @return true if the code can correct errors
     */
    default boolean canCorrectError() {
        return false;
    }
}
