package net.straininfo2.grs.grsmapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="genome_prok_complete")
@PrimaryKeyJoinColumn(name="proj_id")
public class GenomeProkaryoteCompleteProject extends GenomeProkaryoteProject {
	
	// where Long is used instead of long, values can be null
	private Long chromosomeNumber;
	
	private Long plasmidNumber;
	
	// date from the record itself, release date of the full genome
	private Date dateReleased;
	
	private Date dateModified;
	
	private Map<String, String> contents;

	@Column(name="chromosome_number")
	public Long getChromosomeNumber() {
		return chromosomeNumber;
	}

	public void setChromosomeNumber(Long chromosomeNumber) {
		this.chromosomeNumber = chromosomeNumber;
	}

	@Column(name="plasmid_number")
	public Long getPlasmidNumber() {
		return plasmidNumber;
	}

	public void setPlasmidNumber(Long plasmidNumber) {
		this.plasmidNumber = plasmidNumber;
	}

	@Column(name="date_released")
	public Date getDateReleased() {
		return dateReleased;
	}

	public void setDateReleased(Date dateReleased) {
		this.dateReleased = dateReleased;
	}

	@Column(name="date_modified")
	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	@Override
	@Transient
	public Map<String, String> getContents() {
		if (contents == null) {
			Map<String, String> res = new HashMap<String, String>(super.getContents());
			res.put("chromosome_number", chromosomeNumber.toString());
			res.put("plasmid_number", plasmidNumber.toString());
			res.put("date_released", dateReleased.toString());
			this.contents = res;
		}
		return contents;
	}

}
