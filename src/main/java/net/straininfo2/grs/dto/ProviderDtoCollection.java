package net.straininfo2.grs.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="providers")
public class ProviderDtoCollection {
	
	private List<ProviderDto> providers;
	
	public ProviderDtoCollection() {
		
	}
	
	public ProviderDtoCollection(List<ProviderDto> providers) {
		this.providers = providers;
	}
	
	@XmlElement(name="provider")
	public List<ProviderDto> getProviders() {
		return providers;
	}
	
	public void setProviders(List<ProviderDto> providers) {
		this.providers = providers;
	}
}
