package pl.michalowski.smpd.model.factory;

import org.apache.commons.lang3.NotImplementedException;
import pl.michalowski.smpd.Consts;
import pl.michalowski.smpd.datatypes.DataRow;
import pl.michalowski.smpd.model.linear.discriminant.FisherLinearDiscriminant;
import pl.michalowski.smpd.model.linear.discriminant.StandardFisherLinearDiscriminant;

import java.util.List;
import java.util.Optional;

public class LinearDiscriminantFactory {
    public static final Optional<FisherLinearDiscriminant> create(String type, Integer propNumber) {
        switch (type) {
            case Consts.Fisher.STANDARD:
                return Optional.of(new StandardFisherLinearDiscriminant(propNumber));
            case Consts.Fisher.FAST:
                throw new NotImplementedException("SFS is not implemented");
            default:
                return Optional.empty();
        }
    }
}
