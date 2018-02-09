package io.github.maven;

import io.github.maven.domain.ConfigValuesDoc;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 * Goal which creates configdoc files
 */
@Mojo( name = "configdoc", defaultPhase = LifecyclePhase.SITE )
public class ConfigDoc extends AbstractMojo {
    /** Output dir for documentation*/
    @Parameter( defaultValue = "${project.build.directory}/site", property = "outputDir", required = true )
    private File outputDirectory;
    /** Input source file. Created if not exists */
    @Parameter( defaultValue = "${project.basedir}/src/site/${project.artifactId}-configdoc.json", property = "sourceFile", required = false )
    private File sourceFile;
    /** Input source directory */
    @Parameter( defaultValue = "${project.basedir}/src", property = "sourceScanDirectory", required = false )
    private File sourceScanDirectory;
    /** Pattern to search. Use asterix to point variable name */
    @Parameter( defaultValue = ".getProperty(\"*\")", property = "patterns", required = false )
    private List<String> patterns;

    public void execute() throws MojoExecutionException, MojoFailureException {
        ConfigValuesDoc documentation;
        //scanDirectory()
    }
}
