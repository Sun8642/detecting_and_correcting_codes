import command.Command;
import command.CorruptMessageCommand;
import command.DecodeCommand;
import command.EncodeCommand;
import command.GenerateGraphCommand;
import command.GenerateMessageCommand;
import enums.MainCommand;
import model.CommandLineOption;
import model.CommandLineParameter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final String APPLICATION_NAME = "myApp";

    public static void main(String[] args) {
        Options options = CommandLineOption.MAIN_OPTIONS;
        CommandLineParser parser = new DefaultParser();
        try {
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException("No command given in parameter");
            }
            Command command;
            MainCommand mainCommand = CommandLineParameter.parseMainCommand(args[0]);
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

            commandLineParameter.setParameters(parser.parse(options, args));
            command.execute(commandLineParameter);
        } catch (ParseException | IllegalArgumentException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(APPLICATION_NAME, options, true);
        }
    }
}
