package command;

import model.ProgramParameter;

public class EncodeCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        programParameter.getCode().encode(programParameter.getMessage(), programParameter.getMessageBitSize());
        System.out.println(programParameter.getMessage().toBinaryString(programParameter.getCode().getN()));
    }
}
