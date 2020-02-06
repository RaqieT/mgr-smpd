package pl.michalowski.smpd.model.linear.discriminant;

import pl.michalowski.smpd.datatypes.DataRow;

import java.util.List;

public abstract class AbstractFisherLinearDiscriminant implements FisherLinearDiscriminant {
    protected Integer propertiesNumber;

    protected AbstractFisherLinearDiscriminant(Integer propertiesNumber) {
        if (propertiesNumber <= 0) {
            throw new ArithmeticException("properties number must be greater than 0");
        }

        this.propertiesNumber = propertiesNumber;
    }
}
