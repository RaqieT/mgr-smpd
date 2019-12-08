package pl.michalowski.smpd.model.classifiers;

import pl.michalowski.smpd.datatypes.DataRow;
import pl.michalowski.smpd.utils.MathUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class KNNClassifier extends KNAlgorithmClassifier {

    public KNNClassifier(int k, List<DataRow> dataSet, int trainingPercent, long seed) {
        super(k, dataSet, trainingPercent, seed);
    }

    @Override
    protected void applyAlgorithm() {
        this.resultSet = new ArrayList<>();
        for (DataRow testingRow : testingSet) {
            HashMap<DataRow, Double> distances = new HashMap<>();

            for (DataRow trainingRow : trainingSet) {
                distances.put(trainingRow, MathUtils.computeBetweenPointsDistance(testingRow.getValues(), trainingRow.getValues()));
            }

            List<DataRow> closestPoints = distances
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .limit(k)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            DataRow max = Collections.max(closestPoints, Comparator.comparing(DataRow::getLabel));
            String mostOccurringLabelInClosestPoints = max.getLabel();
            DataRow dataRow = new DataRow(testingRow, false);
            dataRow.setLabel(mostOccurringLabelInClosestPoints);
            this.resultSet.add(dataRow);
        }
    }
}
