package net.straininfo2.grs.dao;

import net.straininfo2.grs.bioproject.BioProject;
import net.straininfo2.grs.dto.ComparisonData;
import net.straininfo2.grs.mashup.MashupLogic;
import net.straininfo2.grs.resource.ComparisonResource;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class MashupServiceImpl implements MashupService {

    @Autowired
    MegxGenomeService megxService;

    @Autowired
    BioProjectService genomeService;

    @Autowired
    StraininfoService straininfoService;

    @Override
    @Transactional
    public List<Object> getCrossrefData(long genomeId, ComparisonResource comparisonResource) throws JSONException {
        BioProject project = genomeService.findBioProject(genomeId); // check if Bioproject exists
        if (project == null) {
            throw new IllegalArgumentException("Bioproject with given id does not exist");
        }
        final Map<String, String> megxData = megxService.getMegxJson(genomeId);
        final Map<String, String> ncbiData = genomeService.getAssociatedInformation(genomeService.findBioProject(genomeId));
        final Map<String, String> straininfoData = straininfoService.getStrainInfoCultureData(genomeId);
        List<Map<String, String>> data = new LinkedList<Map<String, String>>();
        data.add(megxData);
        data.add(ncbiData);
        data.add(straininfoData);
        MashupLogic logic = new MashupLogic(Arrays.asList("Megx", "NCBI", "StrainInfo"), data);
        List<Object> results = new ArrayList<>();
        results.add(logic);
        Map<String, String> displayValues = new HashMap<>();
        displayValues.put("id", ""+genomeId);
        displayValues.put("Megx", megxService.constructGenomeprojectQuery(genomeId).toString());
        displayValues.put("NCBI", "http://www.ncbi.nlm.nih.gov/bioproject/" + genomeId);
        List<Integer> ids = straininfoService.findCultureIds(genomeId);
        // usually only one
        if (ids.size() > 0) {
            displayValues.put("StrainInfo", "http://www.straininfo.net/strains/" + ids.get(0));
        }
        results.add(displayValues);
        return results;
    }

    @Override
    public ComparisonData getComparisonData(long genomeId, ComparisonResource comparisonResource) throws JSONException {
        ComparisonData data = new ComparisonData();
        data.setMegx(megxService.getMegxJson(genomeId));
        // cheating a little bit by giving NCBI a provider ID
        data.setNcbi(genomeService.getAssociatedInformation(genomeService.findBioProject(genomeId)));
        data.setStrainInfo(straininfoService.getStrainInfoCultureData(genomeId));
        return data;
    }
}
