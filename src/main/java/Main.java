import command.Command;
import command.CorruptMessageCommand;
import command.DecodeCommand;
import command.EncodeCommand;
import command.GenerateGraphCommand;
import command.GenerateMessageCommand;
import command.enums.MainCommand;
import command.line.CommandLineOption;
import command.line.CommandLineParameter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final String APPLICATION_NAME = "cd-codes";

    public static void main(String[] args) {
        MainCommand mainCommand = null;
        Options options = CommandLineOption.MAIN_OPTIONS;
        CommandLineParser parser = new DefaultParser();
        try {
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException("No command given in parameter");
            }
            Command command;
            mainCommand = CommandLineParameter.parseMainCommand(args[0]);
            CommandLineParameter commandLineParameter = new CommandLineParameter();

            switch (mainCommand) {
                case ENCODE -> {
                    options = CommandLineOption.ENCODE_DECODE_OPTIONS;
                    command = new EncodeCommand();
                }
                case DECODE -> {
                    options = CommandLineOption.ENCODE_DECODE_OPTIONS;
                    command = new DecodeCommand();
                }
                case GENERATE_MESSAGE -> {
                    options = CommandLineOption.GENERATE_MESSAGE_OPTIONS;
                    command = new GenerateMessageCommand();
                }
                case CORRUPT_MESSAGE -> {
                    options = CommandLineOption.CORRUPT_MESSAGE_OPTIONS;
                    command = new CorruptMessageCommand();
                }
                case GENERATE_GRAPH -> {
                    options = CommandLineOption.GENERATE_GRAPH_OPTIONS;
                    command = new GenerateGraphCommand();
                }
                default ->
                        throw new IllegalArgumentException("Missing implementation in main for command: " + mainCommand.getArgumentName());
            }

            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption(CommandLineOption.HELP)) {
                new HelpFormatter().printHelp(APPLICATION_NAME + " " + mainCommand.getArgumentName(), options, true);
                return;
            }

            commandLineParameter.setParameters(commandLine);
            command.execute(commandLineParameter);
        } catch (ParseException | IllegalArgumentException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(APPLICATION_NAME + (mainCommand != null ? " " + mainCommand.getArgumentName() : ""), options, true);
        }
    }
}
