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
        knownMappings.put("motility", "motility");
        knownMappings.put("mcl:motile", "motility");
        knownMappings.put("mcl:sampleHabitat", "habitat");
        knownMappings.put("habitat", "habitat");
        knownMappings.put("mcl:strainNumber", "strainnumbers");
        knownMappings.put("mcl:otherStrainNumber", "strainnumbers");
        knownMappings.put("mcl:otherStrainNumbers", "strainnumbers");
        knownMappings.put("strain", "strainnumbers");
        knownMappings.put("shapes", "shapes");
        knownMappings.put("mcl:cellShape", "shapes");
        knownMappings.put("isolate", "isolate");
        knownMappings.put("mcl:sampleCultureStrainNumber", "isolate");
        knownMappings.put("mcl:gramReaction", "gramreaction");
        knownMappings.put("gram_strain", "gramreaction");
        knownMappings.put("endospores", "sporeforming");
        knownMappings.put("mcl:sporeForming", "sporeforming");
        knownMappings.put("mcl:growthTemperature", "growthtemperature");
        knownMappings.put("temperature_range", "growthtemperature");
        knownMappings.put("optimal_temperature", "optimaltemperature");
        knownMappings.put("mcl:optimalGrowthTemperature", "optimaltemperature");
        knownMappings.put("mcl:oxygenRelationship", "oxygenrequirement");
        knownMappings.put("oxygen_requirement", "oxygenrequirement");
        knownMappings.put("culture_sample", "hasculture");
        knownMappings.put("mcl:hasCulture", "hasculture");
        //rest
        knownMappings.put("mcl:alkalinePhosphataseReaction", "alkalinePhosphataseReaction");
        knownMappings.put("mcl:betaGalactosidaseReaction", "betaGalactosidaseReaction");
        knownMappings.put("mcl:catalaseReaction", "catalaseReaction");
        knownMappings.put("mcl:catalogVersion", "catalogVersion");
        knownMappings.put("mcl:cellGcContent", "cellGcContent");
        knownMappings.put("mcl:cellLength", "cellLength");
        knownMappings.put("mcl:cellSize", "cellSize");
        knownMappings.put("mcl:cellWidth", "cellWidth");
        knownMappings.put("mcl:colonyShape", "colonyShape");
        knownMappings.put("mcl:comments", "comments");
        knownMappings.put("mcl:cultureLastUpdateDate", "cultureLastUpdateDate");
        knownMappings.put("mcl:cytochromeOxidaseReaction", "cytochromeOxidaseReaction");
        knownMappings.put("mcl:environmentPublication", "environmentPublication");
        knownMappings.put("mcl:growthPH", "growthPH");
        knownMappings.put("mcl:hasSample", "sampleID");
        knownMappings.put("mcl:history", "history");
        knownMappings.put("mcl:historyPublication", "historyPublication");
        knownMappings.put("mcl:id", "id");
        knownMappings.put("mcl:indoleReaction", "indoleReaction");
        knownMappings.put("mcl:inhibitedBy", "inhibitedBy");
        knownMappings.put("mcl:isolatedFromSample", "isolatedFromSample");
        knownMappings.put("mcl:isolationDate", "isolationDate");
        knownMappings.put("mcl:isolationMethod", "isolationMethod");
        knownMappings.put("mcl:isolationSample", "isolationSample");
        knownMappings.put("mcl:isolator", "isolator");
        knownMappings.put("mcl:isolatorInstitute", "isolatorInstitute");
        knownMappings.put("mcl:maximalGrowthTemperature", "maximalGrowthTemperature");
        knownMappings.put("mcl:maximumGrowthNACL", "maximumGrowthNACL");
        knownMappings.put("mcl:maximumGrowthPH", "maximumGrowthPH");
        knownMappings.put("mcl:mediumDescription", "mediumDescription");
        knownMappings.put("mcl:mediumName", "mediumName");
        knownMappings.put("mcl:mediumNumber", "mediumNumber");
        knownMappings.put("mcl:mediumURL", "mediumURL");
        knownMappings.put("mcl:minimalGrowthTemperature", "minimalGrowthTemperature");
        knownMappings.put("mcl:minimumGrowthNACL", "minimumGrowthNACL");
        knownMappings.put("mcl:minimumGrowthPH", "minimumGrowthPH");
        knownMappings.put("mcl:motileBy", "motileBy");
        knownMappings.put("mcl:noGrowthNACL", "noGrowthNACL");
        knownMappings.put("mcl:noGrowthPH", "noGrowthPH");
        knownMappings.put("mcl:noGrowthTemperature", "noGrowthTemperature");
        knownMappings.put("mcl:nomenclaturalPublication", "nomenclaturalPublication");
        knownMappings.put("mcl:notInhibitedBy", "notInhibitedBy");
        knownMappings.put("mcl:notUtilizes", "notUtilizes");
        knownMappings.put("mcl:optimalGrowthNACL", "optimalGrowthNACL");
        knownMappings.put("mcl:optimalGrowthPH", "optimalGrowthPH");
        knownMappings.put("mcl:oxidaseReaction", "oxidaseReaction");
        knownMappings.put("mcl:preservationPublication", "preservationPublication");
        knownMappings.put("mcl:publication", "publication");
        knownMappings.put("mcl:qualifiedSpeciesName", "qualifiedSpeciesName");
        knownMappings.put("mcl:recommendMedium", "recommendMedium");
        knownMappings.put("mcl:sampleAlt", "sampleAlt");
        knownMappings.put("mcl:sampleCollector", "sampleCollector");
        knownMappings.put("mcl:sampleCollectorInstitute", "sampleCollectorInstitute");
        knownMappings.put("mcl:sampleCulture", "sampleCulture");
        knownMappings.put("mcl:sampleDate", "sampleDate");
        knownMappings.put("mcl:sampleDescription", "sampleDescription");
        knownMappings.put("mcl:sampleHabitatEnvoTerm", "sampleHabitatEnvoTerm");
        knownMappings.put("mcl:sampleLat", "sampleLat");
        knownMappings.put("mcl:sampleLocationCountry", "sampleLocationCountry");
        knownMappings.put("mcl:sampleLocationDescription", "sampleLocationDescription");
        knownMappings.put("mcl:sampleLocationPlace", "sampleLocationPlace");
        knownMappings.put("mcl:sampleLong", "sampleLong");
        knownMappings.put("mcl:significantFattyAcid", "significantFattyAcid");
        knownMappings.put("mcl:significantPolarLipid", "significantPolarLipid");
        knownMappings.put("mcl:significantQuinone", "significantQuinone");
        knownMappings.put("mcl:taxonomicPublication", "taxonomicPublication");
        knownMappings.put("mcl:typeStrainOf", "typeStrainOf");
        knownMappings.put("mcl:typeStrainOfGenus", "typeStrainOfGenus");
        knownMappings.put("mcl:typeStrainOfSpecies", "typeStrainOfSpecies");
        knownMappings.put("mcl:ureaseReaction", "ureaseReaction");
        knownMappings.put("mcl:utilizes", "utilizes");
        knownMappings.put("accession", "accession");
        knownMappings.put("archive", "archive");
        knownMappings.put("tax_id", "tax_id");
        knownMappings.put("species_id", "species_id");
        knownMappings.put("breed", "breed");
        knownMappings.put("cultivar", "cultivar");
        knownMappings.put("genome_size", "genome_size");
        knownMappings.put("supergroup", "supergroup");
        knownMappings.put("organization", "organization");
        knownMappings.put("isolated_cell", "isolated_cell");
        knownMappings.put("tissue_sample", "tissue_sample");
        knownMappings.put("salinity", "salinity");
        knownMappings.put("enveloped", "enveloped");
        knownMappings.put("disease", "disease");
        knownMappings.put("biotic_relationship", "biotic_relationship");
        knownMappings.put("trophic_level", "trophic_level");
        knownMappings.put("locustag_prefixes", "locustag_prefixes");
        knownMappings.put("description", "project_description");
        knownMappings.put("name", "project_name");
        knownMappings.put("title", "project_title");
    }

    private Map<String, String[]> consolidatedData = new HashMap<>();

    public MashupLogic(List<String> providerNames, List<Map<String, String>> providerData) {
        assert providerData.size() == providerNames.size();
        this.providerNames = providerNames;
        this.providerData = providerData;
        computeMaps();
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
