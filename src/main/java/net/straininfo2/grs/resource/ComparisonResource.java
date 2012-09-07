package net.straininfo2.grs.resource;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import net.straininfo2.grs.dao.GenomeProjectService;
import net.straininfo2.grs.dao.MegxGenomeService;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.view.Viewable;

@Component
@Scope("request")
@Path("/comparisons/")
@Produces(MediaType.TEXT_HTML)
public class ComparisonResource {
	
	private final static Logger logger = LoggerFactory.getLogger(ComparisonResource.class);
	
	@Autowired
	MegxGenomeService megxService;
	
	@Autowired
	GenomeProjectService genomeService;
	
	@GET
	public Viewable getMain() {
		logger.debug("Received a GET for main index");
		return new Viewable("index", "blah");
	}
	
	@GET
	@Path("/{gpid}")
	public Viewable getGenome(@PathParam("gpid") long genomeId) {
		logger.debug("Received get for genome {}", genomeId);
		try {
			final Map<String, String> megxData = megxService.getMegxJson(genomeId);
			final Map<String, String> ncbiData = genomeService.findGenomeProject(genomeId).getContents();
			List<Map<String, String>> results = new LinkedList<Map<String, String>>();
			results.add(megxData);
			results.add(ncbiData);
			return new Viewable("comparison", results);
		} catch (JSONException e) {
			// unrecoverable, give some feedback to user
			throw new WebApplicationException(e);
		}
	}
	
}
