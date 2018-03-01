package io.github.maven.domain;

public class PropertyValueDoc {
    private String entryName;
    private String description;
    // usefull on 'false positives' like documentation lines and so on
    private Boolean unused;

    public PropertyValueDoc() {}

    public PropertyValueDoc(String key) {
        this.entryName = key;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getUnused() {
        return unused;
    }

    public void setUnused(Boolean unused) {
        this.unused = unused;
    }
}
