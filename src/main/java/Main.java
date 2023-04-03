import command.Command;
import command.CorruptMessageCommand;
import command.DecodeCommand;
import command.EncodeCommand;
import command.GenerateGraphCommand;
import command.GenerateMessageCommand;
import enums.MainCommand;
import model.ProgramOption;
import model.ProgramParameter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final String APPLICATION_NAME = "myApp";

    public static void main(String[] args) {
        Options options = ProgramOption.MAIN_OPTIONS;
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);

            Command command;
            MainCommand mainCommand = ProgramParameter.parseMainCommand(line.getOptionValue(ProgramOption.MAIN_COMMAND));
            ProgramParameter programParameter = new ProgramParameter();

            switch (mainCommand) {
                case ENCODE -> {
                    options = ProgramOption.ENCODE_DECODE_OPTIONS;
                    programParameter.setParameters(parser.parse(options, args));
                    command = new EncodeCommand();
                }
                case DECODE -> {
                    options = ProgramOption.ENCODE_DECODE_OPTIONS;
                    programParameter.setParameters(parser.parse(options, args));
                    command = new DecodeCommand();
                }
                case GENERATE_MESSAGE -> {
                    options = ProgramOption.GENERATE_MESSAGE_OPTIONS;
                    programParameter.setParameters(parser.parse(options, args));
                    command = new GenerateMessageCommand();
                }
                case CORRUPT_MESSAGE -> {
                    options = ProgramOption.CORRUPT_MESSAGE_OPTIONS;
                    programParameter.setParameters(parser.parse(options, args));
                    command = new CorruptMessageCommand();
                }
                case GENERATE_GRAPH -> {
                    options = ProgramOption.GENERATE_GRAPH_OPTIONS;
                    programParameter.setParameters(parser.parse(options, args));
                    command = new GenerateGraphCommand();
                }
                default ->
                        throw new IllegalArgumentException("Missing implementation in main for command: " + mainCommand.getArgumentName());
            }

            command.execute(programParameter);
        } catch (ParseException | IllegalArgumentException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(APPLICATION_NAME, options, true);
        }
    }
}
