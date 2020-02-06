package pl.michalowski.smpd;

import pl.michalowski.smpd.datatypes.DataRow;
import pl.michalowski.smpd.model.classifiers.KNAlgorithmClassifier;
import pl.michalowski.smpd.model.factory.ClassifierFactory;
import pl.michalowski.smpd.model.factory.LinearDiscriminantFactory;
import pl.michalowski.smpd.model.linear.discriminant.FisherLinearDiscriminant;
import pl.michalowski.smpd.utils.FileImporter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("smpd").build()
                .defaultHelp(true)
                .description("Classifies given data set.");
        parser.addArgument("-f", "--file")
                .required(true)
                .help("specify csv file with dataset");
        parser.addArgument("-m", "--method")
                .choices(Consts.NearestNeighboursMethods.KNN, Consts.NearestNeighboursMethods.KNM)
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
        parser.addArgument("-f", "--fisher")
                .choices(Consts.Fisher.STANDARD, Consts.Fisher.FAST, Consts.Fisher.NONE)
                .setDefault(Consts.Fisher.FAST)
                .help("fisher method type");
        parser.addArgument("-fpn", "--fisher_properties_number")
                .type(Integer.class)
                .setDefault(2)
                .help("fisher properties number");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        List<DataRow> dataSet = FileImporter.convertFromCsv(ns.getString("file"));

        Optional<FisherLinearDiscriminant> optionalFisherLinearDiscriminant = LinearDiscriminantFactory.create(ns.getString("fisher"),
                dataSet,
                ns.getInt("fisher_properties_number"));

        if (optionalFisherLinearDiscriminant.isPresent()) {
           dataSet = optionalFisherLinearDiscriminant.get().pickBestProperties();
        }

        KNAlgorithmClassifier classifier = ClassifierFactory.create(ns.getString("method"), ns.getInt("k"), dataSet,
                ns.getInt("training_set_percent"), ns.getLong("seed"));
        classifier.classify();

        System.out.println(classifier.getTruthLevel());
    }
}
