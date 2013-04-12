package net.straininfo2.grs.bioproject;

import javax.persistence.*;

@Entity
public class Organism {

    private long id;

    private String organismName;

    private String label;

    private String strain;

    private String isolateName;

    private String breed;

    private String cultivar;

    private String supergroup;

    private Integer taxID;

    private Integer species;

    private String organization;

    private String reproduction;

    private Long genomeSize;

    private String genomeSizeUnits;

    private OrganismMorphology morphology;

    private OrganismEnvironment environment;

    private OrganismPhenotype phenotype;

    private OrganismSample sample;

    private BioProject bioProject;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //private TypeOrganism.BiologicalProperties biologicalProperties;

    // TODO: Replicons not mapped yet.

    public String getOrganismName() {
        return organismName;
    }

    public void setOrganismName(String organismName) {
        this.organismName = organismName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getIsolateName() {
        return isolateName;
    }

    public void setIsolateName(String isolateName) {
        this.isolateName = isolateName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getCultivar() {
        return cultivar;
    }

    public void setCultivar(String cultivar) {
        this.cultivar = cultivar;
    }

    public String getSupergroup() {
        return supergroup;
    }

    public void setSupergroup(String supergroup) {
        this.supergroup = supergroup;
    }

    public Integer getTaxID() {
        return taxID;
    }

    public void setTaxID(Integer taxID) {
        this.taxID = taxID;
    }

    public Integer getSpecies() {
        return species;
    }

    public void setSpecies(Integer species) {
        this.species = species;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getReproduction() {
        return reproduction;
    }

    public void setReproduction(String reproduction) {
        this.reproduction = reproduction;
    }

    public Long getGenomeSize() {
        return genomeSize;
    }

    public void setGenomeSize(Long genomeSize) {
        this.genomeSize = genomeSize;
    }

    public String getGenomeSizeUnits() {
        return genomeSizeUnits;
    }

    public void setGenomeSizeUnits(String genomeSizeUnits) {
        this.genomeSizeUnits = genomeSizeUnits;
    }

    @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
    public OrganismMorphology getMorphology() {
        return morphology;
    }

    public void setMorphology(OrganismMorphology morphology) {
        this.morphology = morphology;
    }

    @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
    public OrganismEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(OrganismEnvironment environment) {
        this.environment = environment;
    }

    @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
    public OrganismPhenotype getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(OrganismPhenotype phenotype) {
        this.phenotype = phenotype;
    }

    @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
    public OrganismSample getSample() {
        return sample;
    }

    public void setSample(OrganismSample sample) {
        this.sample = sample;
    }

    @ManyToOne(optional = false)
    public BioProject getBioProject() {
        return bioProject;
    }

    public void setBioProject(BioProject bioProject) {
        this.bioProject = bioProject;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Organism)) {
            return false;
        }
        else {
            return getId() == ((Organism)o).getId();
        }
    }

    @Override
    public int hashCode() {
        return (int)getId();
    }

}
