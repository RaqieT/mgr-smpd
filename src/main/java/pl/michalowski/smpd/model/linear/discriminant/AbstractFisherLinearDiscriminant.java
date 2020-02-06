package pl.michalowski.smpd.model.linear.discriminant;

import pl.michalowski.smpd.datatypes.DataRow;

import java.util.List;

public abstract class AbstractFisherLinearDiscriminant implements FisherLinearDiscriminant {
    private List<DataRow> dataRows;
    private Integer propertiesNumber;

    protected AbstractFisherLinearDiscriminant(List<DataRow> dataRows, Integer propertiesNumber) {
        this.dataRows = dataRows;
        this.propertiesNumber = propertiesNumber;
    }
}
