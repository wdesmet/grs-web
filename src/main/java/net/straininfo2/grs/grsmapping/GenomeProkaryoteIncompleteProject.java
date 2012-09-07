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
@PrimaryKeyJoinColumn(name="proj_id")
@Table(name="genome_prok_inprogress")
public class GenomeProkaryoteIncompleteProject extends GenomeProkaryoteProject {
	
	private String refseqAccession;
	
	private String genbankAccession;
	
	// nr of contigs in sequence
	private Long contigNr;
	
	// nr of codons, I suppose
	private Long cdsNr;
	
	// release date of the actual sequence, which doesn't make any sense since it's in progress
	private Date dateReleased;
	
	// sequencing center
	private String centerName;
	
	private String centerUrl;
	
	// stage of assembly, usually (for instance 'wgs assembly')
	private String sequenceAvailability;
	
	private Map<String, String> contents;

	@Column(name="refseq_accession")
	public String getRefseqAccession() {
		return refseqAccession;
	}

	public void setRefseqAccession(String refseqAccession) {
		this.refseqAccession = refseqAccession;
	}

	@Column(name="genbank_accession")
	public String getGenbankAccession() {
		return genbankAccession;
	}

	public void setGenbankAccession(String genbankAccession) {
		this.genbankAccession = genbankAccession;
	}

	@Column(name="contig_nr")
	public Long getContigNr() {
		return contigNr;
	}

	public void setContigNr(Long contigNr) {
		this.contigNr = contigNr;
	}

	@Column(name="cds_nr")
	public Long getCdsNr() {
		return cdsNr;
	}

	public void setCdsNr(Long cdsNr) {
		this.cdsNr = cdsNr;
	}

	@Column(name="date_released")
	public Date getDateReleased() {
		return dateReleased;
	}

	public void setDateReleased(Date dateReleased) {
		this.dateReleased = dateReleased;
	}

	@Column(name="center_name")
	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	@Column(name="center_url")
	public String getCenterUrl() {
		return centerUrl;
	}

	public void setCenterUrl(String centerUrl) {
		this.centerUrl = centerUrl;
	}

	@Column(name="sequence_availability")
	public String getSequenceAvailability() {
		return sequenceAvailability;
	}

	public void setSequenceAvailability(String sequenceAvailability) {
		this.sequenceAvailability = sequenceAvailability;
	}
	
	@Override
	@Transient
	public Map<String, String> getContents() {
		if (contents == null) {
			Map<String, String> res = new HashMap<String, String>(super.getContents());
			res.put("refseq", refseqAccession);
			res.put("genbank", genbankAccession);
			res.put("contig_nr", contigNr.toString());
			res.put("cds_nr", cdsNr.toString());
			res.put("center_name", centerName);
			res.put("center_url", centerUrl);
			res.put("sequence_availability", sequenceAvailability);
			res.put("date_released", dateReleased.toString());
			this.contents = res;
		}
		return contents;
	}
}
