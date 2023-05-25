package command;

import command.line.CommandLineParameter;

public interface Command {

    void execute(CommandLineParameter commandLineParameter) throws IllegalArgumentException;
}
