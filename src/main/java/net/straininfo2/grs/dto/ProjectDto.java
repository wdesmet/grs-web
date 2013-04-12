package net.straininfo2.grs.dto;

import net.straininfo2.grs.bioproject.BioProject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="genome_project")
public class ProjectDto {

	@XmlElement
	long id;
	
	@XmlElement
	MappingDtoCollection mappings;
	
	public ProjectDto() {
		
	}
	
	public ProjectDto(long id) {
		this.id = id;
	}
	
	public ProjectDto(BioProject project) {
		this.id = project.getProjectId();
		this.mappings = MappingDtoCollection.fromList(project.getMappings());
	}
	
	public void setMappings(MappingDtoCollection mappings) {
		this.mappings = mappings;
	}
}
