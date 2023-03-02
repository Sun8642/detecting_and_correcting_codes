package model;

import enums.DetectingCode;
import enums.ErrorChannelModel;
import java.text.Normalizer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.CommandLine;

@Getter
@Setter
public class ProgramParameter {

    protected DetectingCode detectingCode;
    protected ErrorChannelModel errorChannelModel = ErrorChannelModel.CONSTANT_ERROR_CHANNEL_MODEL;

    protected int numberOfIterationsPerProbability = 10000;

    protected int messageBitSize = 8;

    protected double minP = 0.01d;
    protected double maxP = 0.5d;
    protected double p = 0.1d;
    protected int numberOfStep = 50;

    protected boolean canCorrectError = false;

    protected int burstErrorLength = 3;
    private String generatorPolynomial = "1011";
    private String message;

    public void setParameters(CommandLine line) {
        if (line.hasOption(ProgramOption.CODE)) {
            detectingCode = parseCode(line.getOptionValue(ProgramOption.CODE));
        }

        if (line.hasOption(ProgramOption.ERROR_CHANNEL_MODEL)) {
            errorChannelModel = parseErrorModel(line.getOptionValue(ProgramOption.ERROR_CHANNEL_MODEL));
        }

        if (line.hasOption(ProgramOption.BURST_LENGTH)) {
            setBurstErrorLength(Integer.parseInt(line.getOptionValue(ProgramOption.BURST_LENGTH)));
        }

        if (line.hasOption(ProgramOption.MESSAGE_BIT_SIZE)) {
            setMessageBitSize(Integer.parseInt(line.getOptionValue(ProgramOption.MESSAGE_BIT_SIZE)));
        }

        if (line.hasOption(ProgramOption.MIN_P)) {
            setMinP(Double.parseDouble(line.getOptionValue(ProgramOption.MIN_P)));
        }

        if (line.hasOption(ProgramOption.MAX_P)) {
            setMaxP(Double.parseDouble(line.getOptionValue(ProgramOption.MAX_P)));
        }

        if (line.hasOption(ProgramOption.P)) {
            setP(Double.parseDouble(line.getOptionValue(ProgramOption.P)));
        }

        if (line.hasOption(ProgramOption.ITERATIONS_PER_P)) {
            setNumberOfIterationsPerProbability(Integer.parseInt(line.getOptionValue(ProgramOption.ITERATIONS_PER_P)));
        }

        if (line.hasOption(ProgramOption.NB_STEP_PER_P)) {
            setNumberOfStep(Integer.parseInt(line.getOptionValue(ProgramOption.NB_STEP_PER_P)));
        }

        if (line.hasOption(ProgramOption.GENERATOR_POLYNOMIAL)) {
            setGeneratorPolynomial(line.getOptionValue(ProgramOption.GENERATOR_POLYNOMIAL));
        }

        if (line.hasOption(ProgramOption.MESSAGE)) {
            setMessage(line.getOptionValue(ProgramOption.MESSAGE));
        }
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
        if (messageBitSize < 1) {
            throw new IllegalArgumentException("The number of bits per message must be at least one.");
        }
        this.numberOfStep = numberOfStep;
    }

    public void setBurstErrorLength(int burstErrorLength) {
        if (messageBitSize < 1) {
            throw new IllegalArgumentException("The burst error length must be greater than zero.");
        }
        this.burstErrorLength = burstErrorLength;
    }

    public void setGeneratorPolynomial(String generatorPolynomial) {
        if (generatorPolynomial == null || generatorPolynomial.length() == 0) {
            throw new IllegalArgumentException("The polynomial generator must be greater or equals to x (>= 10 in binary representation).");
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
            throw new IllegalArgumentException("The polynomial generator must be greater or equals to x (>= 10 in binary representation).");
        }
        this.generatorPolynomial = generatorPolynomial;
    }
}
