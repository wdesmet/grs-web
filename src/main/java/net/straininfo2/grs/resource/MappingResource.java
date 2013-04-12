package net.straininfo2.grs.resource;

import net.straininfo2.grs.bioproject.mappings.Mapping;
import net.straininfo2.grs.dao.MappingService;
import net.straininfo2.grs.dto.MappingDtoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;

@Component
@Scope("request")
@Path("/mappings/")
@Produces(MediaType.TEXT_PLAIN)
public class MappingResource {

    private final static Logger logger = LoggerFactory.getLogger(MappingResource.class);

    private final static String STATIC_HEADER = "genome_project_id\tlink_name\turl";

	@Autowired
	MappingService mappingService;
	
	@GET
	public Response allMappings() {
        logger.debug("Received a request for all mappings");
        Collection<Mapping> mappings = mappingService.allMappings();
        if (mappings == null || mappings.size() == 0) {
            return emptyResponse(null, null);
        }
        else {
            return formatMappings(mappings, FieldDescription.SOURCE, FieldDescription.TARGET_ID);
        }
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public MappingDtoCollection allStructuredMappings() {
		logger.debug("Received a request for all mappings as XML/JSON");
		return MappingDtoCollection.fromList(mappingService.allMappings());
	}
	
	@GET
	@Path("{source}")
	public Response getMappingsFor(@PathParam("source")String source) {
        logger.debug("Received a request for source {}", source);
		List<Mapping> mappings = mappingService.mappingsFor(source);
        if (mappings == null || mappings.size() == 0) {
            return emptyResponse(source, null);
        }
        else {
            return formatMappings(mappings, FieldDescription.TARGET_ID);
        }
	}
	
	@GET
	@Path("{source}")
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Response getStructuredMappingsFor(@PathParam("source")String source) {
		logger.debug("Received a request for source {} as XML/JSON", source);
		List<Mapping> mappings = mappingService.mappingsFor(source);
		if (mappings == null || mappings.size() == 0) {
			return emptyResponse(source, null);
		}
		else {
			return Response.ok(MappingDtoCollection.fromList(mappings)).build();
		}
	}
	
	@GET
	@Path("{source}/{sourceId}")
	public Response getMapping(@PathParam("source")String source,
			@PathParam("sourceId")String sourceId) {
        logger.debug("Received a request for source {} and id {}", source, sourceId);
		List<Mapping> mappings = mappingService.mappingsFor(source, sourceId);
        if (mappings == null || mappings.size() == 0) {
            return emptyResponse(source, sourceId);
        }
        else {
		    return formatMappings(mappings);
        }
	}
	
	@GET
	@Path("{source}/{sourceId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Response getStructuredMapping(@PathParam("source")String source,
			@PathParam("sourceId")String sourceId) {
		logger.debug("Received a request for source {} and id {} as XML/JSON", source, sourceId);
		List<Mapping> mappings = mappingService.mappingsFor(source, sourceId);
		if (mappings == null || mappings.size() == 0) {
			return emptyResponse(source, sourceId);
		}
		else {
			return Response.ok(MappingDtoCollection.fromList(mappings)).build();
		}
	}

    public static enum FieldDescription {
        SOURCE("source (abbr)"),
        TARGET_ID("source_id");

        private final String name;
        
        String extract(Mapping mapping) {
            switch (this) {
                case SOURCE:
                    return mapping.getProvider().getAbbr();
                case TARGET_ID:
                    return mapping.getTargetId();
                default:
                    return "";
            }
        }
        
        FieldDescription(String name) {
        	this.name = name;
        }
        
        @Override
        public String toString() {
        	return name;
        }
    }

    private Response emptyResponse(String source, String id) {
        String response = "No mappings found";
        if (source != null) {
            response += " for provider " + encode(source);
        }
        if (id != null) {
            response += " for id " + encode(id);
        }
        response += ".";
        return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(response).build();
    }
	
    public static Response formatMappings(Collection<Mapping> mappings, FieldDescription... descriptions) {
        StringBuilder sb = new StringBuilder(16 * mappings.size());
        // build the header first
        sb.append("# ");
        for (FieldDescription descr : descriptions) {
        	sb.append(descr.toString()).append('\t');
        }
        sb.append(STATIC_HEADER).append('\n');
        Formatter formatter = new Formatter(sb);
        for (Mapping mapping : mappings) {
            for (FieldDescription descr : descriptions) {
                formatter.format("%s\t", descr.extract(mapping));
            }
		    formatter.format("%d\t%s\t%s\n", 
				mapping.getBioProject().getProjectId(),
				mapping.getLinkName() == null ? "" : mapping.getLinkName(), 
						mapping.getUrl());
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // snip trailing \n
        }
        return Response.ok(sb.toString()).build();
    }
	
	private static String encode(String in) {
		try {
			return URLEncoder.encode(in, "utf-8").toString();
		} catch (UnsupportedEncodingException e) {
			// whatever
		}
		return "";
	}
	
}
