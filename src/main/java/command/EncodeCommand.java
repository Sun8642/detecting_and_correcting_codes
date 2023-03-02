package command;

import code.CyclicRedundancyCode;
import code.HammingCode;
import code.InternetChecksum;
import code.ParityBitCode;
import model.ProgramParameter;

public class EncodeCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        System.out.println(encodedMessage(programParameter));
    }

    private String encodedMessage(ProgramParameter programParameter) throws IllegalArgumentException {
        switch (programParameter.getDetectingCode()) {
            case PARITY_BIT_CODE -> {
                return ParityBitCode.encode(programParameter.getMessage());
            }
            case CYCLIC_REDUNDANCY_CODE -> {
                return CyclicRedundancyCode.encode(programParameter.getMessage(), programParameter.getGeneratorPolynomial());
            }
            case INTERNET_CHECKSUM -> {
                return InternetChecksum.encode(programParameter.getMessage());
            }
            case HAMMING_CODE -> {
                return HammingCode.encode(programParameter.getMessage(), true);
            }
            default -> throw new IllegalArgumentException("Couldn't encode message for code: " + programParameter.getDetectingCode().getArgumentName());
        }
    }
}
