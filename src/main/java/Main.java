import command.EncodeCommand;
import enums.DetectingCode;
import java.text.Normalizer;
import java.util.Scanner;
import model.CodeParameter;
import model.CyclicRedundancyCodeParameter;
import model.HammingCodeParameter;
import model.InternetChecksumParameter;
import model.ProgramOption;
import model.ProgramParameter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import test.Test2;

public class Main {

    public static void main(String[] args) {
//        Test2.test(new CodeParameter(), DetectingCode.PARITY_BIT_CODE);
//        Test2.test(new CyclicRedundancyCodeParameter(), DetectingCode.CYCLIC_REDUNDANCY_CODE);
//        Test2.test(new InternetChecksumParameter(), DetectingCode.INTERNET_CHECKSUM);
//        Test2.test(new HammingCodeParameter(), DetectingCode.HAMMING_CODE);

        askCommand();
//        if (true)
//            return;
//
//        CommandLineParser parser = new DefaultParser();
//        try {
//            CommandLine line = parser.parse(options, args);
//            DetectingCode detectingCode = parseCode((String) line.getParsedOptionValue(code));
//            CodeParameter codeParameter = getCodeParameter(detectingCode);
//
//            if (line.hasOption(burstLength)) {
//                codeParameter.setBurstError(true);
//            }
//
//            if (line.hasOption(iterationsPerP)) {
//                codeParameter.setNumberOfIterationsPerProbability(Integer.parseInt((String) line.getParsedOptionValue(iterationsPerP)));
//            }
//
//            Test2.test(codeParameter, detectingCode);
//        } catch (ParseException | IllegalArgumentException exp) {
//            // oops, something went wrong
//            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
//            HelpFormatter formatter = new HelpFormatter();
//            formatter.printHelp("myApp", options, true);
//        }
    }

    private static CodeParameter getCodeParameter(DetectingCode detectingCode) throws ParseException {
        CodeParameter codeParameter;
        switch (detectingCode) {
            case CYCLIC_REDUNDANCY_CODE -> codeParameter = new CyclicRedundancyCodeParameter();
            case INTERNET_CHECKSUM -> codeParameter = new InternetChecksumParameter();
            case HAMMING_CODE -> codeParameter = new HammingCodeParameter();
            default -> codeParameter = new CodeParameter();
        }
        return codeParameter;
    }

    private static void askCommand() {
        try (Scanner scanner = new Scanner(System.in)) {
            String mainCommand;
            String subCommand;
            String[] splitCommand;
            CommandLineParser parser = new DefaultParser();
            ProgramOption programOption = new ProgramOption();
            Options options;
            HelpFormatter formatter = new HelpFormatter();
            printUsage();
            do {
                System.out.println("Enter a command: ");
                mainCommand = scanner.nextLine();
                if ("exit".equals(mainCommand)) {
                    return;
                }
                switch (mainCommand) {
                    case "encode":
                    case "decode":
                        options = programOption.getEncodeDecodeOptions();
                        break;
                    case "generateMessage":
                        options = programOption.getGenerateMessageOptions();
                        break;
                    case "corruptMessage":
                        options = programOption.getCorruptMessageOptions();
                        break;
                    case "generateGraph":
                        options = programOption.getGraphOptions();
                        break;
                    default:
                        System.out.println("Couldn't recognize the command");
                        printUsage();
                        continue;
                }
                formatter.printHelp(mainCommand, options, true);
                subCommand = scanner.nextLine();
                splitCommand = subCommand.split(" ");
                try {
                    CommandLine line = parser.parse(options, splitCommand);
                    ProgramParameter programParameter = new ProgramParameter();
                    programParameter.setParameters(line);
                    switch (mainCommand) {
                        case "encode":
                            new EncodeCommand().execute(programParameter);
                            break;
                        case "decode":
                            break;
                        case "generateMessage":
                            break;
                        case "corruptMessage":
                            break;
                        case "errorDetectingRate":
                            break;
                        case "errorCorrectingRate":
                            break;
                        case "generateGraph":
                            break;
                    }
                } catch (ParseException | IllegalArgumentException exception) {
                    System.err.println("Parsing failed.  Reason: " + exception.getMessage());
                }
            } while (true);
        }
    }

    private static void printUsage() {
        System.out.println("Usage : ");
        System.out.println("exit: exit the program");
        System.out.println("encode: encode a message for a particular code");
        System.out.println("decode: decode a message for a particular code");
        System.out.println("generateMessage: generate a random message");
        System.out.println("corruptMessage: corrupt a given message");
        System.out.println("errorDetectingRate: display the rate of detected error message");
        System.out.println("errorCorrectingRate: display the rate of corrected error message");
        System.out.println("generateGraph: generate a graph showing the detecting/correcting error rate of a specified code");
        System.out.println();
    }

//    private static void generateGraph(CommandLineParser parser, Options options, String[] splitCommand) {
//        try {
//            CommandLine line = parser.parse(options, splitCommand);
//            DetectingCode detectingCode = parseCode((String) line.getParsedOptionValue(ProgramOption.CODE));
//            CodeParameter codeParameter = getCodeParameter(detectingCode);
//
////            if (line.hasOption(burstLength)) {
////                codeParameter.setBurstError(true);
////            }
//
////            if (line.hasOption(iterationsPerP)) {
////                codeParameter.setNumberOfIterationsPerProbability(Integer.parseInt((String) line.getParsedOptionValue(iterationsPerP)));
////            }
//
//            Test2.test(codeParameter, detectingCode);
//        } catch (ParseException | IllegalArgumentException exp) {
//            // oops, something went wrong
//            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
//        }
//    }
}
