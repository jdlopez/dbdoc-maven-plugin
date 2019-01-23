# dbdoc maven plugin

A maven plugin that builds documentation from jdbc source. Now (1.2) added config file entries documentation from source 

* Uses JSON file to add comments and extra documentation for tables or columns.
* Uses mustache templates. [Basic HTML template](src/main/resources/template-html.mustache)
* v1.2 Config entries documentation. Very simple search for patterns in files (getProperty ... configurable)
* v1.3 Minor fix (deleted tables and columns)

Full usage example [here](src/it/simple-it/pom.xml)

Basic example in pom.xml:

                <plugin>
                <groupId>io.github.jdlopez</groupId>
                <artifactId>dbdoc-maven-plugin</artifactId>
                <version>1.3</version>
                <configuration>
                    <jdbcDriver>${jdbc.driver}</jdbcDriver>
                    <jdbcUrl>${jdbc.url}</jdbcUrl>
                    <jdbcUser>${jdbc.user}</jdbcUser>
                    <jdbcPass>${jdbc.password}</jdbcPass>
                    <cfgOverwriteSource>true</cfgOverwriteSource>
                    <overwriteSource>false</overwriteSource>
                </configuration>
                <!-- add jdbc driver depends -->
                <dependencies>
                    <dependency>
                        <groupId>org.hsqldb</groupId>
                        <artifactId>hsqldb</artifactId>
                        <version>2.4.0</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <id>dbdoc</id>
                        <phase>site</phase>
                        <goals>
                            <goal>dbdoc</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>dbconfig</id>
                        <phase>site</phase>
                        <goals>
                            <goal>configdoc</goal>
                        </goals>
                    </execution>
                </executions>

>Check target/site generated files
>
>It tests database with [hsqldb](http://hsqldb.org/)

