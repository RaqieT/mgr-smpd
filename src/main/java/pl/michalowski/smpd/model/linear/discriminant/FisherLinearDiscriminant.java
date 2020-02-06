package pl.michalowski.smpd.model.linear.discriminant;

import pl.michalowski.smpd.datatypes.DataRow;

import java.util.List;

public interface FisherLinearDiscriminant {
    List<Integer> getBestPropertiesCols(List<DataRow> trainingSet);
}
