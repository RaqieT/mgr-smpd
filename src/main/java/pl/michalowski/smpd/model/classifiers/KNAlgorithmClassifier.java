package pl.michalowski.smpd.model.classifiers;

import pl.michalowski.smpd.datatypes.DataRow;

import java.util.*;
import java.util.stream.Collectors;

public abstract class KNAlgorithmClassifier {
    private Double truthLevel = null;
    protected int k;
    protected List<DataRow> dataSet;
    protected List<DataRow> trainingSet;
    protected List<DataRow> testingSet;
    protected List<DataRow> resultSet = new ArrayList<>();


    public KNAlgorithmClassifier(int k, List<DataRow> dataSet, List<DataRow> trainingSet, List<DataRow> testingSet) throws ArithmeticException {
        if (k <= 0) {
            throw new ArithmeticException("k cannot be lowest than 1");
        }
        this.k = k;
        this.dataSet = dataSet;
        this.trainingSet = trainingSet;
        this.testingSet = testingSet;
    }

    public void classify() throws ArithmeticException {
        applyAlgorithm();
        countTruthLevel();
    }

    protected abstract void applyAlgorithm();

    private void countTruthLevel() throws ArithmeticException {
        int resultSize = resultSet.size();
        int trulyClassified = 0;
        for (DataRow resultPoint : resultSet) {
            Optional<DataRow> dataSetPointSearch = dataSet.stream().filter(dr -> dr.getId().equals(resultPoint.getId())).findFirst();
            DataRow dataSetPoint = dataSetPointSearch.orElseThrow(() -> new ArithmeticException("Cannot find element from result set in data set."));
            if(dataSetPoint.getLabel().equals(resultPoint.getLabel())) {
                trulyClassified++;
            }
        }
        this.truthLevel = (double) trulyClassified / resultSize;
    }

    public Double getTruthLevel() {
        return truthLevel;
    }
}
