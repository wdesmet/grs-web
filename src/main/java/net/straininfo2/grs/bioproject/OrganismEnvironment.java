package net.straininfo2.grs.bioproject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrganismEnvironment {

    public enum Salinity {
        eUnknown("Unknown"),
        eNonHalophilic("Non-halophilic"),
        eMesophilic("Mesophilic"),
        eModerateHalophilic("Moderate-halophilic"),
        eExtremeHalophilic("Extreme-halophilic");

        private String descr;

        Salinity(String descr) {
            this.descr = descr;
        }

        @Override
        public String toString() {
            return descr;
        }
    }
    
    public enum OxygenReq {
        eUnknown,
        eAerobic,
        eMicroaerophilic,
        eFacultative,
        eAnaerobic;

        @Override
        public String toString() {
            return this.name().substring(1);
        }
    }

    public enum TemperatureRange {
        eUnknown,
        eCryophilic,
        ePsychrophilic,
        eMesophilic,
        eThermophilic,
        eHyperthermophilic;

        @Override
        public String toString() {
            return this.name().substring(1);
        }
    }
    
    public enum Habitat {
        eUnknown,
        eHostAssociated,
        eAquatic,
        eTerrestrial,
        eSpecialized,
        eMultiple;

        @Override
        public String toString() {
            return this.name().substring(1);
        }
    }

    private long id;
    
    private Salinity salinity;
    
    private OxygenReq oxygenReq;
    
    private String optimumTemperature;
    
    private TemperatureRange temperatureRange;
    
    private Habitat habitat;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Salinity getSalinity() {
        return salinity;
    }

    public void setSalinity(Salinity salinity) {
        this.salinity = salinity;
    }

    public OxygenReq getOxygenReq() {
        return oxygenReq;
    }

    public void setOxygenReq(OxygenReq oxygenReq) {
        this.oxygenReq = oxygenReq;
    }

    public String getOptimumTemperature() {
        return optimumTemperature;
    }

    public void setOptimumTemperature(String optimumTemperature) {
        this.optimumTemperature = optimumTemperature;
    }

    public TemperatureRange getTemperatureRange() {
        return temperatureRange;
    }

    public void setTemperatureRange(TemperatureRange temperatureRange) {
        this.temperatureRange = temperatureRange;
    }

    public Habitat getHabitat() {
        return habitat;
    }

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }
}
