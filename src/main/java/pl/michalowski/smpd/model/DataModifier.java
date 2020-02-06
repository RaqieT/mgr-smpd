package pl.michalowski.smpd.model;

import pl.michalowski.smpd.datatypes.DataRow;
import pl.michalowski.smpd.model.linear.discriminant.FisherLinearDiscriminant;

import java.util.*;
import java.util.stream.Collectors;

public class DataModifier {
    private List<DataRow> dataSet;
    private List<DataRow> trainingSet;
    private List<DataRow> testingSet;
    private int trainingPercent;
    private long seed;
    private FisherLinearDiscriminant fisherLinearDiscriminant;

    public DataModifier(List<DataRow> dataSet, int trainingPercent, long seed, FisherLinearDiscriminant fisherLinearDiscriminant) {
        if (trainingPercent <= 0 || trainingPercent >= 100) {
            throw new ArithmeticException("Training set percent cannot be lower than 1 and higher than 99");
        }
        this.dataSet = dataSet;
        this.trainingPercent = trainingPercent;
        this.seed = seed;
        this.fisherLinearDiscriminant = fisherLinearDiscriminant;
    }

    public DataModifier(List<DataRow> dataSet, int trainingPercent, long seed) {
        this(dataSet, trainingPercent, seed, null);
    }

    public void loadDataSets() {
        splitGivenDataSetToTrainingAndTestingSet();
        applyFisherIfAvailable();
    }

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

    private void applyFisherIfAvailable() {
        if (fisherLinearDiscriminant == null) {
            return;
        }

        List<Integer> bestPropertiesCols = fisherLinearDiscriminant.getBestPropertiesCols(trainingSet);

        this.dataSet = selectBestCols(bestPropertiesCols, dataSet);
        this.trainingSet = selectBestCols(bestPropertiesCols, trainingSet);
        this.testingSet = selectBestCols(bestPropertiesCols, testingSet);
    }

    private List<DataRow> selectBestCols(List<Integer> bestPropertiesCols, List<DataRow> dataRows) {
        List<DataRow> resultNewDataRows = new ArrayList<>();
        for (DataRow dataRow : dataRows) {
            DataRow newDataRow = new DataRow(dataRow, false);
            List<Double> bestColsValues = new ArrayList<>();
            for (int i = 0; i < dataRow.getValues().size(); i++) {
                if (bestPropertiesCols.contains(i)) {
                    bestColsValues.add(dataRow.getValues().get(i));
                }
            }
            newDataRow.setValues(bestColsValues);
            resultNewDataRows.add(newDataRow);
        }
        return resultNewDataRows;
    }

    public List<DataRow> getTrainingSet() {
        return trainingSet;
    }

    public List<DataRow> getTestingSet() {
        return testingSet;
    }

    public List<DataRow> getDataSet() {
        return dataSet;
    }
}
