package pl.michalowski.smpd;

import pl.michalowski.smpd.datatypes.DataRow;
import pl.michalowski.smpd.model.classifiers.KNAlgorithmClassifier;
import pl.michalowski.smpd.model.factory.ClassifierFactory;
import pl.michalowski.smpd.utils.FileImporter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("smpd").build()
                .defaultHelp(true)
                .description("Classifies given data set.");
        parser.addArgument("-f", "--file")
                .required(true)
                .help("specify csv file with dataset");
        parser.addArgument("-m", "--method")
                .choices(Consts.Methods.KNN, Consts.Methods.KNM)
                .help("specify classifying method");
        parser.addArgument("-k")
                .type(Integer.class)
                .setDefault(1)
                .help("specify k-nearest");
        parser.addArgument("-tsp", "--training_set_percent")
                .type(Integer.class)
                .setDefault(80)
                .help("specify training set percent");
        parser.addArgument("-s", "--seed")
                .type(Long.class)
                .setDefault(System.currentTimeMillis())
                .help("specify seed");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        List<DataRow> dataSet = FileImporter.convertFromCsv(ns.getString("file"));
        KNAlgorithmClassifier classifier = ClassifierFactory.create(ns.getString("method"), ns.getInt("k"), dataSet,
                ns.getInt("training_set_percent"), ns.getLong("seed"));
        classifier.classify();
        System.out.println(classifier.getTruthLevel());
    }
}
