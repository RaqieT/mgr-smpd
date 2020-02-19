package pl.michalowski.smpd.utils;

import com.github.dakusui.combinatoradix.Combinator;
import pl.michalowski.smpd.datatypes.DataRow;

import java.util.ArrayList;
import java.util.Collections;
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

    public static List<List<Double>> transposeMatrix(List<List<Double>> matrix) {
        List<List<Double>> transposedMatrix = new ArrayList<>();
        final int N = matrix.get(0).size();
        for (int i = 0; i < N; i++) {
            List<Double> col = new ArrayList<>();
            for (List<Double> row : matrix) {
                col.add(row.get(i));
            }
            transposedMatrix.add(col);
        }
        return transposedMatrix;
    }

    public static List<List<Double>> transposeAndMultiplyMatrices(List<List<Double>> matrix) {
        List<List<Double>> result = new ArrayList<>();

        for (int i = 0; i < matrix.size(); i++) {
            List<Double> vector = matrix.get(i);
            List<Double> singleRow = new ArrayList<>();
            for (int k = 0; k < matrix.size(); k++) {
                List<Double> anotherVector = matrix.get(k);
                double sum = 0;
                for (int j = 0; j < vector.size(); j++) {
                    sum += vector.get(j) * anotherVector.get(j);
                }
                singleRow.add(sum);
            }
            result.add(singleRow);
        }

        return result;
    }
    public static Double determinant(List<List<Double>> matrix) {
        List<List<Double>> temporary;
        double result = 0;

        if (matrix.size() == 1) {
            result = matrix.get(0).get(0);
            return (result);
        }

        if (matrix.size() == 2) {
            result = ((matrix.get(0).get(0) * matrix.get(1).get(1)) - (matrix.get(0).get(1) * matrix.get(1).get(0)));
            return (result);
        }

        for (int i = 0; i < matrix.get(0).size(); i++) {
            // init
            temporary = new ArrayList<>();
            for (int j = 0; j < matrix.size() - 1; j++) {
                List<Double> zeroes = new ArrayList<>();
                for (int k = 0; k < matrix.get(0).size() - 1; k++) {
                    zeroes.add(0d);
                }
                temporary.add(zeroes);
            }

            for (int j = 1; j < matrix.size(); j++) {
                for (int k = 0; k < matrix.get(0).size(); k++) {
                    if (k < i) {
                        temporary.get(j - 1).set(k, matrix.get(j).get(k));
                    } else if (k > i) {
                        temporary.get(j - 1).set(k - 1, matrix.get(j).get(k));
                    }
                }
            }

            result += matrix.get(0).get(i) * Math.pow (-1, i) * determinant (temporary);
        }
        return (result);
    }

    public static Combinator<Integer> getCombinationsWithoutDuplicates(int size, int numberOfPicks) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return new Combinator<>(list, numberOfPicks);
    }
}
