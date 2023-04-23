package service.channel.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import math.BigInt;

@Getter
@Setter
@AllArgsConstructor
public class BurstErrorChannelModel implements ErrorChannelModel {

    private int burstLength;

    @Override
    public void corrupt(BigInt message, int messageBitSize, double errorRate) {
        for (int i = messageBitSize - 1; i >= 0; i--) {
            if (Math.random() <= errorRate) {
                message.flipBit(i);
                if (i != 0) {
                    for (int j = i - 1; j >= Math.max(1, i - (burstLength - 1)); j--) {
                        if (Math.random() <= errorRate) {
                            message.flipBit(j);
                        }
                    }
                    i -= (burstLength - 1);
                    message.flipBit(Math.max(i, 0));
                }
            }
        }
    }
}
