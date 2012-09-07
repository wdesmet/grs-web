package net.straininfo2.grs.grsmapping;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@PrimaryKeyJoinColumn(name="proj_id")
@Table(name="genome_euk")
public class GenomeEukaryoteProject extends GenomeProject {

	/* many of these values are similar to ones you'll find
	 * in GenomeProkaryoteProject. Similar comments as there
	 * on use of (null) etc. apply here.
	 */
	
	private String organismName;
	
	private String organismGroup;
	
	private String organismSubgroup;
	
	private Long ncbiTaxonId;
	
	private BigDecimal genomeSize;
	
	private Integer nrChromosomes;
	
	private String sequenceStatus;
	
	private String sequenceMethod;
	
	private String coverage;
	
	// release date of sequence data or project data
	private Date releaseDate;
	
	// dates created and updated in our database
	private Date createdAt;
	
	private Date updatedAt;
	
	private Map<String, String> contents;

	@Column(name="organism_name")
	public String getOrganismName() {
		return organismName;
	}

	public void setOrganismName(String organismName) {
		this.organismName = organismName;
	}

	@Column(name="organism_group")
	public String getOrganismGroup() {
		return organismGroup;
	}

	public void setOrganismGroup(String organismGroup) {
		this.organismGroup = organismGroup;
	}

	@Column(name="organism_subgroup")
	public String getOrganismSubgroup() {
		return organismSubgroup;
	}

	public void setOrganismSubgroup(String organismSubgroup) {
		this.organismSubgroup = organismSubgroup;
	}

	@Column(name="ncbi_taxon_id")
	public Long getNcbiTaxonId() {
		return ncbiTaxonId;
	}

	public void setNcbiTaxonId(Long ncbiTaxonId) {
		this.ncbiTaxonId = ncbiTaxonId;
	}

	@Column(name="genome_size")
	public BigDecimal getGenomeSize() {
		return genomeSize;
	}

	public void setGenomeSize(BigDecimal genomeSize) {
		this.genomeSize = genomeSize;
	}

	@Column(name="nr_chromosomes")
	public Integer getNrChromosomes() {
		return nrChromosomes;
	}

	public void setNrChromosomes(Integer nrChromosomes) {
		this.nrChromosomes = nrChromosomes;
	}

	@Column(name="sequence_status")
	public String getSequenceStatus() {
		return sequenceStatus;
	}

	public void setSequenceStatus(String sequenceStatus) {
		this.sequenceStatus = sequenceStatus;
	}

	@Column(name="sequence_method")
	public String getSequenceMethod() {
		return sequenceMethod;
	}

	public void setSequenceMethod(String sequenceMethod) {
		this.sequenceMethod = sequenceMethod;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
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
			res.put("organism_name", organismName);
			res.put("organism_group", organismGroup);
			res.put("organism_subgroup", organismSubgroup);
			res.put("taxid", ncbiTaxonId.toString());
			res.put("genome_size", genomeSize.toPlainString());
			res.put("nr_chromosomes", nrChromosomes.toString());
			res.put("sequence_status", sequenceStatus);
			res.put("sequence_method", sequenceMethod);
			res.put("coverage", coverage);
			res.put("release_date", releaseDate.toString());
			this.contents = res;
		}
		return contents;
	}
}