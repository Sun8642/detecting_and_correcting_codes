package command;

import model.ProgramParameter;
import util.SyntheticDataGenerator;

public class GenerateMessageCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        System.out.println(SyntheticDataGenerator.getRandomWordToString(programParameter.getMessageBitSize()));
    }
}
