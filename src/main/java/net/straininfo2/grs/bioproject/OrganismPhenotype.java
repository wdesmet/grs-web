package net.straininfo2.grs.bioproject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrganismPhenotype {

    public enum BioticRelationship {
        eFreeLiving,
        eCommensal,
        eSymbiont,
        eEpisymbiont,
        eIntracellular,
        eParasite,
        eHost,
        eEndosymbiont;

        @Override
        public String toString() {
            return this.name().substring(1);
        }
    }
    
    public enum TrophicLevel {
        eAutotroph,
        eHeterotroph,
        eMixotroph;

        @Override
        public String toString() {
            return this.name().substring(1);
        }
    }

    private long id;
    
    private BioticRelationship bioticRelationship;
    
    private TrophicLevel trophicLevel;

    private String disease;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BioticRelationship getBioticRelationship() {
        return bioticRelationship;
    }

    public void setBioticRelationship(BioticRelationship bioticRelationship) {
        this.bioticRelationship = bioticRelationship;
    }

    public TrophicLevel getTrophicLevel() {
        return trophicLevel;
    }

    public void setTrophicLevel(TrophicLevel trophicLevel) {
        this.trophicLevel = trophicLevel;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

}
