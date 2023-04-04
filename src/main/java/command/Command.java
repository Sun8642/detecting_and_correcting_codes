package command;

import model.CommandLineParameter;

public interface Command {

    void execute(CommandLineParameter commandLineParameter) throws IllegalArgumentException;
}
