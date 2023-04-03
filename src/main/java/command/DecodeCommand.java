package command;

import code.CyclicRedundancyCode;
import code.HammingCode;
import code.InternetChecksum;
import code.ParityBitCode;
import model.ProgramParameter;
import util.BitUtil;

public class DecodeCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        decodeMessage(programParameter);
    }

    private void decodeMessage(ProgramParameter programParameter) throws IllegalArgumentException {
        int decodedMessageLength;
        switch (programParameter.getDetectingCode()) {
            case PARITY_BIT_CODE -> {
                ParityBitCode.decode(programParameter.getMessage());
                decodedMessageLength = programParameter.getMessageBitSize() - 1;
            }
            case CYCLIC_REDUNDANCY_CODE -> {
                CyclicRedundancyCode.decode(programParameter.getMessage(), programParameter.getGeneratorPolynomial());
                decodedMessageLength = programParameter.getMessageBitSize() - (programParameter.getGeneratorPolynomial().getLeftMostSetBit() - 1);
            }
            case INTERNET_CHECKSUM -> {
                InternetChecksum.decode(programParameter.getMessage());
                decodedMessageLength = programParameter.getMessageBitSize() - 16;
            }
            case HAMMING_CODE -> {
                int numberOfRedundancyBitsAdded = BitUtil.leftMostSetBit(programParameter.getMessageBitSize());
                decodedMessageLength = programParameter.getMessageBitSize() - numberOfRedundancyBitsAdded;
                if (HammingCode.decode(programParameter.getMessage(), true, decodedMessageLength)) {
                    System.out.println("An error was detected in the given message, correcting it automatically.");
                }
            }
            default ->
                    throw new IllegalArgumentException("Couldn't encode message for code: " + programParameter.getDetectingCode().getArgumentName());
        }
        System.out.println(programParameter.getMessage().toBinaryString(decodedMessageLength));
    }
}
