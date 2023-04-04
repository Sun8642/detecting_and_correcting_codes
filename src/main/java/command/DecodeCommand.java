package command;

import model.ProgramParameter;

public class DecodeCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        programParameter.getCode().decode(programParameter.getMessage(), programParameter.getMessageBitSize());
        System.out.println(programParameter.getMessage().toBinaryString(programParameter.getCode().getK()));
    }
}
