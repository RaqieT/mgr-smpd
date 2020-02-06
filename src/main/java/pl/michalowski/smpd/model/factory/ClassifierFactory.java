package pl.michalowski.smpd.model.factory;

import pl.michalowski.smpd.Consts;
import pl.michalowski.smpd.datatypes.DataRow;
import pl.michalowski.smpd.model.classifiers.KNAlgorithmClassifier;
import pl.michalowski.smpd.model.classifiers.KNMClassifier;
import pl.michalowski.smpd.model.classifiers.KNNClassifier;

import java.util.List;

public class ClassifierFactory {
    public static KNAlgorithmClassifier create(String method, int k, List<DataRow> dataSet, List<DataRow> trainingSet, List<DataRow> testingSet, long seed) {
        switch (method) {
            case Consts.NearestNeighboursMethods.KNM:
                return new KNMClassifier(k, dataSet, trainingSet, testingSet, seed);
            case Consts.NearestNeighboursMethods.KNN:
            default:
                return new KNNClassifier(k, dataSet, trainingSet, testingSet);
        }
    }
}
