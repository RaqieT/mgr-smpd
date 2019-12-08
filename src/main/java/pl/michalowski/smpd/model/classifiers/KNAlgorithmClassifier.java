package pl.michalowski.smpd.model.classifiers;

import pl.michalowski.smpd.datatypes.DataRow;

import java.util.*;
import java.util.stream.Collectors;

public abstract class KNAlgorithmClassifier {
    private int trainingPercent;
    private Double truthLevel = null;
    protected int k;
    protected List<DataRow> dataSet;
    protected long seed;
    protected List<DataRow> trainingSet;
    protected List<DataRow> testingSet;
    protected List<DataRow> resultSet;


    public KNAlgorithmClassifier(int k, List<DataRow> dataSet, int trainingPercent, long seed) throws ArithmeticException {
        if (k <= 0) {
            throw new ArithmeticException("k cannot be lowest than 1");
        }
        if (trainingPercent <= 0 || trainingPercent >= 100) {
            throw new ArithmeticException("Training set percent cannot be lower than 1 and higher than 99");
        }
        this.k = k;
        this.dataSet = dataSet;
        this.trainingPercent = trainingPercent;
        this.seed = seed;
    }

    public void classify() throws ArithmeticException {
        splitGivenDataSetToTrainingAndTestingSet();
        applyAlgorithm();
        countTruthLevel();
    }

    protected abstract void applyAlgorithm();

    private void splitGivenDataSetToTrainingAndTestingSet() throws ArithmeticException {
        int trainingSetSize = dataSet.size() * trainingPercent / 100;
        List<DataRow> shuffledDataSet = new ArrayList<>(dataSet);
        Collections.shuffle(shuffledDataSet, new Random(seed));
        this.trainingSet = shuffledDataSet.subList(0, trainingSetSize-1).stream().map(dr -> new DataRow(dr, false)).collect(Collectors.toList());
        if (this.trainingSet.isEmpty()) {
            throw new ArithmeticException("Training set is empty, it is probably caused by too low training set percent, try to increase it");
        }
        this.testingSet = shuffledDataSet.subList(trainingSetSize, shuffledDataSet.size() - 1).stream().map(dr -> new DataRow(dr, true)).collect(Collectors.toList());
        if (this.testingSet.isEmpty()) {
            throw new ArithmeticException("Testing set is empty, it is probably caused by too high training set percent, try to decrease it");
        }
    }

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
