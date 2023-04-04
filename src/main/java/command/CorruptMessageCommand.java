package command;

import model.ProgramParameter;

public class CorruptMessageCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        programParameter.getErrorChannelModelImpl().corrupt(programParameter.getMessage(), programParameter.getMessageBitSize(), programParameter.getP());
        System.out.println(programParameter.getMessage().toBinaryString(programParameter.getMessageBitSize()));
    }
}
