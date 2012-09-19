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
@Table(name="genome_prok")
public class GenomeProkaryoteProject extends GenomeProject {
	
	private String refSeqNumber;
	
	private Long ncbiTaxonId;
	
	private String organismName;
	
	private String superKingdom;
	
	private String taxonGroup;
	
	private String sequenceStatus;
	
	private BigDecimal genomeSize;
	
	private String gcContent;
	
	// true = positive, null is a possibility (no stain) 
	private Boolean gramStain;
	
	private String shape;
	
	private String arrangement;
	
	// yes, no or null (no data)
	private String endospores;
	
	// yes, no, no data
	private String motility;
	
	// halophilic, non-halophilic, ... or null
	private String salinity;
	
	private String oxygenRequirement;
	
	private String habitat;
	
	// mesophilic etc.
	private String tempRange;
	
	/* these weren't parsed because they often denote ranges
	 could parse them in this layer (change to Interval,
	 add a setter that accepts a String */
	private String optimalTemp; 
	
	// example values: null, No, Ruminant, "Human, Animal"
	private String pathogenicIn;
	
	// None, Bacterial vaginosis, (null)
	private String disease;
	
	private Map<String, String> contents;
	
	@Column(name="refseq")
	public String getRefSeqNumber() {
		return refSeqNumber;
	}

	public void setRefSeqNumber(String refSeqNumber) {
		this.refSeqNumber = refSeqNumber;
	}

	@Column(name="ncbi_taxon_id")
	public Long getNcbiTaxonId() {
		return ncbiTaxonId;
	}

	public void setNcbiTaxonId(Long ncbiTaxonId) {
		this.ncbiTaxonId = ncbiTaxonId;
	}

	@Column(name="organism_name")
	public String getOrganismName() {
		return organismName;
	}

	public void setOrganismName(String organismName) {
		this.organismName = organismName;
	}

	@Column(name="super_kingdom")
	public String getSuperKingdom() {
		return superKingdom;
	}

	public void setSuperKingdom(String superKingdom) {
		this.superKingdom = superKingdom;
	}

	@Column(name="taxon_group")
	public String getTaxonGroup() {
		return taxonGroup;
	}

	public void setTaxonGroup(String taxonGroup) {
		this.taxonGroup = taxonGroup;
	}

	@Column(name="sequence_status")
	public String getSequenceStatus() {
		return sequenceStatus;
	}

	public void setSequenceStatus(String sequenceStatus) {
		this.sequenceStatus = sequenceStatus;
	}

	@Column(name="genome_size")
	public BigDecimal getGenomeSize() {
		return genomeSize;
	}

	public void setGenomeSize(BigDecimal genomeSize) {
		this.genomeSize = genomeSize;
	}

	@Column(name="gc_content")
	public String getGcContent() {
		return gcContent;
	}

	public void setGcContent(String gcContent) {
		this.gcContent = gcContent;
	}

	@Column(name="gram_stain")
	public Boolean getGramStain() {
		return gramStain;
	}

	public void setGramStain(Boolean gramStain) {
		this.gramStain = gramStain;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getArrangement() {
		return arrangement;
	}

	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}

	public String getEndospores() {
		return endospores;
	}

	public void setEndospores(String endospores) {
		this.endospores = endospores;
	}

	public String getMotility() {
		return motility;
	}

	public void setMotility(String motility) {
		this.motility = motility;
	}

	public String getSalinity() {
		return salinity;
	}

	public void setSalinity(String salinity) {
		this.salinity = salinity;
	}

	@Column(name="oxygen_req")
	public String getOxygenRequirement() {
		return oxygenRequirement;
	}

	public void setOxygenRequirement(String oxygenRequirement) {
		this.oxygenRequirement = oxygenRequirement;
	}

	public String getHabitat() {
		return habitat;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	@Column(name="temp_range")
	public String getTempRange() {
		return tempRange;
	}

	public void setTempRange(String tempRange) {
		this.tempRange = tempRange;
	}

	@Column(name="optimal_temp")
	public String getOptimalTemp() {
		return optimalTemp;
	}

	public void setOptimalTemp(String optimalTemp) {
		this.optimalTemp = optimalTemp;
	}

	@Column(name="pathogenic_in")
	public String getPathogenicIn() {
		return pathogenicIn;
	}

	public void setPathogenicIn(String pathogenicIn) {
		this.pathogenicIn = pathogenicIn;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	@Override
	@Transient
	public Map<String, String> getContents() {
		if (contents == null) {
			Map<String, String> res = new HashMap<String, String>(super.getContents());
			res.put("refseq", refSeqNumber);
			res.put("taxid", ncbiTaxonId.toString());
			res.put("organism_name", organismName);
			res.put("super_kingdom", superKingdom);
			res.put("taxon_group", taxonGroup);
			res.put("sequence_status", sequenceStatus);
			res.put("genome_size", genomeSize.toPlainString());
			res.put("gc_content", gcContent);
			res.put("gram_stain", gramStain ? "t" : "f");
			res.put("shape", shape);
			res.put("arrangement", arrangement);
			res.put("endospores", endospores);
			res.put("motility", motility);
			res.put("salinity", salinity);
			res.put("oxygen_requirement", oxygenRequirement);
			res.put("habitat", habitat);
			res.put("temp_range", tempRange);
			res.put("optimal_temp", optimalTemp);
			res.put("pathogenic_in", pathogenicIn);
			res.put("disease", disease);
			this.contents = res;
		}
		return contents;
	}
	
}
