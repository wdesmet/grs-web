package net.straininfo2.grs.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.straininfo2.grs.grsmapping.Provider;

@XmlRootElement(name="provider")
public class ProviderDto {
	
	@XmlElement
	public long id;
	
	@XmlElement
	public String name;
	
	@XmlElement
	public String abbr;
	
	@XmlElement
	public String url;
	
	public ProviderDto() {
	}
	
	public ProviderDto(Provider provider) {
		this.id = provider.getId();
		this.name = provider.getName();
		this.abbr = provider.getAbbr();
		this.url = provider.getUrl();
	}
}
