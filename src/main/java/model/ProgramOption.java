package model;

import enums.DetectingCode;
import enums.ErrorChannelModel;
import lombok.Getter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ProgramOption {

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

    @Getter
    private final Options encodeDecodeOptions = new Options();
    @Getter
    private final Options graphOptions = new Options();
    @Getter
    private final Options generateMessageOptions = new Options();
    @Getter
    private final Options corruptMessageOptions = new Options();

    public ProgramOption() {
        encodeDecodeOptions.addOption(CODE);
        encodeDecodeOptions.addOption(MESSAGE);
        encodeDecodeOptions.addOption(GENERATOR_POLYNOMIAL);

        graphOptions.addOption(CODE);
        graphOptions.addOption(ERROR_CHANNEL_MODEL);
        graphOptions.addOption(BURST_LENGTH);
        graphOptions.addOption(MESSAGE_BIT_SIZE);
        graphOptions.addOption(MIN_P);
        graphOptions.addOption(MAX_P);
        graphOptions.addOption(ITERATIONS_PER_P);
        graphOptions.addOption(NB_STEP_PER_P);
        graphOptions.addOption(GENERATOR_POLYNOMIAL);

        generateMessageOptions.addOption(MESSAGE_BIT_SIZE);

        corruptMessageOptions.addOption(P);
        //TODO: error type
    }
}
