package pl.michalowski.smpd.utils;


import com.github.dakusui.combinatoradix.Combinator;
import com.github.dakusui.combinatoradix.HomogeniousCombinator;
import com.github.dakusui.combinatoradix.Permutator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    void transposeAndMultiplyMatrices() {
        List<List<Double>> lists = MathUtils.transposeAndMultiplyMatrices(Arrays.asList(Arrays.asList(-1d, 0d, 0d, 1d, 0d), Arrays.asList(-1d, 0d, 1d, -1d, 1d)));
        assertEquals(2d, lists.get(0).get(0));
        assertEquals(0d, lists.get(0).get(1));
        assertEquals(0d, lists.get(1).get(0));
        assertEquals(4d, lists.get(1).get(1));
    }

    @Test
    void determinant() {
        assertEquals(4, MathUtils.determinant(Arrays.asList(Arrays.asList(2d, 2d), Arrays.asList(2d, 4d))));
        assertEquals(-306, MathUtils.determinant(Arrays.asList(Arrays.asList(6d, 1d, 1d), Arrays.asList(4d, -2d, 5d), Arrays.asList(2d, 8d, 7d))));
    }

    @Test
    void combinations() {
        assertEquals(41664, MathUtils.getCombinationsWithoutDuplicates(64, 3).size());
    }
}