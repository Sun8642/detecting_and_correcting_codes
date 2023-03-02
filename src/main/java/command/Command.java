package command;

import model.ProgramParameter;

public interface Command {

    void execute(ProgramParameter programParameter) throws IllegalArgumentException;
}
