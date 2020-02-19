package pl.michalowski.smpd.model.linear.discriminant;

import com.github.dakusui.combinatoradix.Combinator;
import pl.michalowski.smpd.datatypes.DataRow;
import pl.michalowski.smpd.utils.MathUtils;
import pl.michalowski.smpd.utils.SimpleLogger;

import java.util.*;
import java.util.stream.Collectors;

public class StandardFisherLinearDiscriminant extends AbstractFisherLinearDiscriminant {
    private SimpleLogger simpleLogger = new SimpleLogger(this.getClass());

    public StandardFisherLinearDiscriminant(Integer providedPropertiesNumber) {
        super(providedPropertiesNumber);
    }


    @Override
    public List<Integer> getBestPropertiesCols(List<DataRow> trainingSet) {
        // F = |MiA - MiB| / (da + db)
        Map<String, List<DataRow>> dataRowsByLabel = new HashMap<>(); // size = 2

        // here we categorize dataset
        for (DataRow dataRow : trainingSet) {
            if (!dataRowsByLabel.containsKey(dataRow.getLabel())) {
                dataRowsByLabel.put(dataRow.getLabel(), new ArrayList<>());
            }
            List<DataRow> dataRows = dataRowsByLabel.get(dataRow.getLabel());
            dataRows.add(dataRow);
            dataRowsByLabel.put(dataRow.getLabel(), dataRows);
        }

        int allPropsNumber = trainingSet.get(0).getValues().size(); // that is dirty, because we trust that every data row has the same amount of properties as first row :)

        simpleLogger.log("Calculating combinations...");

        if (dataRowsByLabel.keySet().size() > 2) {
            throw new ArithmeticException("Algorithm supports only 2 classes (labels)");
        }

        Iterator<Map.Entry<String, List<DataRow>>> iterator = dataRowsByLabel.entrySet().iterator();

        Map.Entry<String, List<DataRow>> aLabel = iterator.next();
        if (!iterator.hasNext()) {
            throw new ArithmeticException("Only one class found in dataset");
        }

        Map.Entry<String, List<DataRow>> bLabel = iterator.next();
        if (iterator.hasNext()) {
            throw new ArithmeticException("Algorithm supports only two classes");
        }

        List<PropertyVector> properties = new ArrayList<>();

        List<List<Double>> mappedA = aLabel.getValue().stream().map(DataRow::getValues).collect(Collectors.toList());
        List<List<Double>> transposedA = MathUtils.transposeMatrix(mappedA);

        List<List<Double>> mappedB = bLabel.getValue().stream().map(DataRow::getValues).collect(Collectors.toList());
        List<List<Double>> transposedB = MathUtils.transposeMatrix(mappedB);


        Combinator<Integer> combinationsWithoutDuplicates = MathUtils.getCombinationsWithoutDuplicates(allPropsNumber, providedPropertiesNumber);
        int combinationNum = 1;
        long size = combinationsWithoutDuplicates.size();
        for (List<Integer> columns : combinationsWithoutDuplicates) {
            simpleLogger.log("Preparing data set. Progress: " + (combinationNum++) + "/" + size);
            PropertyVector propertyVector = new PropertyVector(columns);
            List<List<Double>> aLabelPropertiesValues = new ArrayList<>();
            for (Integer colNum : columns) {
                aLabelPropertiesValues.add(transposedA.get(colNum));
            }
            propertyVector.setDataRowsFromLabelAValues(aLabelPropertiesValues);
            List<List<Double>> bLabelPropertiesValues = new ArrayList<>();
            for (Integer colNum : columns) {
                bLabelPropertiesValues.add(transposedB.get(colNum));
            }
            propertyVector.setDataRowsFromLabelBValues(bLabelPropertiesValues);
            properties.add(propertyVector);
        }




//        for (int i = 0; i < allPropsNumber; i++) {
//            // TODO: for j in propertiesNumber
//            PropertyVector p = new PropertyVector(i);
//
//            // for a class/label
//            List<Double> valueOfSingleProperty = new ArrayList<>();
//            for (DataRow dataRow : aLabel.getValue()) {
//                valueOfSingleProperty.add(dataRow.getValues().get(i));
//            }
//            p.setDataRowsFromLabelAValues(valueOfSingleProperty);
//
//            // for b class/label
//            valueOfSingleProperty = new ArrayList<>();
//            for (DataRow dataRow : bLabel.getValue()) {
//                valueOfSingleProperty.add(dataRow.getValues().get(i));
//            }
//            p.setDataRowsFromLabelBValues(valueOfSingleProperty);
//
//
//            properties.add(p); // sum of current property value for all data rows of current label
//        }

        double maxValue = Double.MIN_VALUE;
        PropertyVector bestPropertyVector = null;

        for (int i = 0; i < properties.size(); i++) {
            PropertyVector propertyVector = properties.get(i);
            simpleLogger.log("Calculating Standard Fisher for " + propertyVector.getPropertyNumbers() + "...");
            Double fisherValue = propertyVector.calculateFisher();

            if (fisherValue > maxValue) {
                bestPropertyVector = propertyVector;
                maxValue = fisherValue;
            }
            simpleLogger.log("Done. Value is F = " + fisherValue
                    + " | Best combination: " + (bestPropertyVector == null ? "none" : "(F = " + bestPropertyVector.getFisherValue() + ")" + bestPropertyVector.getPropertyNumbers())
                    + " | Progress is " + (i + 1) + "/" + properties.size());
        }

        if (bestPropertyVector == null) {
            throw new ArithmeticException("Best property not found!");
        }


        return bestPropertyVector.getPropertyNumbers();
    }

    private static class PropertyVector { // vector of property, size is equal to 64 above @providedPropertiesNumber
        //    datarow 1 | datarow 2 | datarow 3
        // c1     3           2          1
        // c2     1           2          3
        // c3     0           0          0
        private List<List<Double>> dataRowsFromLabelAValues;
        private List<List<Double>> dataRowsFromLabelBValues;
        private List<Integer> propertyNumbers; // property numbers, size is equal to @providedPropertiesNumber < 64
        private Double fisherValue;

        public PropertyVector(List<Integer> propertyNumbers) {
            this.propertyNumbers = propertyNumbers;
        }

        public Double calculateFisher() {
            List<Double> miValueA = calculateMi(dataRowsFromLabelAValues);
            double dValueA = MathUtils.determinant(MathUtils.transposeAndMultiplyMatrices(minusMi(miValueA, dataRowsFromLabelAValues)));
            List<Double> miValueB = calculateMi(dataRowsFromLabelBValues);
            double dValueB =  MathUtils.determinant(MathUtils.transposeAndMultiplyMatrices(minusMi(miValueB, dataRowsFromLabelBValues)));
            fisherValue = Math.abs(MathUtils.pointsBetweenDistance(miValueA, miValueB))/(dValueA + dValueB);
            return fisherValue;
        }

        public Double getFisherValue() {
            return fisherValue;
        }

        private List<Double> calculateMi(List<List<Double>> dataRows) {
            return dataRows.stream().map(collection -> collection.stream().reduce(0d, Double::sum) / collection.size()).collect(Collectors.toList());
        }

        private List<List<Double>> minusMi(List<Double> miVector, List<List<Double>> matrix) {
            List<List<Double>> result = new ArrayList<>();
            for (int i = 0; i < matrix.size(); i++) {
                List<Double> vector = matrix.get(i);
                result.add(new ArrayList<>());
                for (int j = 0; j < vector.size(); j++) {
                    result.get(i).add(vector.get(j) - miVector.get(i));
                }
            }
            return result;
        }

        public void setDataRowsFromLabelAValues(List<List<Double>> dataRowsFromLabelAValues) {
            this.dataRowsFromLabelAValues = dataRowsFromLabelAValues;
        }

        public void setDataRowsFromLabelBValues(List<List<Double>> dataRowsFromLabelBValues) {
            this.dataRowsFromLabelBValues = dataRowsFromLabelBValues;
        }

        public List<Integer> getPropertyNumbers() {
            return propertyNumbers;
        }
    }

}
