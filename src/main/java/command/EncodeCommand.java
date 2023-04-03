package command;

import code.CyclicRedundancyCode;
import code.HammingCode;
import code.InternetChecksum;
import code.ParityBitCode;
import model.ProgramParameter;

public class EncodeCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        encodeMessage(programParameter);
    }

    private void encodeMessage(ProgramParameter programParameter) throws IllegalArgumentException {
        int encodedMessageLength;
        switch (programParameter.getDetectingCode()) {
            case PARITY_BIT_CODE -> {
                ParityBitCode.encode(programParameter.getMessage());
                encodedMessageLength = programParameter.getMessageBitSize() + 1;
            }
            case CYCLIC_REDUNDANCY_CODE -> {
                CyclicRedundancyCode.encode(programParameter.getMessage(), programParameter.getGeneratorPolynomial());
                encodedMessageLength = programParameter.getMessageBitSize() + (programParameter.getGeneratorPolynomial().getLeftMostSetBit() - 1);
            }
            case INTERNET_CHECKSUM -> {
                InternetChecksum.encode(programParameter.getMessage());
                encodedMessageLength = programParameter.getMessageBitSize() + 16;
            }
            case HAMMING_CODE -> {
                HammingCode.encode(programParameter.getMessage(), true, programParameter.getMessageBitSize());
                encodedMessageLength = programParameter.getMessageBitSize() + HammingCode.numberOfRedundancyBitsToAdd(programParameter.getMessageBitSize());
            }
            default ->
                    throw new IllegalArgumentException("Couldn't encode message for code: " + programParameter.getDetectingCode().getArgumentName());
        }
        System.out.println(programParameter.getMessage().toBinaryString(encodedMessageLength));
    }
}
