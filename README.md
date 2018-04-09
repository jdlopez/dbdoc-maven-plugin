# dbdoc maven plugin

A maven plugin that builds documentation from jdbc source. Now (1.2) added config file entries documentation from source 

* Uses JSON file to add comments and extra documentation for tables or columns.
* Uses mustache templates. [Basic HTML template](src/main/resources/template-html.mustache)
* v1.2 Config entries documentation. Very simple search for patterns in files (getProperty ... configurable)
* v1.3 Minor fix (deleted tables and columns)

Usage example [here](src/it/simple-it/pom.xml)

>Check target/site generated files
>
>It tests database with [hsqldb](http://hsqldb.org/)

## Deployment to OSS sonatype and then to central howto

    mvn clean deploy -e -P ossrh

Usefull guides to upload artifact to central or github:

 * http://central.sonatype.org/pages/ossrh-guide.html
 * https://github.com/github/maven-plugins#readme
    
Always check staging rules!:

    http://central.sonatype.org/pages/requirements.html