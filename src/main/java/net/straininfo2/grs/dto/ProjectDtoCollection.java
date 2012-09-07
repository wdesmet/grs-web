package net.straininfo2.grs.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.straininfo2.grs.grsmapping.GenomeProject;

@XmlRootElement(name="genome_projects")
public class ProjectDtoCollection {

	private List<ProjectDto> projects;
	
	public ProjectDtoCollection() {
		// empty constructor
	}
	
	public ProjectDtoCollection(List<ProjectDto> projects) {
		this.projects = projects;
	}
	
	@XmlElement(name="genome_project")
	public List<ProjectDto> getProjects() {
		return projects;
	}
	
	public void setProjects(List<ProjectDto> projects) {
		this.projects = projects;
	}
	
	public static ProjectDtoCollection fromList(Collection<GenomeProject> projects) {
		List<ProjectDto> dtos = new ArrayList<ProjectDto>(projects.size());
		for (GenomeProject project : projects) {
			dtos.add(new ProjectDto(project));
		}
		return new ProjectDtoCollection(dtos);
	}
}
