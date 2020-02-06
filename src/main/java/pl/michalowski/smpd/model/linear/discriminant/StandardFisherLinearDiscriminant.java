package pl.michalowski.smpd.model.linear.discriminant;

import pl.michalowski.smpd.datatypes.DataRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardFisherLinearDiscriminant extends AbstractFisherLinearDiscriminant {

    public StandardFisherLinearDiscriminant(List<DataRow> dataRows, Integer propertiesNumber) {
        super(dataRows, propertiesNumber);
    }

    @Override
    public List<DataRow> pickBestProperties() {
        // F = |MiA - MiB| / (da + db)
        Map<String, List<Double>> labelMiValuesForEveryProperty = new HashMap<>();


        return null;
    }
}
