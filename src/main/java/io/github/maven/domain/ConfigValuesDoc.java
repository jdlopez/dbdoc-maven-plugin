package io.github.maven.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigValuesDoc {
    private String title;
    private String description;
    @JsonIgnore
    private Map<String, PropertyValueDoc> valuesSet = new HashMap<>();
    @JsonGetter
    public Collection<PropertyValueDoc> getValues() {
        return valuesSet.values();
    }
    @JsonSetter
    public void setValues(Collection<PropertyValueDoc> values) {
        for (PropertyValueDoc p: values) {
            valuesSet.put(p.getEntryName(), p);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, PropertyValueDoc> getValuesSet() {
        return valuesSet;
    }

    public void setValuesSet(Map<String, PropertyValueDoc> valuesSet) {
        this.valuesSet = valuesSet;
    }
}
