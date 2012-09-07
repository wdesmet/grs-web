package net.straininfo2.grs.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.straininfo2.grs.grsmapping.GenomeProject;

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
	
	public ProjectDto(GenomeProject project) {
		this.id = project.getId();
		this.mappings = MappingDtoCollection.fromList(project.getMappings());
	}
	
	public void setMappings(MappingDtoCollection mappings) {
		this.mappings = mappings;
	}
}
