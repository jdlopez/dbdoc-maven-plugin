# Deployment to OSS sonatype and then to central howto

    mvn clean deploy -e -P ossrh

Usefull guides to upload artifact to central or github:

 * http://central.sonatype.org/pages/ossrh-guide.html
 * https://github.com/github/maven-plugins#readme
    
Always check staging rules!:

    http://central.sonatype.org/pages/requirements.html
