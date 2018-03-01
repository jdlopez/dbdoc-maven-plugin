package io.github.maven.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database documentation model. Contains all documented data. User documentation in 'description' attribute
 */
public class DatabaseDoc {

    private List<TableDoc> tables = new ArrayList<TableDoc>();
    private List<GroupDoc> groups = new ArrayList<GroupDoc>();
    private String name;
    private String description;
    private List<Map<String, Object>> queryResult = Collections.emptyList();
    private Map<String, String> defaults = new HashMap<>();

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

    public List<Map<String, Object>> getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(List<Map<String, Object>> queryResult) {
        this.queryResult = queryResult;
    }

    public Map<String, String> getDefaults() {
        return defaults;
    }

    public void setDefaults(Map<String, String> defaults) {
        this.defaults = defaults;
    }
}
