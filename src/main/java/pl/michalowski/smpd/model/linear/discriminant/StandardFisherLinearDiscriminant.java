package pl.michalowski.smpd.model.linear.discriminant;

import org.apache.commons.lang3.NotImplementedException;
import pl.michalowski.smpd.datatypes.DataRow;

import java.util.*;

public class StandardFisherLinearDiscriminant extends AbstractFisherLinearDiscriminant {

    public StandardFisherLinearDiscriminant(Integer propertiesNumber) {
        super(propertiesNumber);
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

        int propsNumber = trainingSet.get(0).getValues().size(); // that is dirty, because we trust that every data row has the same amount of properties as first row :)


        if (propertiesNumber == 1) {

            if (dataRowsByLabel.keySet().size() > 2) {
                throw new ArithmeticException("Algorithm supports only 2 classes (labels)");
            }

            Iterator<Map.Entry<String, List<DataRow>>> iterator = dataRowsByLabel.entrySet().iterator();

            Map.Entry<String, List<DataRow>> aLabel = iterator.next();
            if (!iterator.hasNext()) {
                throw new ArithmeticException("Only one class found in dataset");
            }
            Map.Entry<String, List<DataRow>> bLabel = iterator.next();


            List<Property> properties = new ArrayList<>();
            for (int i = 0; i < propsNumber; i++) {
                Property p = new Property(i);

                // for a class/label
                List<Double> valueOfSingleProperty = new ArrayList<>();
                for (DataRow dataRow : aLabel.getValue()) {
                    valueOfSingleProperty.add(dataRow.getValues().get(i));
                }
                p.setDataRowsFromLabelAValues(valueOfSingleProperty);

                // for b class/label
                valueOfSingleProperty = new ArrayList<>();
                for (DataRow dataRow : bLabel.getValue()) {
                    valueOfSingleProperty.add(dataRow.getValues().get(i));
                }
                p.setDataRowsFromLabelBValues(valueOfSingleProperty);


                properties.add(p); // sum of current property value for all data rows of current label
            }

            double maxValue = Double.MIN_VALUE;
            Property bestProperty = null;

            for (Property property : properties) {
                if (property.getFisherValue() > maxValue) {
                    bestProperty = property;
                }
            }

            if (bestProperty == null) {
                throw new ArithmeticException("Best property not found!");
            }


            return new ArrayList<>(Collections.singletonList(bestProperty.getPropertyNumber()));
        }

        throw new NotImplementedException("propertiesNumber > 1 will be implemented later");
    }

    private static class Property { // cecha
        private List<Double> dataRowsFromLabelAValues;
        private List<Double> dataRowsFromLabelBValues;
        private int propertyNumber; // numer cechy

        public Property(int propNum) {
            this.propertyNumber = propNum;
        }

        public Double getFisherValue() {
            double miValueA = dataRowsFromLabelAValues.stream().reduce(0d, Double::sum) / dataRowsFromLabelAValues.size();
            double dValueA = Math.sqrt(dataRowsFromLabelAValues.stream().reduce(0d, (a, b) -> Math.pow(a - miValueA, 2) + Math.pow(b - miValueA, 2)));
            double miValueB = dataRowsFromLabelBValues.stream().reduce(0d, Double::sum) / dataRowsFromLabelBValues.size();
            double dValueB = Math.sqrt(dataRowsFromLabelBValues.stream().reduce(0d, (a, b) -> Math.pow(a - miValueB, 2) + Math.pow(b - miValueB, 2)));
            return Math.abs(miValueA - miValueB)/(dValueA + dValueB);
        }

        public void setDataRowsFromLabelAValues(List<Double> dataRowsFromLabelAValues) {
            this.dataRowsFromLabelAValues = dataRowsFromLabelAValues;
        }

        public void setDataRowsFromLabelBValues(List<Double> dataRowsFromLabelBValues) {
            this.dataRowsFromLabelBValues = dataRowsFromLabelBValues;
        }

        public int getPropertyNumber() {
            return propertyNumber;
        }
    }

}
