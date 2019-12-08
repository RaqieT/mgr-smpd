package pl.michalowski.smpd.utils;

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
}
