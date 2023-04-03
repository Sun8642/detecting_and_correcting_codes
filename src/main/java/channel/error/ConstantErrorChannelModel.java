package channel.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import math.BigInt;

@Getter
@Setter
@AllArgsConstructor
public class ConstantErrorChannelModel implements ErrorChannelModel {

    private double errorRate;

    @Override
    public void corrupt(BigInt message, int messageBitSize) {
        for (int i = messageBitSize - 1; i >= 0; i--) {
            if (Math.random() <= errorRate) {
                message.flipBit(i);
            }
        }
    }
}
