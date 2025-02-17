package pl.michalowski.smpd.model.classifiers;

import pl.michalowski.smpd.datatypes.DataRow;
import pl.michalowski.smpd.utils.MathUtils;

import java.util.*;
import java.util.stream.Collectors;

public class KNMClassifier extends KNAlgorithmClassifier {
    private long seed;

    public KNMClassifier(int k, List<DataRow> dataSet, List<DataRow> trainingSet, List<DataRow> testingSet, long seed) throws ArithmeticException {
        super(k, dataSet, trainingSet, testingSet);
        this.seed = seed;
    }

    @Override
    protected void applyAlgorithm() {
        Map<String, List<DataRow>> categorizedTrainingSet = new HashMap<>();
        for (DataRow dataRow : trainingSet) {
            if (!categorizedTrainingSet.containsKey(dataRow.getLabel())) {
                categorizedTrainingSet.put(dataRow.getLabel(), new ArrayList<>());
            }

            List<DataRow> dataRows = categorizedTrainingSet.get(dataRow.getLabel());
            dataRows.add(new DataRow(dataRow, false));
            categorizedTrainingSet.put(dataRow.getLabel(), dataRows);
        }


        Set<DataRow> finalPoints = new HashSet<>();
        for (Map.Entry<String, List<DataRow>> entry : categorizedTrainingSet.entrySet()) {
            Random random = new Random(seed);
            List<DataRow> avgPoints = entry.getValue().stream().map(d -> new DataRow(d, false)).collect(Collectors.toList());
            Collections.shuffle(avgPoints, random);
            avgPoints = avgPoints.stream().limit(k).collect(Collectors.toList());
            Map<DataRow, List<DataRow>> subClassMap;
            Map<DataRow, List<DataRow>> oldSubClassMap = new HashMap<>();
            boolean classesChanged;
            do {
                classesChanged = false;
                subClassMap = new HashMap<>();
                for (DataRow point : entry.getValue()) {
                    double minDistance = Double.MAX_VALUE;
                    DataRow closestAvgPoint = null;
                    for (DataRow avgPoint : avgPoints) {
                        double currDistance = MathUtils.pointsBetweenDistance(avgPoint.getValues(), point.getValues());
                        if (currDistance < minDistance) {
                            closestAvgPoint = avgPoint;
                            minDistance = currDistance;
                        }
                    }
                    if (!subClassMap.containsKey(closestAvgPoint)) {
                        subClassMap.put(closestAvgPoint, new ArrayList<>());
                    }
                    if (!oldSubClassMap.containsKey(closestAvgPoint) || !oldSubClassMap.get(closestAvgPoint).contains(point)) {
                        classesChanged = true;
                    }
                    List<DataRow> dataRows = subClassMap.get(closestAvgPoint);
                    dataRows.add(point);
                    subClassMap.put(closestAvgPoint, dataRows);
                }

                // calculate new medium points
                for (Map.Entry<DataRow, List<DataRow>> entry1 : subClassMap.entrySet()) {
                    MathUtils.recalculateAvgDataRow(entry1.getKey(), entry1.getValue());
                }
                oldSubClassMap = new HashMap<>(subClassMap);
            } while (classesChanged);

            Set<DataRow> dataRows = subClassMap.keySet();
            finalPoints.addAll(dataRows);
        }


        for (DataRow dataRow : testingSet) {
            double minDistance = Double.MAX_VALUE;
            DataRow closestRow = null;
            for (DataRow trainingRow : finalPoints) {
                double v = MathUtils.pointsBetweenDistance(dataRow.getValues(), trainingRow.getValues());
                if (v < minDistance) {
                    minDistance = v;
                    closestRow = trainingRow;
                }
            }

            DataRow result = new DataRow(dataRow, false);
            result.setLabel(closestRow.getLabel());
            resultSet.add(result);
        }
    }
}
