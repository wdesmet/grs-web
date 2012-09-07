Genomic Rosetta Stone: Web Application
--------------------------------------
This simple web application provides an easy way to download all the linked
records for different data providers involved in the GRS project[1] that have
registered their identifiers with LinkOut. The master version tracks
the official GRS web application[2].

Usage
=====
The application is deployed like any java web application, so:

    $ cd grs-web/
    $ mvn package
    $ cp target/grs.war /path/to/your/application/server

It expects the database to be provider under JNDI name `jdbc/grsdb`. If you're
using tomcat and the apache derby database downloaded by gpdler[3], you can
configure that database with the following snippet in your context.xml file
(or web.xml in older versions):

    <Resource name="jdbc/grsdb" type="javax.sql.DataSource"
        driverClassName="org.apache.derby.jdbc.EmbeddedDriver"
        url="jdbc:derby:/path/to/grsdb"
        singleton="true" auth="Container" username="grs"
        initialSize="1" maxActive="4" />

Make sure to add derby.jar[4] to your lib/ directory before running.

[1]:http://gensc.org/gc_wiki/index.php/Genomic_Rosetta_Stone
[2]:http://grs.straininfo.net
[3]:https://github.com/wdesmet/gpdler
[4]:http://db.apache.org/derby/derby_downloads.html