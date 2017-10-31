package io.github.maven.domain;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDoc {

    private List<TableDoc> tables = new ArrayList<TableDoc>();
    private List<GroupDoc> groups = new ArrayList<GroupDoc>();
    private String name;
    private String description;

    public List<TableDoc> getTables() {
        return tables;
    }

    public void setTables(List<TableDoc> tables) {
        this.tables = tables;
    }

    public List<GroupDoc> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDoc> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}