package pl.michalowski.smpd.utils;

import pl.michalowski.smpd.datatypes.DataRow;

import java.util.ArrayList;
import java.util.List;

public class MathUtils {
    public static double pointsBetweenDistance(List<Double> a, List<Double> b) {
        int size = Math.min(a.size(), b.size());
        double sum = 0d;
        for (int i = 0; i < size; i++) {
             sum += Math.pow(a.get(i) - b.get(i), 2);
        }
        return Math.sqrt(sum);
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
            sums.add(dataRows.stream().map(dr -> dr.getValues().get(z)).reduce(0d, Double::sum));
        }


        List<Double> result = new ArrayList<>();

        for (Double value : sums) {
            result.add(value/dataRows.size());
        }
        avgPoint.setValues(result);
    }
}
