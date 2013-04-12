package net.straininfo2.grs.dao;

import com.sun.jersey.api.client.WebResource;
import net.straininfo2.grs.bioproject.mappings.Mapping;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.MediaType;
import java.util.*;

public class StraininfoService {

    private WebResource straininfo; // points to a StrainInfo endpoint for JSON culture data

    @Autowired
    private BioProjectService projectService;

    private static Logger logger = LoggerFactory.getLogger(StraininfoService.class);

    public Map<String, String> getStrainInfoCultureData(long genomeProjectId) throws JSONException {
        // this basically is just internally doing the same as querying /projects
        // we should consider adding the ncbi bioproject as a special type of provider as well
        List<Integer> cultureIds = new LinkedList<Integer>();
        for (Mapping mapping : projectService.findFullGenomeProject(genomeProjectId).getMappings()) {
            if (mapping.getProvider().getAbbr().equalsIgnoreCase("straininfo")) {
                cultureIds.add(Integer.parseInt(mapping.getTargetId()));
            }
        }
        logger.info("Found {} culture IDs in StrainInfo for genome project {}", cultureIds.size(), genomeProjectId);
        // TODO: the jackson json library would be preferrable here
        Map<String, String> result = new HashMap<String, String>();
        // values for later culture IDs currently overwrite those of previous
        for (int id : cultureIds) {
            JSONObject obj = straininfo.queryParam("cultureId", "" + id).accept(
                    MediaType.APPLICATION_JSON_TYPE).get(
                    JSONObject.class);
            Iterator<String> it = obj.keys();
            logger.info("Adding keys from id {}.", id);
            while (it.hasNext()) {
                String key = it.next();
                Object value = obj.get(key);
                if (obj.get(key) != JSONObject.NULL) {
                    logger.debug("Adding JSON value from StrainInfo for key {}", key);
                    if (value instanceof String) {
                        result.put(key, obj.getString(key));
                    }
                    else if (value instanceof JSONArray) {
                        JSONArray ar = (JSONArray) value;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ar.length() - 1; i++) {
                            sb.append(ar.getString(i)).append(',');
                        }
                        sb.append(ar.getString(ar.length() - 1));
                        result.put(key, sb.toString());
                    }
                    else {
                        throw new RuntimeException("Cannot parse value returned from StrainInfo JSON");
                    }
                }
            }
        }
        return result;
    }

    public WebResource getStraininfo() {
        return straininfo;
    }

    public void setStraininfo(WebResource straininfo) {
        this.straininfo = straininfo;
    }
}
