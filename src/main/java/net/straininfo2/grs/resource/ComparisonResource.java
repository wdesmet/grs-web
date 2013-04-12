package net.straininfo2.grs.resource;

import com.sun.jersey.api.view.Viewable;
import net.straininfo2.grs.bioproject.BioProject;
import net.straininfo2.grs.dao.BioProjectService;
import net.straininfo2.grs.dao.MegxGenomeService;
import net.straininfo2.grs.dao.StraininfoService;
import net.straininfo2.grs.dto.ComparisonData;
import net.straininfo2.grs.mashup.MashupLogic;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.*;

@Component
@Scope("request")
@Path("/comparisons/")
@Produces(MediaType.TEXT_HTML)
public class ComparisonResource {
	
	private final static Logger logger = LoggerFactory.getLogger(ComparisonResource.class);
	
	@Autowired
	MegxGenomeService megxService;
	
	@Autowired
    BioProjectService genomeService;

    @Autowired
    StraininfoService straininfoService;
	
	@GET
	public Viewable getMain() {
		logger.debug("Received a GET for main index");
		return new Viewable("index", "blah");
	}

    @GET
    @Path("/search")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response testForm(@QueryParam("id") long id) {
        return Response.temporaryRedirect(UriBuilder.fromResource(ComparisonResource.class).path(""+id).build()).build();
    }
	
	@GET
	@Path("/{gpid}")
	public Viewable getGenomeComparison(@PathParam("gpid") long genomeId) {
		logger.debug("Received get for genome {}", genomeId);
        BioProject project = genomeService.findBioProject(genomeId);
        if (project == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
		try {
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
            results.add(Collections.singletonMap("id", ""+genomeId));
			return new Viewable("comparison", results);
		} catch (JSONException e) {
			// unrecoverable, give some feedback to user
			throw new WebApplicationException(e);
		}
	}

    @GET
    @Path("/{gpid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ComparisonData getMachineReadableGenomeComparison(@PathParam("gpid") long genomeId) {
        ComparisonData data = new ComparisonData();
        try {
            data.setMegx(megxService.getMegxJson(genomeId));
            // cheating a little bit by giving NCBI a provider ID
            data.setNcbi(genomeService.getAssociatedInformation(genomeService.findBioProject(genomeId)));
            data.setStrainInfo(straininfoService.getStrainInfoCultureData(genomeId));
        } catch (JSONException e) {
            throw new WebApplicationException(e);
        }
        return data;
    }
}