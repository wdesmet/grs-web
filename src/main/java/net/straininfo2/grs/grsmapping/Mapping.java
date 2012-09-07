package net.straininfo2.grs.grsmapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="mappings")
public class Mapping {

	private long id;
	
	private String url;
	
	private String subjectType;
	
	private String linkName;
	
	private String category;
	
	private String targetId;
	
	private Provider provider;
	
	private GenomeProject project;
	
	protected void setId(long id) {
		this.id = id;
	}
	
	@Id
	public long getId() {
		return id;
	}
	
	protected void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	protected void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	
	@Column(name="subject_type")
	public String getSubjectType() {
		return subjectType;
	}
	
	protected void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	@Column(name="link_name")
	public String getLinkName() {
		return linkName;
	}
	
	protected void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
	
	protected void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
	@Column(name="target_id")
	public String getTargetId() {
		return targetId;
	}
	
	protected void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="provider_id")
	public Provider getProvider() {
		return provider;
	}
	
	protected void setGenomeProject(GenomeProject project) {
		this.project = project;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="project_id")
	public GenomeProject getGenomeProject() {
		return project;
	}
}
