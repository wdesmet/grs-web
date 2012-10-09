package net.straininfo2.grs.resource;


import net.straininfo2.grs.bioproject.BioProject;
import net.straininfo2.grs.dao.BioProjectService;
import net.straininfo2.grs.dto.ProjectDto;
import net.straininfo2.grs.dto.ProjectDtoCollection;
import net.straininfo2.grs.resource.MappingResource.FieldDescription;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Scope("request")
@Path("/projects/")
@Produces(MediaType.TEXT_PLAIN)
public class ProjectsResource {
	
	@Autowired
	private BioProjectService projectService;
	
	private final static Response LIST_ERROR = 
			Response.status(Status.INTERNAL_SERVER_ERROR).entity("No genome projects found").build();
	
	@GET
	public Response allProjectIdentifiersPlaintext() {
		Collection<BioProject> projects = projectService.allGenomeProjects();
		if (projects == null || projects.size() == 0) {
			return LIST_ERROR;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("# project_id\n");
		for (BioProject proj : projects) {
			sb.append(proj.getProjectId()).append('\n');
		}
		return Response.ok(sb.toString()).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Response allProjectIdentifiers() {
		Collection<BioProject> projects = projectService.allGenomeProjects();
		if (projects == null || projects.size() == 0) {
			return LIST_ERROR;
		}
		else {
			List<ProjectDto> dtos = new ArrayList<ProjectDto>(projects.size());
			for (BioProject proj : projects) {
				dtos.add(new ProjectDto(proj.getProjectId()));
			}
			return Response.ok(new ProjectDtoCollection(dtos)).build();
		}
	}
	
	@GET
	@Path("{id}")
	public Response projectPlaintext(@PathParam("id")long id) {
		BioProject proj = projectService.findFullGenomeProject(id);
		if (proj == null) {
			return projectNotFound(id);
		}
		else {
			return MappingResource.formatMappings(proj.getMappings(), 
					FieldDescription.SOURCE, FieldDescription.TARGET_ID);
		}
	}
	
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Response project(@PathParam("id")long id) {
		BioProject proj = projectService.findFullGenomeProject(id);
		if (proj == null) {
			return projectNotFound(id);
		}
		else {
			return Response.ok(new ProjectDto(proj)).build();
		}
	}
	

	private static Response projectNotFound(long id) {
		return Response.status(Status.NOT_FOUND)
				.entity("Project " + id + " not found.")
				.type(MediaType.TEXT_PLAIN).build();
	}
}