package model;

import math.BigInt;

/**
 * An interface for error channel models, which represent the errors that may occur during transmission of a message
 * over a communication channel.
 */
public interface ErrorChannelModel {

    /**
     * Corrupts a message by introducing errors according to the specific error model.
     *
     * @param message the message to be corrupted
     */
    void corrupt(BigInt message, int messageBitSize);
}
