package command;

import model.CommandLineParameter;
import util.SyntheticDataGenerator;

public class GenerateMessageCommand implements Command {

    @Override
    public void execute(CommandLineParameter commandLineParameter) throws IllegalArgumentException {
        System.out.println(SyntheticDataGenerator.getRandomWordToString(commandLineParameter.getMessageBitSize()));
    }
}
