package net.straininfo2.grs.grsmapping;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="providers")
public class Provider {

	private long id;
	
	private String name;
	
	private String abbr;
	
	private String url;
	
	private List<Mapping> mappings;
	
	protected void setId(long id) { 
		this.id = id;
	}
	
	@Id
	public long getId() {
		return id;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	protected void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	
	public String getAbbr() {
		return abbr;
	}
	
	protected void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	protected void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}
	
	@OneToMany(mappedBy="provider")
	public List<Mapping> getMappings() {
		return mappings;
	}
}
