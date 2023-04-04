package command;

import model.CommandLineParameter;

public class EncodeCommand implements Command {

    @Override
    public void execute(CommandLineParameter commandLineParameter) throws IllegalArgumentException {
        commandLineParameter.getCode().encode(commandLineParameter.getMessage(), commandLineParameter.getMessageBitSize());
        System.out.println(commandLineParameter.getMessage().toBinaryString(commandLineParameter.getCode().getN()));
    }
}
