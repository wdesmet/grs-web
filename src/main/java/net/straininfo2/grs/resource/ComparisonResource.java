package net.straininfo2.grs.resource;

import com.sun.jersey.api.view.Viewable;
import net.straininfo2.grs.dao.BioProjectService;
import net.straininfo2.grs.dao.MashupService;
import net.straininfo2.grs.dto.ComparisonData;
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
    BioProjectService genomeService;

    @Autowired
    MashupService mashupService;

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
        try {
            List<Object> results = mashupService.getCrossrefData(genomeId, this);
            return new Viewable("comparison", results);
        } catch (JSONException e) {
            // unrecoverable, give some feedback to user
            throw new WebApplicationException(e);
        } catch (IllegalArgumentException e) {
            // bioproject does not exist
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("/{gpid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ComparisonData getMachineReadableGenomeComparison(@PathParam("gpid") long genomeId) {
        try {
            ComparisonData data = mashupService.getComparisonData(genomeId, this);
            return data;
        } catch (JSONException e) {
            throw new WebApplicationException(e);
        }
    }

}