package command;

import command.line.CommandLineParameter;
import service.SyntheticDataGenerator;

public class GenerateMessageCommand implements Command {

    @Override
    public void execute(CommandLineParameter commandLineParameter) throws IllegalArgumentException {
        System.out.println(SyntheticDataGenerator.getRandomWordToString(commandLineParameter.getMessageBitSize()));
    }
}
