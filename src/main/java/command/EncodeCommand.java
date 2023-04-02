package command;

import code.CyclicRedundancyCode;
import code.HammingCode;
import code.InternetChecksum;
import code.ParityBitCode;
import model.ProgramParameter;

import java.math.BigInteger;

public class EncodeCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        encodeMessage(programParameter);
        System.out.println(new BigInteger(programParameter.getMessage().toString()).toString(2));
    }

    private void encodeMessage(ProgramParameter programParameter) throws IllegalArgumentException {
        switch (programParameter.getDetectingCode()) {
            case PARITY_BIT_CODE -> {
                ParityBitCode.encode(programParameter.getMessage());
            }
            case CYCLIC_REDUNDANCY_CODE -> {
                CyclicRedundancyCode.encode(programParameter.getMessage(), programParameter.getGeneratorPolynomial());
            }
            case INTERNET_CHECKSUM -> {
                InternetChecksum.encode(programParameter.getMessage());
            }
            case HAMMING_CODE -> {
                HammingCode.encode(programParameter.getMessage(), true, programParameter.getMessageBitSize());
            }
            default -> throw new IllegalArgumentException("Couldn't encode message for code: " + programParameter.getDetectingCode().getArgumentName());
        }
    }
}
