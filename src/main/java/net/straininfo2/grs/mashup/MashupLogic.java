package net.straininfo2.grs.mashup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Separate class for the algorithm that mashes up the data from various different providers.
 * It marks an entry as being conflicting if two of the data sources disagree, no matter what
 * the third thinks. Other implementations are possible, but this is the easiest to display
 * and represent.
 *
 * There are some known label mappings in this class for Megx, StrainInfo and NCBI. If other
 * providers are used, they should use the same labels, or knowledge of their labels should
 * be added.
 *
 * This class is not thread-safe.
 */
public class MashupLogic {

    private final static Logger logger = LoggerFactory.getLogger(MashupLogic.class);

    /* for input */
    private List<String> providerNames;

    private List<Map<String, String>> providerData;

    /* result of running the mashup logic */
    private List<String> labelsWithConflicts = new ArrayList<>();

    private List<String> labelsWithMatching = new ArrayList<>();

    private List<String> labelsUnique = new ArrayList<>();

    private Map<String, String> labelMapping = new HashMap<>();

    // known labels for the services,
    private final static Map<String, String> knownMappings;

    static {
        knownMappings = new HashMap<>();
        // matching
        // see http://www.straininfo.net/schema/2.0/si-base.xsd and ftp://ftp.ncbi.nlm.nih.gov/bioproject/Schema/Core.xsd
        knownMappings.put("organism_name", "organism");
        knownMappings.put("mcl:speciesName", "organism");
        knownMappings.put("isolate_name", "organism");
        knownMappings.put("motility", "motility");
        knownMappings.put("mcl:motile", "motility");
        knownMappings.put("mcl:sampleHabitat", "habitat");
        knownMappings.put("habitat", "habitat");
        knownMappings.put("biome", "habitat");
        knownMappings.put("mcl:strainNumber", "strain numbers");
        knownMappings.put("mcl:otherStrainNumber", "strain numbers");
        knownMappings.put("mcl:otherStrainNumbers", "strain numbers");
        knownMappings.put("strain", "strain numbers");
        knownMappings.put("shapes", "shapes");
        knownMappings.put("mcl:cellShape", "shapes");
        knownMappings.put("cell_shape", "shapes");
        knownMappings.put("isolate", "isolate");
        knownMappings.put("mcl:sampleCultureStrainNumber", "isolate");
        knownMappings.put("mcl:gramReaction", "gram reaction");
        knownMappings.put("gram_strain", "gram reaction");
        knownMappings.put("gram_stain", "gram reaction");
        knownMappings.put("endospores", "spore forming");
        knownMappings.put("mcl:colonyShape", "colony shape");
        knownMappings.put("cell_arrange", "colony shape");
        knownMappings.put("salinity", "salinity");
        knownMappings.put("salinity_class", "salinity");
        knownMappings.put("sporulation", "spore forming");
        knownMappings.put("mcl:sporeForming", "spore forming");
        knownMappings.put("mcl:growthTemperature", "growth temperature");
        knownMappings.put("temperature_class", "growth temperature");
        knownMappings.put("temperature_range", "temperature range");
        knownMappings.put("optimal_temperature", "optimal temperature");
        knownMappings.put("mcl:optimalGrowthTemperature", "optimal temperature");
        knownMappings.put("mcl:oxygenRelationship", "oxygen requirement");
        knownMappings.put("oxygen_requirement", "oxygen requirement");
        knownMappings.put("oxygen_class", "oxygen requirement");
        knownMappings.put("culture_sample", "has culture");
        knownMappings.put("mcl:hasCulture", "has culture");
        knownMappings.put("mcl:id", "culture ID");
        knownMappings.put("straininfo", "culture ID");
        knownMappings.put("tax_id", "taxon ID");
        knownMappings.put("taxid", "taxon ID");
        knownMappings.put("mcl:sampleLat", "latitude");
        knownMappings.put("lat", "latitude");
        knownMappings.put("lon", "longitude");
        knownMappings.put("mcl:sampleLong", "sampleLong");
        knownMappings.put("mcl:growthPH", "pH range");
        knownMappings.put("ph_range", "pH range");
        knownMappings.put("energy_source", "energy source");
        knownMappings.put("trophic_level", "trophic level"); // TODO: do these have the same def?
        //rest
        knownMappings.put("site_name", "site name");
        knownMappings.put("lat_lon", "geographic location");
        knownMappings.put("collection_date", "collection date");
        knownMappings.put("date_res", "date resolution");
        knownMappings.put("sample_name", "sample name");
        knownMappings.put("material", "material");
        knownMappings.put("geo_loc_name", "location name");
        knownMappings.put("genus", "genus");
        knownMappings.put("species", "species");
        knownMappings.put("type_strain", "type strain");
        knownMappings.put("subspecies", "subspecies");
        knownMappings.put("serovar", "serovar");
        knownMappings.put("num_chromosomes", "chromosomes");
        knownMappings.put("num_plasmids", "plasmids");
        knownMappings.put("num_replicons", "replicons");
        knownMappings.put("culture_collection_label", "culture collection label");
        knownMappings.put("descr_short", "title");
        knownMappings.put("abstract", "description");
        knownMappings.put("gpid", "project ID");
        knownMappings.put("goldstamp", "goldstamp");
        knownMappings.put("gcat_id", "gcat ID");
        knownMappings.put("img_oid", "img OID");
        knownMappings.put("seq_center", "sequencing center");
        knownMappings.put("resequencing", "resequencing");
        knownMappings.put("finishing_strategy", "finishing strategy");
        knownMappings.put("seq_ids", "sequences");
        knownMappings.put("investigation_type", "investigation type");
        knownMappings.put("submitted_to_insdc","submitted to INSDC");
        knownMappings.put("mcl:alkalinePhosphataseReaction", "alkaline phosphatase reaction");
        knownMappings.put("mcl:betaGalactosidaseReaction", "beta-galactosidase reaction");
        knownMappings.put("mcl:catalaseReaction", "catalase reaction");
        knownMappings.put("mcl:catalogVersion", "catalog version");
        knownMappings.put("mcl:cellGcContent", "cell GC content");
        knownMappings.put("mcl:cellLength", "cell length");
        knownMappings.put("mcl:cellSize", "cell size");
        knownMappings.put("mcl:cellWidth", "cell width");
        knownMappings.put("mcl:comments", "comments");
        knownMappings.put("mcl:cultureLastUpdateDate", "culture last update date");
        knownMappings.put("mcl:cytochromeOxidaseReaction", "cytochrome oxidase reaction");
        knownMappings.put("mcl:environmentPublication", "environment publication");
        knownMappings.put("mcl:hasSample", "sample ID");
        knownMappings.put("mcl:history", "history");
        knownMappings.put("mcl:historyPublication", "history publication");
        knownMappings.put("mcl:indoleReaction", "indole reaction");
        knownMappings.put("mcl:inhibitedBy", "inhibited by");
        knownMappings.put("mcl:isolatedFromSample", "isolated from sample");
        knownMappings.put("mcl:isolationDate", "isolation date");
        knownMappings.put("mcl:isolationMethod", "isolation method");
        knownMappings.put("mcl:isolationSample", "isolation sample");
        knownMappings.put("mcl:isolator", "isolator");
        knownMappings.put("mcl:isolatorInstitute", "isolator institute");
        knownMappings.put("mcl:maximalGrowthTemperature", "maximal growth temperature");
        knownMappings.put("mcl:maximumGrowthNACL", "maximum growth NaCl");
        knownMappings.put("mcl:maximumGrowthPH", "maximum growth pH");
        knownMappings.put("mcl:mediumDescription", "medium description");
        knownMappings.put("mcl:mediumName", "medium name");
        knownMappings.put("mcl:mediumNumber", "medium number");
        knownMappings.put("mcl:mediumURL", "medium URL");
        knownMappings.put("mcl:minimalGrowthTemperature", "minimal growth temperature");
        knownMappings.put("mcl:minimumGrowthNACL", "minimum growth NaCl");
        knownMappings.put("mcl:minimumGrowthPH", "minimum growth pH");
        knownMappings.put("mcl:motileBy", "motile by");
        knownMappings.put("mcl:noGrowthNACL", "no growth NaCl");
        knownMappings.put("mcl:noGrowthPH", "no growth pH");
        knownMappings.put("mcl:noGrowthTemperature", "no growth temperature");
        knownMappings.put("mcl:nomenclaturalPublication", "nomenclatural publication");
        knownMappings.put("mcl:notInhibitedBy", "not inhibited by");
        knownMappings.put("mcl:notUtilizes", "does not utilize");
        knownMappings.put("mcl:optimalGrowthNACL", "optimal growth NaCl");
        knownMappings.put("mcl:optimalGrowthPH", "optimal growth pH");
        knownMappings.put("mcl:oxidaseReaction", "oxidase reaction");
        knownMappings.put("mcl:preservationPublication", "preservation publication");
        knownMappings.put("mcl:publication", "publication");
        knownMappings.put("mcl:qualifiedSpeciesName", "qualified species name");
        knownMappings.put("mcl:recommendMedium", "recommend medium");
        knownMappings.put("mcl:sampleAlt", "sample alt");
        knownMappings.put("mcl:sampleCollector", "sample collector");
        knownMappings.put("mcl:sampleCollectorInstitute", "sample collector institute");
        knownMappings.put("mcl:sampleCulture", "sample culture");
        knownMappings.put("mcl:sampleDate", "sample date");
        knownMappings.put("mcl:sampleDescription", "sample description");
        knownMappings.put("mcl:sampleHabitatEnvoTerm", "sample habitat EnvoTerm");
        knownMappings.put("mcl:sampleLocationCountry", "sample location country"); // todo: check overlap for location
        knownMappings.put("mcl:sampleLocationDescription", "sample location description");
        knownMappings.put("mcl:sampleLocationPlace", "sample location place");
        knownMappings.put("mcl:significantFattyAcid", "significant fatty acid");
        knownMappings.put("mcl:significantPolarLipid", "significant polar lipid");
        knownMappings.put("mcl:significantQuinone", "significant quinone");
        knownMappings.put("mcl:taxonomicPublication", "taxonomic publication");
        knownMappings.put("mcl:typeStrainOf", "type strain of");
        knownMappings.put("mcl:typeStrainOfGenus", "type strain of genus");
        knownMappings.put("mcl:typeStrainOfSpecies", "type strain of species");
        knownMappings.put("mcl:ureaseReaction", "urease reaction");
        knownMappings.put("mcl:utilizes", "utilizes");
        knownMappings.put("accession", "accession");
        knownMappings.put("archive", "archive");
        knownMappings.put("species_id", "species ID");
        knownMappings.put("breed", "breed");
        knownMappings.put("cultivar", "cultivar");
        knownMappings.put("genome_size", "genome_size");
        knownMappings.put("supergroup", "supergroup");
        knownMappings.put("organization", "organization");
        knownMappings.put("isolated_cell", "isolated cell");
        knownMappings.put("tissue_sample", "tissue sample");
        knownMappings.put("enveloped", "enveloped");
        knownMappings.put("disease", "disease");
        knownMappings.put("biotic_relationship", "biotic relationship");
        knownMappings.put("locustag_prefixes", "locustag prefixes");
        knownMappings.put("description", "project description");
        knownMappings.put("name", "project name");
        knownMappings.put("title", "project title");
    }

    private Map<String, String[]> consolidatedData = new HashMap<>();

    public MashupLogic(List<String> providerNames, List<Map<String, String>> providerData) {
        assert providerData.size() == providerNames.size();
        this.providerNames = providerNames;
        this.providerData = providerData;
        cleanupData();
        computeMaps();
    }

    private void cleanupData() {
        // makes sure everything uses about the same values for terms, otherwise nothing will match
        Iterator<Map<String, String>> it = providerData.iterator();
        for (String name : providerNames) {
            Map<String, String> providerData = it.next();
            if (name.equalsIgnoreCase("megx")) {
                for (Map.Entry<String, String> entry : new HashMap<>(providerData).entrySet()) {
                    if (entry.getValue().equals("infinity") || entry.getValue().equals("-1")) {
                        providerData.remove(entry.getKey());
                    }
                    else if (entry.getValue().equals("t")) {
                        providerData.put(entry.getKey(), "yes");
                    }
                    else if (entry.getValue().equals("f")) {
                        providerData.put(entry.getKey(), "no");
                    }
                    else if (entry.getKey().equals("motility")) {
                        providerData.put(entry.getKey(), entry.getValue().equalsIgnoreCase("motile") ? "yes" : "no");
                    }
                }
            }
        }
    }

    public void computeMaps() {
        // first add missing keys to mappings
        labelMapping.putAll(knownMappings);
        Set<String> keys = new HashSet<>();
        for (Map<String, String> data : providerData) {
            keys.addAll(data.keySet());
        }
        keys.removeAll(knownMappings.keySet());
        for (String key : keys) {
            labelMapping.put(key, key);
        }
        // then, iterate over all the data, put everything in the main map
        int index = 0;
        for (Map<String, String> data : providerData) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = labelMapping.get(entry.getKey());
                String[] curData = consolidatedData.get(key);
                if (curData == null) {
                    curData = new String[providerData.size()];
                }
                if (curData[index] == null) {
                    curData[index] = entry.getValue();
                }
                else {
                    curData[index] += "," + entry.getValue();
                }
                consolidatedData.put(key, curData);
            }
            index++;
        }
        // once the map is filled, compute what is unique, what is matching, different, etc.
        for (Map.Entry<String, String[]> entry : consolidatedData.entrySet()) {
            int notNull = countNotNull(entry.getValue());
            if (notNull > 0) {
                if (notNull == 1) {
                    labelsUnique.add(entry.getKey());
                }
                else {
                    String cur = null;
                    boolean equal = true;
                    for (String elem : entry.getValue()) {
                        if (elem == null) {
                            continue;
                        }
                        if (cur == null) {
                            cur = elem;
                        }
                        else {
                            equal = equal && cur.equalsIgnoreCase(elem);
                        }
                    }
                    if (equal) {
                        labelsWithMatching.add(entry.getKey());
                    }
                    else {
                        labelsWithConflicts.add(entry.getKey());
                    }
                }
            }
            else {
                logger.warn("Consolidated data entry added without any elements for key: {}", entry.getKey());
            }
        }
        // sort everything alphabeticall
        Collections.sort(labelsWithConflicts);
        Collections.sort(labelsWithMatching);
        Collections.sort(labelsUnique);
    }

    private int countNotNull(String[] data) {
        int count = 0;
        for (String elem : data) {
            if (elem != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a list of custom data labels, each of which maps to some data
     * point within the original provider data.
     *
     * @return list of labels for public display
     */
    public List<String> getDataLabels() {
        return new ArrayList<>(labelMapping.values());
    }

    /**
     * Returns how the original labels are mapped. The keys in this map are
     * the labels as they appear in the original collections passed in, the
     * values is the data label that is used for output. This may be the same
     * as the input label or a generalised label stored internally.
     *
     * @return mapping of original label names to output label names.
     */
    public Map<String, String> getLabelMapping() {
        return new HashMap<>(labelMapping);
    }

    public Map<String, String[]> getData() {
        return consolidatedData;
    }

    /**
     * Returns an array with all the data for a particular data label. There
     * are as many entries as there are providers. The label should be one
     * present in the list from #getDataLabels().
     */
    public String[] getData(String label) {
        return consolidatedData.get(label);
    }

    public List<String> getLabelsWithConflicts() {
        return labelsWithConflicts;
    }

    public List<String> getLabelsUniques() {
        return labelsUnique;
    }

    public List<String> getLabelsMatching() {
        return labelsWithMatching;
    }

    public List<String> getProviderNames() {
        return providerNames;
    }
}
