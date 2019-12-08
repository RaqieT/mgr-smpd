package pl.michalowski.smpd.model.classifiers;

import pl.michalowski.smpd.datatypes.DataRow;
import java.util.List;

public class KNMClassifier extends KNAlgorithmClassifier {

    public KNMClassifier(int k, List<DataRow> dataSet, int trainingPercent, long seed) throws ArithmeticException {
        super(k, dataSet, trainingPercent, seed);
    }

    public double computeMedium(List<Double> values) {
        double sum = 0d;

        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    @Override
    protected void applyAlgorithm() {
        
    }
}
