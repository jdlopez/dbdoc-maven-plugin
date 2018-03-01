package io.github.maven;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.github.maven.domain.ConfigValuesDoc;
import io.github.maven.domain.PropertyValueDoc;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * Goal which creates configdoc files
 */
@Mojo( name = "configdoc", defaultPhase = LifecyclePhase.SITE )
public class ConfigDoc extends AbstractMojo {
    /** Output dir for documentation*/
    @Parameter( defaultValue = "${project.build.directory}/site", property = "cfgOutputDirectory", required = true )
    private File cfgOutputDirectory;
    /** Input source file. Created if not exists */
    @Parameter( defaultValue = "${project.basedir}/src/site/${project.artifactId}-configdoc.json", property = "cfgSourceFile", required = false )
    private File cfgSourceFile;
    /** Input source directory */
    @Parameter( defaultValue = "${project.basedir}/src", property = "cfgSourceScanDirectory", required = false )
    private File cfgSourceScanDirectory;
    /** Pattern to search. Use asterix to point variable name */
    @Parameter( defaultValue = ".getProperty(\"*\")", property = "cfgEntryPatterns", required = false )
    private List<String> cfgEntryPatterns;
    @Parameter( defaultValue = "false", property = "cfgOverwriteSource", required = false )
    private boolean cfgOverwriteSource;
    @Parameter( property = "cfgDocumentationTemplate", required = false )
    private File cfgDocumentationTemplate;
    @Parameter( property = "cfgOutputDocFile", required = false )
    private File cfgOutputDocFile = null;
    // calculated vars
    private String[] prefixPatterns;
    private String[] suffixPatterns;

    public void execute() throws MojoExecutionException, MojoFailureException {
        ObjectMapper om = new ObjectMapper();
        // ignore unknown properties
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ConfigValuesDoc documentation = null;
        try {
            prefixPatterns = new String[cfgEntryPatterns.size()];
            suffixPatterns = new String[cfgEntryPatterns.size()];
            // * used as wildcard :-o
            for (int i = 0; i < cfgEntryPatterns.size(); i++) {
                int idxPattern = cfgEntryPatterns.get(i).indexOf("*");
                prefixPatterns[i] = cfgEntryPatterns.get(i).substring(0, idxPattern);
                suffixPatterns[i] = cfgEntryPatterns.get(i).substring(idxPattern + 1);
            } // endfor search for pattern suffix and prefix
            // //////////////////////////
            // read source
            if (cfgSourceFile != null && cfgSourceFile.exists()) {
                documentation = om.readValue(cfgSourceFile, ConfigValuesDoc.class);
            } else {
                documentation = new ConfigValuesDoc();
            } // endif source
            scanDirectory(documentation, cfgSourceScanDirectory);
            // //////////////////////////
            // write source
            File outSourceFile;
            if (cfgOverwriteSource) {
                getLog().info("Overwriting source with new meta-data");
                outSourceFile = cfgSourceFile;
            } else {
                outSourceFile = new File(cfgOutputDirectory, "source-configdoc.json");
            }
            // keeps nulls so its easy to fulfill doc
            //om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.writerWithDefaultPrettyPrinter().writeValue(outSourceFile, documentation);

            // //////////////////////////
            // generate doc
            getLog().info("Generating documentation file");
            Reader templateReader = null;
            if (cfgDocumentationTemplate != null)
                templateReader = new FileReader(cfgDocumentationTemplate);
            else
                templateReader = new InputStreamReader(this.getClass().getResourceAsStream("/template-config-html.mustache"));
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(templateReader, "template");
            if (cfgOutputDocFile == null)
                cfgOutputDocFile = new File(cfgOutputDirectory, "configuration.html");
            mustache.execute(new FileWriter(cfgOutputDocFile), documentation).flush();
            getLog().info("Done");

        } catch (IOException e) {
            throw new MojoExecutionException("Reading source file: " + cfgSourceFile, e);
        }
    }

    /** Recursive deept scan folder */
    private void scanDirectory(ConfigValuesDoc documentation, File directory) throws IOException{
        if (directory != null) {
            getLog().debug("Scanning " + directory);
            if (directory.isDirectory()) {
                for (File f: directory.listFiles()) {
                    scanDirectory(documentation, f);
                }
            } else {
                readConfigEntriesFromFile(documentation, directory);
            }
        } // endif null
    }

    private void readConfigEntriesFromFile(ConfigValuesDoc documentation, File scannedFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(scannedFile));
        String line = "";
        while ((line = br.readLine()) != null) {
            for (int i = 0; i < prefixPatterns.length; i++) {
                String pref = prefixPatterns[i];
                int idxStart = line.indexOf(pref);
                if (idxStart > -1) { // found config entry!
                    getLog().debug("Found pattern: " + pref);
                    int idxEnd = line.indexOf(suffixPatterns[i], idxStart + pref.length() + 1);
                    if (idxEnd > -1) {
                        String configEntry = line.substring(idxStart + pref.length(), idxEnd);
                        if (!documentation.getValuesSet().containsKey(configEntry))
                            documentation.getValuesSet().put(configEntry, new PropertyValueDoc(configEntry));
                    }
                }
            } // end for prefix
        } // end while lines
        br.close();
    }
}
