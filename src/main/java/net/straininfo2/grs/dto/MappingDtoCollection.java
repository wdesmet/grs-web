package net.straininfo2.grs.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.straininfo2.grs.grsmapping.Mapping;

@XmlRootElement(name="mappings")
public class MappingDtoCollection {

	private List<MappingDto> mappings;
	
	public MappingDtoCollection() {
		
	}
	
	public MappingDtoCollection(List<MappingDto> mappings) {
		this.mappings = mappings;
	}
	
	@XmlElement(name="mapping")
	public List<MappingDto> getMappings() {
		return mappings;
	}
	
	public void setMappings(List<MappingDto> mappings) {
		this.mappings = mappings;
	}
	
	public static MappingDtoCollection fromList(Collection<Mapping> mappings) {
		List<MappingDto> mappingDtos = new ArrayList<MappingDto>(mappings.size());
		for (Mapping mapping : mappings) {
			mappingDtos.add(new MappingDto(mapping));
		}
		return new MappingDtoCollection(mappingDtos);
	}
}
