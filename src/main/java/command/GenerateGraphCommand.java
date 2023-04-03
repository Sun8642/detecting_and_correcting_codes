package command;

import code.CyclicRedundancyCode;
import code.HammingCode;
import code.InternetChecksum;
import code.ParityBitCode;
import enums.DetectingCode;
import model.ProgramParameter;
import org.math.plot.Plot2DPanel;
import org.math.plot.PlotPanel;

import javax.swing.*;

public class GenerateGraphCommand implements Command {

    @Override
    public void execute(ProgramParameter programParameter) throws IllegalArgumentException {
        double[] probabilities = new double[programParameter.getNumberOfStep()];
        double[] errorDetectingRates = new double[programParameter.getNumberOfStep()];
        double[] errorCorrectingRates = new double[programParameter.getNumberOfStep()];

        double currentP = programParameter.getMinP();
        double pToAdd = (programParameter.getMaxP() - programParameter.getMinP()) / (programParameter.getNumberOfStep() - 1);
        double[] probabilityForCode;
        for (int i = 0; i < programParameter.getNumberOfStep(); i++) {
            probabilities[i] = currentP;
            probabilityForCode = getProbabilitiesForCode(programParameter, programParameter.getDetectingCode(), currentP);
            errorDetectingRates[i] = probabilityForCode[0];
            if (programParameter.isCanCorrectError()) {
                errorCorrectingRates[i] = probabilityForCode[1];
            }
            currentP += pToAdd;
        }

        Plot2DPanel plot = new Plot2DPanel();

        plot.setAxisLabels("Probability of a bit being corrupted", "Error detection" + (programParameter.isCanCorrectError() ? "/correction" : "") + " rate");
        plot.addLinePlot("Error detection rate", probabilities, errorDetectingRates);

        if (programParameter.isCanCorrectError()) {
            plot.addLinePlot("Error correction rate", probabilities, errorCorrectingRates);
            plot.addLegend(PlotPanel.EAST);
        }

        JFrame frame = new JFrame("Error detection" + (programParameter.isCanCorrectError() ? "/correction" : "") + " rate with code: " + programParameter.getDetectingCode().getArgumentName());
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(1000, 600);
    }

    private static double[] getProbabilitiesForCode(ProgramParameter programParameter, DetectingCode code, double p) {
        switch (code) {
            case PARITY_BIT_CODE -> {
                return new double[]{ParityBitCode.getErrorDetectionRate(programParameter.getNumberOfIterationsPerProbability(), p, programParameter.getMessageBitSize())};
            }
            case INTERNET_CHECKSUM -> {
                return new double[]{InternetChecksum.getErrorDetectionRate(programParameter.getNumberOfIterationsPerProbability(), p, programParameter.getMessageBitSize())};
            }
            case CYCLIC_REDUNDANCY_CODE -> {
                return new double[]{CyclicRedundancyCode.getErrorDetectionRate(programParameter.getNumberOfIterationsPerProbability(), p,
                        programParameter.getMessageBitSize(), programParameter.getGeneratorPolynomial())};
            }
            case HAMMING_CODE -> {
                return HammingCode.getErrorDetectionRate(programParameter.getNumberOfIterationsPerProbability(), p, programParameter.getMessageBitSize());
            }
            default -> throw new IllegalArgumentException("No code provided or no implementation found");
        }
    }
}
