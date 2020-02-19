package pl.michalowski.smpd.model.linear.discriminant;

public abstract class AbstractFisherLinearDiscriminant implements FisherLinearDiscriminant {
    protected Integer providedPropertiesNumber;

    protected AbstractFisherLinearDiscriminant(Integer providedPropertiesNumber) {
        if (providedPropertiesNumber <= 0) {
            throw new ArithmeticException("properties number must be greater than 0");
        }

        if (providedPropertiesNumber > 64) {
            throw new ArithmeticException("properties number must be lower than 64");
        }

        this.providedPropertiesNumber = providedPropertiesNumber;
    }
}
