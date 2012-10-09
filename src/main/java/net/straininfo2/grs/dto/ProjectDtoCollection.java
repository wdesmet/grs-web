package net.straininfo2.grs.dto;

import net.straininfo2.grs.bioproject.BioProject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	
	public static ProjectDtoCollection fromList(Collection<BioProject> projects) {
		List<ProjectDto> dtos = new ArrayList<>(projects.size());
		for (BioProject project : projects) {
			dtos.add(new ProjectDto(project));
		}
		return new ProjectDtoCollection(dtos);
	}
}
