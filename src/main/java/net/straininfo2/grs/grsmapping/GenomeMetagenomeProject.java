package net.straininfo2.grs.grsmapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;



@Entity
@PrimaryKeyJoinColumn(name="proj_id")
@Table(name="genome_env")
public class GenomeMetagenomeProject extends GenomeProject {

	private GenomeProject parentProject;
	
	private String title;
	
	private String metagenomeSource;
	
	private Integer metagenomeType;
	
	// release date of metagenome data
	private Date releaseDate;
	
	// creation and update dates in our database
	private Date createdAt;
	
	private Date updatedAt;
	
	private Map<String, String> contents;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_proj_id")
	public GenomeProject getParentProject() {
		return parentProject;
	}

	public void setParentProject(GenomeProject parentProject) {
		this.parentProject = parentProject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="metagenome_source")
	public String getMetagenomeSource() {
		return metagenomeSource;
	}

	public void setMetagenomeSource(String metagenomeSource) {
		this.metagenomeSource = metagenomeSource;
	}

	@Column(name="metagenome_type")
	public Integer getMetagenomeType() {
		return metagenomeType;
	}

	public void setMetagenomeType(Integer metagenomeType) {
		this.metagenomeType = metagenomeType;
	}

	@Column(name="release_date")
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Column(name="created_at")
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name="updated_at")
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	@Transient
	public Map<String, String> getContents() {
		if (contents == null) {
			Map<String, String> res = new HashMap<String, String>(super.getContents());
			res.put("title", title);
			res.put("source", metagenomeSource);
			res.put("type", metagenomeType.toString());
			res.put("release_date", releaseDate.toString());
			this.contents = res;
		}
		return contents;
	}
}
