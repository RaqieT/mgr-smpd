package pl.michalowski.smpd.datatypes;

import java.util.List;
import java.util.UUID;

public class DataRow {
    private UUID id = UUID.randomUUID();
    private String label;
    private List<Double> values;

    public DataRow(String label, List<Double> values) {
        this.label = label;
        this.values = values;
    }

    public DataRow(DataRow dataRow, boolean clearLabels) {
        this.id = dataRow.getId();
        if (!clearLabels) {
            this.label = dataRow.getLabel();
        }
        this.values = dataRow.getValues();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
