package test;

import code.CyclicRedundancyCode;
import code.HammingCode;
import code.InternetChecksum;
import code.ParityBitCode;
import enums.DetectingCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.CodeParameter;
import model.CyclicRedundancyCodeParameter;
import org.math.plot.Plot2DPanel;
import org.math.plot.PlotPanel;

import javax.swing.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Test2 {

//    public static void test() {
//        int iterations = 10000;
//        int messageBitSize = 8;
//        double[] x = new double[50];
//        double[] y = new double[50];
////        for (double p = 0.01d; p <= 0.5d; p += 0.01d) {
//
//        int nbPoints = 50;
//        double startP = 0.01d;
//        double endP = 0.5d;
//        double currentP = startP;
//        double pToAdd = (endP - startP) / (nbPoints - 1);
//        for (int i = 0; i < nbPoints; i++) {
//            x[i] = currentP;
//            y[i] = Test.parityBitCode(iterations, currentP, messageBitSize);
//            currentP += pToAdd;
//        }
//
//
//        // create your PlotPanel (you can use it as a JPanel)
//        Plot2DPanel plot = new Plot2DPanel();
//
//        plot.setAxisLabels("Probabilité qu'un bit soit altéré", "Probabilité que l'erreur soit détectée");
//        // add a line plot to the PlotPanel
//        plot.addLinePlot("Probabilité de détecter l'erreur", x, y);
//
//        // put the PlotPanel in a JFrame, as a JPanel
//        JFrame frame = new JFrame("Probabilité de détecter l'erreur avec le code de bit de parité");
//        frame.setContentPane(plot);
//        frame.setVisible(true);
//    }

    public static void test(CodeParameter codeParameter, DetectingCode code) {
        double[] x = new double[codeParameter.getNumberOfStep()];
        double[] y = new double[codeParameter.getNumberOfStep()];
        double[] z = new double[codeParameter.getNumberOfStep()];

        double currentP = codeParameter.getMinP();
        double pToAdd = (codeParameter.getMaxP() - codeParameter.getMinP()) / (codeParameter.getNumberOfStep() - 1);
        double[] probabilityForCode;
        for (int i = 0; i < codeParameter.getNumberOfStep(); i++) {
            x[i] = currentP;
            probabilityForCode = getProbabilitiesForCode(codeParameter, code, currentP);
            y[i] = probabilityForCode[0];
            if (codeParameter.isCanCorrectError()) {
                z[i] = probabilityForCode[1];
            }
            currentP += pToAdd;
        }

        Plot2DPanel plot = new Plot2DPanel();

        plot.setAxisLabels("Probability of a bit being corrupted", "Error detection" + (codeParameter.isCanCorrectError() ? "/correction" : "") + " rate");
        plot.addLinePlot("Error detection rate", x, y);

        if (codeParameter.isCanCorrectError()) {
            plot.addLinePlot("Error correction rate", x, z);
            plot.addLegend(PlotPanel.EAST);
        }

        JFrame frame = new JFrame("Error detection" + (codeParameter.isCanCorrectError() ? "/correction" : "") + " rate with code: " + code.name());
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(1000, 600);
    }

    private static double[] getProbabilitiesForCode(CodeParameter codeParameter, DetectingCode code, double p) {
        switch (code) {
            case PARITY_BIT_CODE -> {
                return new double[]{ParityBitCode.getErrorDetectionRate(codeParameter.getNumberOfIterationsPerProbability(), p, codeParameter.getMessageBitSize())};
            }
            case INTERNET_CHECKSUM -> {
                return new double[]{InternetChecksum.getErrorDetectionRate(codeParameter.getNumberOfIterationsPerProbability(), p, codeParameter.getMessageBitSize())};
            }
            case CYCLIC_REDUNDANCY_CODE -> {
                return new double[]{CyclicRedundancyCode.getErrorDetectionRate(codeParameter.getNumberOfIterationsPerProbability(), p,
                        codeParameter.getMessageBitSize(), ((CyclicRedundancyCodeParameter) codeParameter).getGeneratorPolynomial())};
            }
            case HAMMING_CODE -> {
                return HammingCode.getErrorDetectionRate(codeParameter.getNumberOfIterationsPerProbability(), p, codeParameter.getMessageBitSize());
            }
            default -> throw new IllegalArgumentException("No code provided or no implementation found");
        }
    }
}
