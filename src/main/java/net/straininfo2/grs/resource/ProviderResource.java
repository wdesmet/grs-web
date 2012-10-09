package net.straininfo2.grs.resource;

import net.straininfo2.grs.bioproject.mappings.Provider;
import net.straininfo2.grs.dao.ProviderService;
import net.straininfo2.grs.dto.ProviderDto;
import net.straininfo2.grs.dto.ProviderDtoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Formatter;

@Path("/providers/")
@Produces(MediaType.TEXT_PLAIN)
@Component
@Scope("request")
public class ProviderResource {

	@Autowired
	ProviderService providerService;

	private final static String HEADER = "# id\tname\tabbr\turl\n";
	
	private void formatTextProvider(Formatter formatter, ProviderDto provider) {
		formatter.format("%d\t%s\t%s\t%s\n", provider.id,
				provider.name, provider.abbr, provider.url);
	}
	
	@GET
	public String getProviders() {
		StringBuilder resp = new StringBuilder(HEADER);
		Formatter formatter = new Formatter(resp);
		for (ProviderDto provider : providerService.allProviders().getProviders()) {
			formatTextProvider(formatter, provider);
		}
		return resp.deleteCharAt(resp.length() - 1).toString();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public ProviderDtoCollection getStructuredProviders() {
		return providerService.allProviders();
	}
	
	private Provider findProvider(long id) {
		Provider provider = providerService.findProvider(id);
		if (provider == null) {
			throw new NotFoundException("Provider with id " + id + " not found.");
		}
		else {
			return provider;
		}
	}
	
	@Path("{id}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public ProviderDto getProvider(@PathParam("id") long id) {
		return new ProviderDto(findProvider(id));
	}
	
	
	
	@GET
	@Path("{id}")
	public String getProviderText(@PathParam("id") long id) {
		ProviderDto provider = new ProviderDto(findProvider(id));
		StringBuilder sb = new StringBuilder(HEADER);
		Formatter formatter = new Formatter(sb);
		formatTextProvider(formatter, provider);
		return sb.toString();
	}
	
	private static class NotFoundException extends WebApplicationException {
		
		private static final long serialVersionUID = 1L;
		
		private final Response response;
		
		public NotFoundException(String message) {
			super();
			this.response = Response.status(Status.NOT_FOUND).entity(message).type(MediaType.TEXT_PLAIN).build(); 
		}
		
		@Override
		public Response getResponse() {
			return response;
		}
	}
}
