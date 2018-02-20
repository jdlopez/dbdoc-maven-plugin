package io.github.maven.domain;

public class PropertyValueDoc {
    private String entryName;
    private String description;

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
}
