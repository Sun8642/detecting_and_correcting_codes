package command.line;

import command.enums.DetectingCode;
import command.enums.ErrorChannelModel;
import command.enums.MainCommand;
import lombok.Getter;
import lombok.Setter;
import math.BigInt;
import org.apache.commons.cli.CommandLine;
import service.channel.error.BurstErrorChannelModel;
import service.channel.error.ConstantErrorChannelModel;
import service.code.Code;
import service.code.CyclicRedundancyCode;
import service.code.HammingCode;
import service.code.InternetChecksum;
import service.code.ParityBitCode;

import java.math.BigInteger;
import java.text.Normalizer;

@Getter
@Setter
public class CommandLineParameter {

    public static final String ERROR_POLYNOMIAL_GENERATOR_MIN_VALUE = "The polynomial generator must be greater or equals to 10 (in binary representation).";

    private int numberOfIterationsPerProbability = 10000;

    private int messageBitSize = 8;

    private double minP = 0.01d;
    private double maxP = 0.5d;
    private double p = 0.1d;
    private int numberOfStep = 50;
    private double minBoundYAxis = 0.0d;
    private double maxBoundYAxis = 1.0d;
    private boolean bindYAxis = false;

    private int burstErrorLength = 3;
    private BigInt generatorPolynomial = new BigInt(Long.parseLong("1011", 2));
    private BigInt message;

    protected DetectingCode detectingCode;
    protected Code code;
    protected ErrorChannelModel errorChannelModel = ErrorChannelModel.CONSTANT_ERROR_CHANNEL_MODEL;
    protected service.channel.error.ErrorChannelModel errorChannelModelImpl = new ConstantErrorChannelModel();

    public void setParameters(CommandLine line) {
        if (line.hasOption(CommandLineOption.BURST_LENGTH)) {
            setBurstErrorLength(Integer.parseInt(line.getOptionValue(CommandLineOption.BURST_LENGTH)));
        }

        if (line.hasOption(CommandLineOption.MESSAGE_BIT_SIZE)) {
            setMessageBitSize(Integer.parseInt(line.getOptionValue(CommandLineOption.MESSAGE_BIT_SIZE)));
        }

        if (line.hasOption(CommandLineOption.MIN_P)) {
            setMinP(Double.parseDouble(line.getOptionValue(CommandLineOption.MIN_P)));
        }

        if (line.hasOption(CommandLineOption.MAX_P)) {
            setMaxP(Double.parseDouble(line.getOptionValue(CommandLineOption.MAX_P)));
        }

        if (line.hasOption(CommandLineOption.P)) {
            setP(Double.parseDouble(line.getOptionValue(CommandLineOption.P)));
        }

        if (line.hasOption(CommandLineOption.ITERATIONS_PER_P)) {
            setNumberOfIterationsPerProbability(Integer.parseInt(line.getOptionValue(CommandLineOption.ITERATIONS_PER_P)));
        }

        if (line.hasOption(CommandLineOption.NB_STEP_PER_P)) {
            setNumberOfStep(Integer.parseInt(line.getOptionValue(CommandLineOption.NB_STEP_PER_P)));
        }

        if (line.hasOption(CommandLineOption.MIN_BOUND_Y_AXIS)) {
            bindYAxis = true;
            setMinBoundYAxis(Double.parseDouble(line.getOptionValue(CommandLineOption.MIN_BOUND_Y_AXIS)));
        }

        if (line.hasOption(CommandLineOption.MAX_BOUND_Y_AXIS)) {
            bindYAxis = true;
            setMaxBoundYAxis(Double.parseDouble(line.getOptionValue(CommandLineOption.MAX_BOUND_Y_AXIS)));
        }

        if (line.hasOption(CommandLineOption.GENERATOR_POLYNOMIAL)) {
            setGeneratorPolynomial(line.getOptionValue(CommandLineOption.GENERATOR_POLYNOMIAL));
        }

        if (line.hasOption(CommandLineOption.MESSAGE)) {
            String messageFromOption = line.getOptionValue(CommandLineOption.MESSAGE);
            setMessage(messageFromOption);
            setMessageBitSize(messageFromOption.length());
            if (line.hasOption(CommandLineOption.MESSAGE_BIT_SIZE) && Integer.parseInt(line.getOptionValue(CommandLineOption.MESSAGE_BIT_SIZE)) != messageFromOption.length()) {
                System.out.println("Warning, overriding message bit size by the length of the message.");
            }
        }

        if (line.hasOption(CommandLineOption.ERROR_CHANNEL_MODEL)) {
            errorChannelModel = parseErrorModel(line.getOptionValue(CommandLineOption.ERROR_CHANNEL_MODEL));
            switch (errorChannelModel) {
                case CONSTANT_ERROR_CHANNEL_MODEL -> errorChannelModelImpl = new ConstantErrorChannelModel();
                case BURST_ERROR_CHANNEL_MODEL -> errorChannelModelImpl = new BurstErrorChannelModel(burstErrorLength);
            }
        }

        if (line.hasOption(CommandLineOption.CODE)) {
            detectingCode = parseCode(line.getOptionValue(CommandLineOption.CODE));
            switch (detectingCode) {
                case CYCLIC_REDUNDANCY_CODE -> code = new CyclicRedundancyCode(generatorPolynomial);
                case HAMMING_CODE -> code = new HammingCode();
                case INTERNET_CHECKSUM -> code = new InternetChecksum();
                case PARITY_BIT_CODE -> code = new ParityBitCode();
            }
        }
    }

    public static MainCommand parseMainCommand(String mainCommand) {
        //https://www.baeldung.com/java-remove-accents-from-text
        mainCommand = Normalizer.normalize(mainCommand.trim().toLowerCase(), Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        for (MainCommand command : MainCommand.values()) {
            if (mainCommand.equals(command.getArgumentName().toLowerCase())) {
                return command;
            }
        }
        throw new IllegalArgumentException("The command given in parameter is not valid: " + mainCommand);
    }

    public static DetectingCode parseCode(String code) {
        //https://www.baeldung.com/java-remove-accents-from-text
        code = Normalizer.normalize(code.trim().toLowerCase(), Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        for (DetectingCode detectingCode : DetectingCode.values()) {
            if (code.equals(detectingCode.getArgumentName().toLowerCase())) {
                return detectingCode;
            }
        }
        throw new IllegalArgumentException("The code given in parameter is not valid: " + code);
    }

    public static ErrorChannelModel parseErrorModel(String errorModel) {
        //https://www.baeldung.com/java-remove-accents-from-text
        errorModel = Normalizer.normalize(errorModel.trim().toLowerCase(), Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        for (ErrorChannelModel errorChannelModel : ErrorChannelModel.values()) {
            if (errorModel.equals(errorChannelModel.getArgumentName().toLowerCase())) {
                return errorChannelModel;
            }
        }
        throw new IllegalArgumentException("The error model given in parameter is not valid: " + errorModel);
    }

    public void setNumberOfIterationsPerProbability(int numberOfIterationsPerProbability) {
        if (numberOfIterationsPerProbability < 1) {
            throw new IllegalArgumentException("The number of iterations per probability must be at least one.");
        }
        this.numberOfIterationsPerProbability = numberOfIterationsPerProbability;
    }

    public void setMessageBitSize(int messageBitSize) {
        if (messageBitSize < 1) {
            throw new IllegalArgumentException("The number of bits per message must be at least one.");
        }
        this.messageBitSize = messageBitSize;
    }

    public void setMinP(double minP) {
        if (minP <= 0.d || minP > 1.d) {
            throw new IllegalArgumentException("The probability to have an error must be ]0, 1].");
        }
        if (minP > maxP) {
            maxP = minP;
        }
        this.minP = minP;
    }

    public void setMaxP(double maxP) {
        if (maxP <= 0.d || maxP > 1.d) {
            throw new IllegalArgumentException("The probability to have an error must be ]0, 1].");
        }
        if (minP > maxP) {
            minP = maxP;
        }
        this.maxP = maxP;
    }

    public void setP(double p) {
        if (p <= 0.d || p > 1.d) {
            throw new IllegalArgumentException("The probability to have an error must be ]0, 1].");
        }
        this.p = p;
    }

    public void setNumberOfStep(int numberOfStep) {
        if (messageBitSize < 2) {
            throw new IllegalArgumentException("The number of bits per message must be at least two.");
        }
        this.numberOfStep = numberOfStep;
    }

    public void setMinBoundYAxis(double minBoundYAxis) {
        if (minBoundYAxis < 0.0d || minBoundYAxis > 1.0d) {
            throw new IllegalArgumentException("The minimum bound of the Y axis must be [0, 1] (current value: " +
                    minBoundYAxis + ").");
        }
        this.minBoundYAxis = minBoundYAxis;
    }

    public void setMaxBoundYAxis(double maxBoundYAxis) {
        if (maxBoundYAxis < 0.0d || maxBoundYAxis > 1.0d) {
            throw new IllegalArgumentException("The maximum bound of the Y axis must be [0, 1] (current value: " +
                    maxBoundYAxis + ").");
        }
        if (maxBoundYAxis < minBoundYAxis) {
            throw new IllegalArgumentException("The value of the maximum bound of the Y axis value (" + maxBoundYAxis +
                    ") should we higher or equal to the value of the minimum bound of the Y axis (actual value: " +
                    minBoundYAxis + ")");
        }
        this.maxBoundYAxis = maxBoundYAxis;
    }

    public void setBurstErrorLength(int burstErrorLength) {
        if (messageBitSize < 1) {
            throw new IllegalArgumentException("The burst error length must be greater than zero.");
        }
        this.burstErrorLength = burstErrorLength;
    }

    public void setGeneratorPolynomial(String generatorPolynomial) {
        if (generatorPolynomial == null || generatorPolynomial.length() == 0) {
            throw new IllegalArgumentException(ERROR_POLYNOMIAL_GENERATOR_MIN_VALUE);
        }
        generatorPolynomial = generatorPolynomial.trim();
        int oneCount = 0;
        for (int i = 0; i < generatorPolynomial.length(); i++) {
            if (generatorPolynomial.charAt(i) != '0' && generatorPolynomial.charAt(i) != '1') {
                throw new IllegalArgumentException("The polynomial generator can only contain 1s and 0s as characters.");
            }
            if (generatorPolynomial.charAt(i) == '1') {
                oneCount++;
            }
        }
        if (oneCount == 0 || (oneCount == 1 && generatorPolynomial.charAt(generatorPolynomial.length() - 1) == '1')) {
            throw new IllegalArgumentException(ERROR_POLYNOMIAL_GENERATOR_MIN_VALUE);
        }
        this.generatorPolynomial = BigInt.from(new BigInteger(generatorPolynomial, 2));
    }

    public void setMessage(String message) {
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) != '0' && message.charAt(i) != '1') {
                throw new IllegalArgumentException("The message can only contain 1s and 0s as characters.");
            }
        }
        setMessageBitSize(message.length());
        this.message = BigInt.from(new BigInteger(message, 2));
    }
}
