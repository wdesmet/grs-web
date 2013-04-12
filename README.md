Genomic Rosetta Stone: Resolver
-------------------------------

The GRS Resolver provides an easy way to download all the linked records for
different data providers involved in the [GRS project] [1] that have registered
their identifiers with LinkOut. The master version tracks the [official GRS web
application] [2].

Usage
=====
The application is deployed like any java web application, so:

    $ cd grs-web/
    $ mvn package
    $ cp target/grs.war /path/to/your/application/server

It expects the database to be provided under JNDI name `jdbc/grsdb`. It should
be in the layout defined by [gpdler] [3]. If you already have the embedded
database created by gpdler, there is a shortcut to get up and running, namely
drop the file (default "grsdb2.h2.db") in the grs-web directory and run:

    $ mvn jetty:run

When deploying in a web container or application server, provide the JNDI name
by configuring a database instance in your container. For instance, for Tomcat
you can configure the you can configure that database with the following
snippet in your context.xml file (or web.xml in older versions):

    <Resource name="jdbc/grsdb" type="javax.sql.DataSource"
        driverClassName="org.h2.Driver"
        url="jdbc:h2:/path/to/grsdb2"
        singleton="true" auth="Container" username="grs"
        initialSize="1" maxActive="4" />

Make sure to add [h2.jar] [4] to your lib/ directory before running.

[1]:http://gensc.org/gc_wiki/index.php/Genomic_Rosetta_Stone
[2]:http://grs.straininfo.net
[3]:https://github.com/wdesmet/gpdler
[4]:http://www.h2database.com/html/download.html
