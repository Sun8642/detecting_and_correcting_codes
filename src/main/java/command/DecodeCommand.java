package command;

import code.CyclicRedundancyCode;
import code.HammingCode;
import code.InternetChecksum;
import code.ParityBitCode;
import model.ProgramParameter;

public class DecodeCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        System.out.println(decodeMessage(programParameter));
    }

    private String decodeMessage(ProgramParameter programParameter) throws IllegalArgumentException {
        switch (programParameter.getDetectingCode()) {
            case PARITY_BIT_CODE -> {
                return ParityBitCode.decode(programParameter.getMessage());
            }
            case CYCLIC_REDUNDANCY_CODE -> {
                return CyclicRedundancyCode.decode(programParameter.getMessage(), programParameter.getGeneratorPolynomial());
            }
            case INTERNET_CHECKSUM -> {
                return InternetChecksum.decode(programParameter.getMessage());
            }
            case HAMMING_CODE -> {
                return HammingCode.decode(programParameter.getMessage(), true).getDecodedMessage();
            }
            default -> throw new IllegalArgumentException("Couldn't encode message for code: " + programParameter.getDetectingCode().getArgumentName());
        }
    }
}
