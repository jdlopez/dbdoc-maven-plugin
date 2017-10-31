package io.github.maven.domain;

import java.util.ArrayList;
import java.util.List;

public class TableDoc {
    private String name;
    private String description;
    // jdbc values https://docs.oracle.com/javase/7/docs/api/java/sql/DatabaseMetaData.html#getTables()
    private String catalog;
    private String schema;
    private String type;
    // ...
    private List<ColumnDoc> columns = new ArrayList<ColumnDoc>();

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

    public List<ColumnDoc> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDoc> columns) {
        this.columns = columns;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
