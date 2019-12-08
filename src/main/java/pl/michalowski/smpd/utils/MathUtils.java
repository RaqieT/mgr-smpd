package pl.michalowski.smpd.utils;

import pl.michalowski.smpd.datatypes.DataRow;

import java.util.ArrayList;
import java.util.List;

public class MathUtils {
    // DO PRZEPISANIA
    public static double computeBetweenPointsDistance(List<Double> x1, List<Double> x2) throws ArithmeticException {
        if (x1.isEmpty() && x2.isEmpty()) {
            throw new ArithmeticException("Cannot find values to computePointsBetweenDistance distance ");
        }

        if (x1.size() != x2.size()) {
            throw new ArithmeticException("Cannot computePointsBetweenDistance distance because values sizes are not equal");
        }

        int size = x1.size();
        double result = 0d;

        // (x1 - x2)^2 + ....
        for (int i = 0; i < size; i++) {
            result += Math.pow(x1.get(i) - x2.get(i), 2);
        }
        return Math.sqrt(result);
    }

    public static void recalculateAvgDataRow(DataRow avgPoint, List<DataRow> dataRows) {
        if (dataRows.size() == 0) {
            return;
        }

        if (dataRows.size() == 1) {
            avgPoint.setValues(dataRows.get(0).getValues());
        }

        List<Double> sums = new ArrayList<>();

        for (int i = 0; i < dataRows.get(0).getValues().size(); i++) {
            final int z = i;
            sums.add(dataRows.stream().map(dr -> dr.getValues().get(z)).reduce(0d, (a, b) -> a+b));
        }


        List<Double> result = new ArrayList<>();

        for (Double value : sums) {
            result.add(value/dataRows.size());
        }
        avgPoint.setValues(result);
    }
}
