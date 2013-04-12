package net.straininfo2.grs.bioproject;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class DBXref extends Link {

    private String db;

    private String dbId;

    private BioProject bioProject;

    @Column(length = 32)
    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    /**
     * List of identifiers as a string, separated by comma (',').
     *
     * @return encoded list of identifiers defined by this cross-reference
     */
    @Column(length = 128)
    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    @ManyToOne(optional = false)
    public BioProject getBioProject() {
        return bioProject;
    }

    public void setBioProject(BioProject bioProject) {
        this.bioProject = bioProject;
    }
}
