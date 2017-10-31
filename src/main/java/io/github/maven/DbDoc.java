package io.github.maven;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.github.maven.domain.ColumnDoc;
import io.github.maven.domain.DatabaseDoc;
import io.github.maven.domain.TableDoc;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Goal which creates dbdoc files
 */
@Mojo( name = "dbdoc", defaultPhase = LifecyclePhase.SITE )
public class DbDoc extends AbstractMojo
{
    /** Output dir for documentation*/
    @Parameter( defaultValue = "${project.build.directory}/site", property = "outputDir", required = true )
    private File outputDirectory;
    /** Input source file. Created if not exists */
    @Parameter( defaultValue = "${project.basedir}/src/site/${project.artifactId}-dbdoc.json", property = "sourceFile", required = false )
    private File sourceFile;
    // db connection config parameters
    @Parameter( property = "jdbcDriver", required = false )
    private String jdbcDriver;
    @Parameter( property = "jdbcUrl", required = true )
    private String jdbcUrl;
    @Parameter( property = "jdbcUser", required = true )
    private String jdbcUser;
    @Parameter( property = "jdbcPass", required = true )
    private String jdbcPass;
    @Parameter( defaultValue = "false", property = "overwriteSource", required = false )
    private boolean overwriteSource;
    @Parameter( property = "schemas", required = false )
    private String[] schemas;
    @Parameter( property = "documentationTemplate", required = false )
    private File documentationTemplate;
    @Parameter( property = "outputDocFile", required = false )
    private File outputDocFile = null;

    public void execute() throws MojoExecutionException {
        ObjectMapper om = new ObjectMapper();
        DatabaseDoc documentation = null;
        try {
            // read source
            if (sourceFile != null && sourceFile.exists()) {
                documentation = om.readValue(sourceFile, DatabaseDoc.class);
            } else {
                documentation = new DatabaseDoc();
            } // endif source
            // getting connection
            getLog().info("Getting jdbc connection");
            //DriverManager.registerDriver();
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
            DatabaseMetaData dbMetadata = connection.getMetaData();
            // get name
            if (documentation.getName() == null) {
                // get catalog?
                documentation.setName(dbMetadata.getDatabaseProductName());
            }
            if (schemas != null && schemas.length > 1) {
                for (String schema : schemas)
                    buildSchemaDocumentation(dbMetadata, documentation, schema);
            } else {
                buildSchemaDocumentation(dbMetadata, documentation,null);
            } // endif schemas
            // write source
            File outSourceFile;
            if (overwriteSource) {
                getLog().info("Overwriting source");
                outSourceFile = sourceFile;
            } else {
                outSourceFile = new File(outputDirectory, "source-tables.json");
            }
            // keeps nulls so its easy to fulfill doc
            //om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.writerWithDefaultPrettyPrinter().writeValue(outSourceFile, documentation);

            // generate doc
            Reader templateReader = null;
            if (documentationTemplate != null)
                templateReader = new FileReader(documentationTemplate);
            else
                templateReader = new InputStreamReader(this.getClass().getResourceAsStream("/template-html.mustache"));
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(templateReader, "template");
            if (outputDocFile == null)
                outputDocFile = new File(outputDirectory, "database.html");
            mustache.execute(new FileWriter(outputDocFile), documentation).flush();

        } catch (IOException e) {
            throw new MojoExecutionException("Reading source file: " + sourceFile, e);
        } catch (SQLException e) {
            throw new MojoExecutionException("Reading jdbc database: " +
                    jdbcDriver + " " + jdbcUrl, e);
        }
    }

    private void buildSchemaDocumentation(DatabaseMetaData dbMetadata, DatabaseDoc documentation, String schemaName) throws SQLException {
        ResultSet rsTables = dbMetadata.getTables(null, schemaName, null, new String[]{"TABLE"});
        while (rsTables.next()) {
            String tableName = rsTables.getString("TABLE_NAME");
            TableDoc tbl = null;
            // find first if exists:
            if (documentation.getTables() != null) {
                for (TableDoc t: documentation.getTables()) {
                    if (t.getName().equalsIgnoreCase(tableName)) {
                        tbl = t;
                        break;
                    }
                } // end for
            } // endif tables exists
            if (tbl == null) {
                tbl = new TableDoc();
                documentation.getTables().add(tbl);
            }
            // update jdbc values:
            tbl.setName(tableName);
            tbl.setCatalog(rsTables.getString("TABLE_CAT"));
            tbl.setSchema(rsTables.getString("TABLE_SCHEM"));
            tbl.setType(rsTables.getString("TABLE_TYPE"));
            if (tbl.getDescription() == null || tbl.getDescription().trim().equals(""))
                tbl.setDescription(rsTables.getString("REMARKS"));
            // add columns
            ResultSet rsColumns = dbMetadata.getColumns(tbl.getCatalog(), tbl.getSchema(), tbl.getName(), null);
            while (rsColumns.next()) {
                String colName = rsColumns.getString("COLUMN_NAME");
                ColumnDoc colDoc = findColumn(tbl.getColumns(), colName);
                colDoc.setType(rsColumns.getString("TYPE_NAME"));
                colDoc.setSize(rsColumns.getString("COLUMN_SIZE"));
                colDoc.setNullable( "YES".equalsIgnoreCase(rsColumns.getString("IS_NULLABLE")) );
                colDoc.setConstraints(rsColumns.getString("COLUMN_DEF"));
                if (colDoc.getDescription() == null)
                    colDoc.setDescription(rsColumns.getString("REMARKS"));

            } // while columns
            rsColumns.close();
        } // while tables
        rsTables.close();

    }

    private ColumnDoc findColumn(List<ColumnDoc> columns, String colName) {
        if (columns != null && !columns.isEmpty()) {
            for (ColumnDoc c : columns) {
                if (c.getName().equalsIgnoreCase(colName))
                    return c;
            }
        }
        // not found create one and add to list
        ColumnDoc col = new ColumnDoc();
        col.setName(colName);
        columns.add(col);
        return col;
    }
}
