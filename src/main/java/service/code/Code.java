package service.code;

import service.channel.error.ErrorChannelModel;
import math.BigInt;

public interface Code {

    void encode(BigInt message, int k);

    void decode(BigInt message, int n);

    int getN();

    int getK();

    double[] getErrorDetectionRate(int iterations, double p, int messageBitSize, ErrorChannelModel errorChannelModel);

    default boolean canCorrectError() {
        return false;
    }
}
