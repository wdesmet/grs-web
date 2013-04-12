<!DOCTYPE html>
<html>
    <head>
        <title>Genomic Rosetta Stone</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="/style/styles.css" />
    </head>
    <body>
        <div id="main">
        <h1>Genomic Rosetta Stone</h1>
            <h2>Intro</h2>
            <p>All sorts of genome-related information (phenotypic, materials, analysis) is scattered
            over various different services on the net. Any monolithic service
            that re-integrates all this data must deal with various issues such as data consistency,
            data staleness, availability, etc. This part of the application building is much better left
            over to those that actually need the data, as different users and different implementers will
            have varying needs. However, these users need an easy way to discover
            all available data for a particular full genome. The
            <a href="http://gensc.org/gc_wiki/index.php/Genomic_Rosetta_Stone">Genomic Rosetta Stone</a>
            allows discovery of all this related information, for any genomes currently assigned an
            NCBI genome project identifier.</p>

            <p>This endpoint allows users to query for a list of genome project
            identifiers, the mappings of those identifiers onto specific identifiers of each data provider,
            and some assorted information on those providers. Data providers and the underlying data set are
            registered with the NCBI LinkOut system, which can also be queried directly. This service however
            allows you to provide an identifier from any data provider currently in the system, and map it
            back to a list of identifiers under which related data is held by other providers.</p>

            <h2>Usage</h2>
            <p>There are several hierarchies. The <a href="/providers"><code>/providers</code></a>
            hierarchy allows querying for data provider information. This is an easy shortcut when
            looking for abbreviations or a list of common resources. The
            <a href="/mappings"><code>/mappings</code></a> hierarchy provides all mappings from a
            particular data provider identifier to genome projects.	Querying those top level URLs
             produces a full list of mappings (from a provider ID to genome project ID) and
            data providers (currently all the LinkOut providers) stored in the system.
            The final hierarchy is that of genome projects, found under
            <a href="/projects"><code>/projects</code></a>. This way, all project identifiers can be
            found, and a list of mappings for each project identifier can be queried. This provides
            the same forward mapping as available in the LinkOut system, but with a more specialized
            and simpler API.</p>

            <p>Output format is chosen based on HTTP content autonegotiation, i.e.
            the application	will check the <code>Accept</code> header your applications
            sends with the request and act accordingly. If no <code>Accept</code> header is given,
            the default format (i.e. plain text or XML) is assumed.
            <p>Using curl, we can illustrate this concept more clearly. Here's a simple request
            for all mappings:</p>
            <pre><code>curl <a href="/mappings">http://grs.straininfo.net/mappings</a></code></pre>

            <p>Since no <code>Accept</code> header was specified, the output will be plain text:</p>
            <pre><samp># source (abbr) source_id genome_project_id link_name                       url
StrainInfo      110659    4                 ATCC 35405 (strain passport)    http://www.straininfo.net/strains/110659
[...]</samp></pre>

            <p>We can query just the first record too, in this case requesting XML:</p>
            <pre><code>curl -H 'Accept: text/xml' <a href="/mappings/straininfo/110659">http://grs.straininfo.net/mappings/straininfo/110659</a> | xmllint --format -</code></pre>
            <p>Which outputs:</p>
            <pre><samp>&lt;mappings&gt;
  &lt;mapping&gt;
    &lt;url&gt;http://www.straininfo.net/strains/110659&lt;/url&gt;
    &lt;subjectType&gt;culture/stock collections&lt;/subjectType&gt;
    &lt;linkName&gt;ATCC 35405 (strain passport)&lt;/linkName&gt;
    &lt;category&gt;Research Materials&lt;/category&gt;
    &lt;sourceId&gt;110659&lt;/sourceId&gt;
    &lt;providerId&gt;6685&lt;/providerId&gt;
    &lt;projectId&gt;4&lt;/projectId&gt;
  &lt;/mapping&gt;
&lt;/mappings&gt;</samp></pre>

            <p> Likewise:
            <pre><code>curl -H 'Accept: text/plain' <a href="/projects/3">http://grs.straininfo.net/projects/3</a></code></pre>
            will return all mappings associated with genome project identifier &quot;3&quot;.</p>
            <p>Since content negotiation is only really useful programmatically, and we often
            want to check the output in our browser, where overwriting it is hard word, there's
            an easy shortcut that allows you to select the required format by providing an extension.
            For XML output, simply add <kbd>.xml</kbd> to the end of the requested URL. To query the
            record above, all you need is to head to the following URL:</p>
            <pre><code><a href="/mappings/straininfo/110659.xml">http://grs.straininfo.net/mappings/straininfo/110659.xml</a></code></pre>
            <p>Likewise, you get results in the required format by using <kbd>.txt</kbd> or <kbd>.json</kbd>.
            <h2>See also</h2>
            <ul>
                <li><a href="http://gensc.org/">Genomic Standards Consortium</a></li>
                <li><a href="http://www.ncbi.nlm.nih.gov/sites/entrez">Entrez Genome Project</a></li>
            </ul>
            <h2>Questions/Remarks/Bugs?</h2>
            <p>Mail the author at: </p><address>Wim dot De Smet at UGent dot be</address>
        </div>
    </body>
</html>