package command;

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
            probabilityForCode = programParameter.getCode().getErrorDetectionRate(
                    programParameter.getNumberOfIterationsPerProbability(),
                    programParameter.getP(),
                    programParameter.getMessageBitSize(),
                    programParameter.getErrorChannelModelImpl()
            );
            errorDetectingRates[i] = probabilityForCode[0];
            if (programParameter.getCode().canCorrectError()) {
                errorCorrectingRates[i] = probabilityForCode[1];
            }
            currentP += pToAdd;
        }

        Plot2DPanel plot = new Plot2DPanel();

        plot.setAxisLabels("Probability of a bit being corrupted", "Error detection" + (programParameter.getCode().canCorrectError() ? "/correction" : "") + " rate");
        plot.addLinePlot("Error detection rate", probabilities, errorDetectingRates);

        if (programParameter.getCode().canCorrectError()) {
            plot.addLinePlot("Error correction rate", probabilities, errorCorrectingRates);
            plot.addLegend(PlotPanel.EAST);
        }

        JFrame frame = new JFrame("Error detection" + (programParameter.getCode().canCorrectError() ? "/correction" : "") + " rate with code: " + programParameter.getDetectingCode().getArgumentName());
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(1000, 600);
    }
}
