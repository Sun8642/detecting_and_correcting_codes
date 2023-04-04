package command;

import model.CommandLineParameter;

public class CorruptMessageCommand implements Command {

    @Override
    public void execute(CommandLineParameter commandLineParameter) throws IllegalArgumentException {
        commandLineParameter.getErrorChannelModelImpl().corrupt(commandLineParameter.getMessage(), commandLineParameter.getMessageBitSize(), commandLineParameter.getP());
        System.out.println(commandLineParameter.getMessage().toBinaryString(commandLineParameter.getMessageBitSize()));
    }
}
