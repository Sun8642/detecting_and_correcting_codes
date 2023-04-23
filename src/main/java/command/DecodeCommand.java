package command;

import command.line.CommandLineParameter;

public class DecodeCommand implements Command {

    @Override
    public void execute(CommandLineParameter commandLineParameter) throws IllegalArgumentException {
        commandLineParameter.getCode().decode(commandLineParameter.getMessage(), commandLineParameter.getMessageBitSize());
        System.out.println(commandLineParameter.getMessage().toBinaryString(commandLineParameter.getCode().getK()));
    }
}
