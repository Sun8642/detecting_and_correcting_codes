package command;

import code.CyclicRedundancyCode;
import code.HammingCode;
import code.InternetChecksum;
import code.ParityBitCode;
import model.ProgramParameter;

import java.math.BigInteger;

public class DecodeCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        decodeMessage(programParameter);
        System.out.println(new BigInteger(programParameter.getMessage().toString()).toString(2));
    }

    private void decodeMessage(ProgramParameter programParameter) throws IllegalArgumentException {
        switch (programParameter.getDetectingCode()) {
            case PARITY_BIT_CODE -> {
                ParityBitCode.decode(programParameter.getMessage());
            }
            case CYCLIC_REDUNDANCY_CODE -> {
                CyclicRedundancyCode.decode(programParameter.getMessage(), programParameter.getGeneratorPolynomial());
            }
            case INTERNET_CHECKSUM -> {
                InternetChecksum.decode(programParameter.getMessage());
            }
            case HAMMING_CODE -> {
                HammingCode.decode(programParameter.getMessage(), true);
            }
            default ->
                    throw new IllegalArgumentException("Couldn't encode message for code: " + programParameter.getDetectingCode().getArgumentName());
        }
    }
}
