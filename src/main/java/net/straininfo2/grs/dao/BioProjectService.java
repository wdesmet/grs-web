package net.straininfo2.grs.dao;

import net.straininfo2.grs.bioproject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;

/**
 * Simplifies querying BioProject related information.
 */
public class BioProjectService {

    protected EntityManagerFactory factory;

    private final static Logger logger = LoggerFactory.getLogger(BioProjectService.class);

    public EntityManagerFactory getFactory() {
        return factory;
    }

    @PersistenceUnit
    public void setFactory(EntityManagerFactory factory) {
        this.factory = factory;
    }

    protected EntityManager getEntityManager() {
        logger.debug("Creating entity manager");
        return factory.createEntityManager();
    }

    public BioProject findBioProject(long id) {
        logger.debug("searching for bioproject");
        EntityManager em = getEntityManager();
        try {

            BioProject proj = em.find(BioProject.class, id);
            logger.debug("Returning bioproject");
            return proj;
        } finally {
            logger.debug("closing entity manager");
            em.close();
        }
    }

    /**
     * Finds the bioproject and attaches all mappings to it. Returns null
     * when nothing was found
     *
     * @param id a bioproject identifier
     * @return a bioproject or null when none was found
     */
    public BioProject findFullGenomeProject(long id) {
        logger.debug("searching for 'full' bioproject");
        EntityManager em = getEntityManager();
        try {
            return (BioProject) em.createQuery(
                    "from BioProject proj left join fetch proj.mappings m left join fetch m.provider "
                            + "where proj.id=?").setParameter(1, id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            logger.debug("closing entity manager");
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<BioProject> allGenomeProjects() {
        logger.debug("searching for bioproject list");
        EntityManager em = getEntityManager();
        try {
            return (List<BioProject>) em.createQuery(
                "from BioProject").getResultList();
        } finally {
            logger.debug("closing entity manager");
            em.close();
        }
    }

    // transactional so the session stays open
    @Transactional
    public Map<String, String> getAssociatedInformation(BioProject project) {
        // FIXME: this would be a lot less code with some metaprogramming, or jackson json conversion from organism
        logger.debug("Getting associated information");
        EntityManager em = getEntityManager();
        try {
            project = em.find(BioProject.class, project.getProjectId());
            if (project == null) {
                return Collections.emptyMap();
            }
            Map<String, String> res = new HashMap<>();
            // No longer available: "refseq" (maybe add to parsing)
            if (project.getAccession() != null) res.put("accession", project.getAccession());
            if (project.getArchive() != null) res.put("archive", project.getArchive().toString());

            Organism organism = project.retrieveOrganism();
            if (organism != null) {
                if (organism.getTaxID() != null) res.put("tax_id", organism.getTaxID().toString());
                res.put("species_id", organism.getSpecies().toString());
                if (organism.getOrganismName() != null) res.put("organism_name", organism.getOrganismName());
                else if (organism.getLabel() != null) res.put("organism_name", organism.getLabel());
                if (organism.getBreed() != null) res.put("breed", organism.getBreed());
                if (organism.getCultivar() != null) res.put("cultivar", organism.getCultivar());
                if (organism.getIsolateName() != null) res.put("isolate", organism.getIsolateName());
                if (organism.getGenomeSize() != null) res.put("genome_size", organism.getGenomeSize() + organism.getGenomeSizeUnits());
                if (organism.getSpecies() != null) res.put("strain", organism.getStrain());
                if (organism.getSupergroup() != null) res.put("supergroup", organism.getSupergroup());
                if (organism.getOrganization() != null) res.put("organization", organism.getOrganization());
                if (organism.getSample() != null) {
                    OrganismSample sample = organism.getSample();
                    if (sample.getIsolatedCell() != null) res.put("isolated_cell", sample.getIsolatedCell() ? "yes" : "no");
                    if (sample.getTissueSample() != null) res.put("tissue_sample", sample.getTissueSample() ? "yes" : "no");
                    if (sample.getCultureSampleInfo() != null) res.put("culture_sample", sample.getCultureSampleInfo().toString());
                }
                if (organism.getEnvironment() != null) {
                    OrganismEnvironment env = organism.getEnvironment();
                    if (env.getHabitat() != null) res.put("habitat", env.getHabitat().toString());
                    if (env.getOptimumTemperature() != null) res.put("optimal_temperature", env.getOptimumTemperature());
                    if (env.getOxygenReq() != null) res.put("oxygen_requirement", env.getOxygenReq().toString());
                    if (env.getSalinity() != null) res.put("salinity", env.getSalinity().toString());
                    if (env.getTemperatureRange() != null) res.put("temperature_range", env.getTemperatureRange().toString());
                }
                if (organism.getMorphology() != null) {
                    OrganismMorphology morphology = organism.getMorphology();
                    if (morphology.getEndospores() != null) res.put("endospores", morphology.getEndospores() ? "yes" : "no");
                    if (morphology.getEnveloped() != null) res.put("enveloped", morphology.getEnveloped() ? "yes" : "no");
                    if (morphology.getMotility() != null) res.put("motility", morphology.getMotility() ? "yes" : "no");
                    if (morphology.getGram() != null) res.put("gram_strain", morphology.getGram().toString());
                    if (morphology.getShapes() != null && morphology.getShapes().size() > 0)
                        res.put("shapes", StringUtils.collectionToCommaDelimitedString(morphology.getShapes()));
                }
                if (organism.getPhenotype() != null) {
                    OrganismPhenotype phenotype = organism.getPhenotype();
                    if (phenotype.getDisease() != null) res.put("disease", phenotype.getDisease());
                    if (phenotype.getBioticRelationship() != null) res.put("biotic_relationship", phenotype.getBioticRelationship().toString());
                    if (phenotype.getTrophicLevel() != null) res.put("trophic_level", phenotype.getTrophicLevel().toString());
                }
            }
            if (project.getLocusTagPrefixes() != null && project.getLocusTagPrefixes().size() > 0)
                res.put("locustag_prefixes", StringUtils.collectionToCommaDelimitedString(project.getLocusTagPrefixes()));
            if (project.getDescription() != null) res.put("description", project.getDescription());
            if (project.getName() != null) res.put("name", project.getName());
            if (project.getTitle() != null) res.put("title", project.getTitle());
            return res;
        } finally {
            logger.debug("closing entity manager");
            em.close();
        }
    }
}
