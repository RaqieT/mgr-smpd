package pl.michalowski.smpd.utils;

import pl.michalowski.smpd.datatypes.DataRow;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// DO PRZEPISANIA
public class FileImporter {
    public static List<DataRow> convertFromCsv(String fileName) {
        List<DataRow> dataRows = new ArrayList<>();
        String currLabel;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                List<Double> data = new ArrayList<>();
                currLabel = values[0];
                for (int i = 1; i < values.length; i++) {
                    data.add(Double.parseDouble(values[i]));
                }
                dataRows.add(new DataRow(currLabel, data));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find CSV file");

        } catch (IOException e) {
            System.err.println("Error occurred in file importer");
        }

        return dataRows;
    }
}
