package net.straininfo2.grs.grsmapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="genome_projects")
public class GenomeProject {

	private long id;
	
	private List<Mapping> mappings;
	
	private Map<String, String> contents;
	
	protected void setId(long id) {
		this.id = id;
	}
	
	@Id
	public long getId() {
		return id;
	}
	
	protected void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}
	
	@OneToMany(mappedBy="genomeProject")
	public List<Mapping> getMappings() {
		return mappings;
	}
	
	@Transient
	public Map<String, String> getContents() {
		if (contents == null) {
			contents = Collections.singletonMap("id", "" + id);
		}
		return contents;
	}
}
