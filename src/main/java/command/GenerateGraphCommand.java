package command;

import model.CommandLineParameter;
import org.math.plot.Plot2DPanel;
import org.math.plot.PlotPanel;

import javax.swing.*;

public class GenerateGraphCommand implements Command {

    @Override
    public void execute(CommandLineParameter commandLineParameter) throws IllegalArgumentException {
        double[] probabilities = new double[commandLineParameter.getNumberOfStep()];
        double[] errorDetectingRates = new double[commandLineParameter.getNumberOfStep()];
        double[] errorCorrectingRates = new double[commandLineParameter.getNumberOfStep()];

        double currentP = commandLineParameter.getMinP();
        double pToAdd = (commandLineParameter.getMaxP() - commandLineParameter.getMinP()) / (commandLineParameter.getNumberOfStep() - 1);
        double[] probabilityForCode;
        for (int i = 0; i < commandLineParameter.getNumberOfStep(); i++) {
            probabilities[i] = currentP;
            probabilityForCode = commandLineParameter.getCode().getErrorDetectionRate(
                    commandLineParameter.getNumberOfIterationsPerProbability(),
                    currentP,
                    commandLineParameter.getMessageBitSize(),
                    commandLineParameter.getErrorChannelModelImpl()
            );
            errorDetectingRates[i] = probabilityForCode[0];
            if (commandLineParameter.getCode().canCorrectError()) {
                errorCorrectingRates[i] = probabilityForCode[1];
            }
            currentP += pToAdd;
        }

        Plot2DPanel plot = new Plot2DPanel();

        plot.setAxisLabels("Probability of a bit being corrupted", "Error detection" + (commandLineParameter.getCode().canCorrectError() ? "/correction" : "") + " rate");
        plot.addLinePlot("Error detection rate", probabilities, errorDetectingRates);

        if (commandLineParameter.getCode().canCorrectError()) {
            plot.addLinePlot("Error correction rate", probabilities, errorCorrectingRates);
            plot.addLegend(PlotPanel.EAST);
        }

        if (commandLineParameter.isBindYAxis()) {
            plot.setFixedBounds(1, commandLineParameter.getMinBoundYAxis(), commandLineParameter.getMaxBoundYAxis());
        }

        JFrame frame = new JFrame("Error detection" + (commandLineParameter.getCode().canCorrectError() ? "/correction" : "") + " rate with code: " + commandLineParameter.getDetectingCode().getArgumentName());
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(1000, 600);
    }
}
