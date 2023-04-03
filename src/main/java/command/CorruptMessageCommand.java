package command;

import channel.error.BurstErrorChannelModel;
import channel.error.ConstantErrorChannelModel;
import channel.error.ErrorChannelModel;
import model.ProgramParameter;

public class CorruptMessageCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        ErrorChannelModel errorChannelModel;
        switch (programParameter.getErrorChannelModel()) {
            case CONSTANT_ERROR_CHANNEL_MODEL ->
                    errorChannelModel = new ConstantErrorChannelModel(programParameter.getP());
            case BURST_ERROR_CHANNEL_MODEL ->
                    errorChannelModel = new BurstErrorChannelModel(programParameter.getP(), programParameter.getBurstErrorLength());
            default ->
                    throw new IllegalArgumentException("Missing implementation for error channel model: " + programParameter.getErrorChannelModel().getArgumentName());
        }
        errorChannelModel.corrupt(programParameter.getMessage(), programParameter.getMessageBitSize());
        System.out.println(programParameter.getMessage().toBinaryString(programParameter.getMessageBitSize()));
    }
}
