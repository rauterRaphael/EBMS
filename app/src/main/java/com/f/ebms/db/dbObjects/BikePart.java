package com.f.ebms.db.dbObjects;

public class BikePart {

    private String partName;

    private String notes;

    private boolean toBeRepaired;

    private boolean finished;

    public BikePart(String partName) {
        this.partName = partName;
        this.toBeRepaired = false;
        this.finished = false;
        this.notes = "";
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartName() {
        return partName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isToBeRepaired() {
        return toBeRepaired;
    }

    public void setToBeRepaired(boolean toBeRepaired) {
        this.toBeRepaired = toBeRepaired;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "BikePart{" +
                "partName='" + partName + '\'' +
                ", notes='" + notes + '\'' +
                ", toBeRepaired=" + toBeRepaired +
                ", finished=" + finished +
                '}';
    }

}
