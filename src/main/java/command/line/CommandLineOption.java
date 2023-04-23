package command.line;

import command.enums.DetectingCode;
import command.enums.ErrorChannelModel;
import command.enums.MainCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommandLineOption {

    public static final Option MAIN_COMMAND = Option.builder("")
            .desc("The command to use. Valid commands are: " + MainCommand.getArgumentNamesForConsole())
            .required(true)
            .hasArg()
            .build();
    public static final Option CODE = Option.builder("C")
            .longOpt("code")
            .desc("The code to use to encode and decode messages. Valid codes are: " + DetectingCode.getArgumentNamesForConsole())
            .required(true)
            .hasArg()
            .build();
    public static final Option ERROR_CHANNEL_MODEL = Option.builder("E")
            .longOpt("errorModel")
            .desc("Specify the error model of the transmission channel. Default model is " +
                    ErrorChannelModel.CONSTANT_ERROR_CHANNEL_MODEL.getArgumentName() + ". Valid models are: " +
                    ErrorChannelModel.getArgumentNamesForConsole())
            .hasArg()
            .build();
    public static final Option BURST_LENGTH = Option.builder("BL")
            .longOpt("burstLength")
            .desc("Specify the length of the burst (only if burstError option was specified, default 3)")
            .hasArg()
            .build();
    public static final Option MESSAGE_BIT_SIZE = Option.builder("MBS")
            .longOpt("messageBitSize")
            .desc("Specify the length of a message to be coded (default 8 for parity bit code and CRC, 16 for internet checksum and 4 for hamming)")
            .hasArg()
            .build();
    public static final Option MIN_P = Option.builder("MinP")
            .desc("The minimum probability of a bit to be corrupted (default 0.01)")
            .hasArg()
            .build();
    public static final Option MAX_P = Option.builder("MaxP")
            .desc("The maximum probability of a bit to be corrupted (default 0.5)")
            .hasArg()
            .build();
    public static final Option P = Option.builder("P")
            .desc("The probability of a bit to be corrupted (default 0.1)")
            .hasArg()
            .build();
    public static final Option ITERATIONS_PER_P = Option.builder("I")
            .desc("The number of iterations per probability (default 10000)")
            .hasArg()
            .build();
    public static final Option NB_STEP_PER_P = Option.builder("S")
            .desc("The number of step between the minimum and the maximum probability (default 50)")
            .hasArg()
            .build();
    public static final Option GENERATOR_POLYNOMIAL = Option.builder("GP")
            .longOpt("generatorPolynomial")
            .desc("The generator polynomial to use to encode messages (only if CRC code is chosen, default 1011)")
            .hasArg()
            .build();
    public static final Option MESSAGE = Option.builder("M")
            .longOpt("message")
            .desc("The message to be encoded/decoded")
            .required(true)
            .hasArg()
            .build();
    public static final Option HELP = Option.builder("H")
            .longOpt("help")
            .desc("Display the usage for the given functionality")
            .required(false)
            .build();

    public static final Options MAIN_OPTIONS = new Options();

    static {
        MAIN_OPTIONS.addOption(MAIN_COMMAND);
    }

    public static final Options ENCODE_DECODE_OPTIONS = new Options();

    static {
        ENCODE_DECODE_OPTIONS.addOption(CODE);
        ENCODE_DECODE_OPTIONS.addOption(MESSAGE);
        ENCODE_DECODE_OPTIONS.addOption(GENERATOR_POLYNOMIAL);
        ENCODE_DECODE_OPTIONS.addOption(HELP);
    }

    public static final Options GENERATE_GRAPH_OPTIONS = new Options();

    static {
        GENERATE_GRAPH_OPTIONS.addOption(CODE);
        GENERATE_GRAPH_OPTIONS.addOption(ERROR_CHANNEL_MODEL);
        GENERATE_GRAPH_OPTIONS.addOption(BURST_LENGTH);
        GENERATE_GRAPH_OPTIONS.addOption(MESSAGE_BIT_SIZE);
        GENERATE_GRAPH_OPTIONS.addOption(MIN_P);
        GENERATE_GRAPH_OPTIONS.addOption(MAX_P);
        GENERATE_GRAPH_OPTIONS.addOption(ITERATIONS_PER_P);
        GENERATE_GRAPH_OPTIONS.addOption(NB_STEP_PER_P);
        GENERATE_GRAPH_OPTIONS.addOption(GENERATOR_POLYNOMIAL);
        GENERATE_GRAPH_OPTIONS.addOption(HELP);
    }

    public static final Options GENERATE_MESSAGE_OPTIONS = new Options();

    static {
        GENERATE_MESSAGE_OPTIONS.addOption(MESSAGE_BIT_SIZE);
        GENERATE_MESSAGE_OPTIONS.addOption(HELP);
    }

    public static final Options CORRUPT_MESSAGE_OPTIONS = new Options();

    static {
        CORRUPT_MESSAGE_OPTIONS.addOption(P);
        CORRUPT_MESSAGE_OPTIONS.addOption(ERROR_CHANNEL_MODEL);
        CORRUPT_MESSAGE_OPTIONS.addOption(BURST_LENGTH);
        CORRUPT_MESSAGE_OPTIONS.addOption(MESSAGE);
        CORRUPT_MESSAGE_OPTIONS.addOption(HELP);
    }
}
