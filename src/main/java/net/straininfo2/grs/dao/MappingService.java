package net.straininfo2.grs.dao;

import net.straininfo2.grs.bioproject.mappings.Mapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;

public class MappingService {

    protected EntityManager em;

    @PersistenceContext
    protected void setEntityManager(EntityManager em) {
        this.em = em;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    @SuppressWarnings("unchecked")
    public Collection<Mapping> allMappings() {
        return (List<Mapping>)getEntityManager().createQuery("from Mapping m join fetch m.provider join fetch m.bioProject").getResultList();
    }

    /**
     * Query mappings through provider name.
     *
     * @param provider abbreviation of the provider name
     * @return list of mappings for this provider
     */
    @SuppressWarnings("unchecked")
    public List<Mapping> mappingsFor(String provider) {
        return (List<Mapping>)getEntityManager().createQuery(
                "from Mapping m join fetch m.provider join fetch m.bioProject " +
                "where lower(m.provider.abbr)=:abbr"
                ).setParameter("abbr", provider.toLowerCase()).getResultList();
    }

    /**
     * Query mappings through provider and provider id. This
     * returns all mappings for the associated bioproject.
     *
     * @param provider abbreviation of the provider name
     * @param id string with provider ID
     * @return list of mappings for this provider and id
     */
    @SuppressWarnings("unchecked")
    public List<Mapping> mappingsFor(String provider, String id) {
        return (List<Mapping>)getEntityManager().createQuery(
                "from Mapping m join fetch m.provider join fetch m.bioProject " +
                "where m.bioProject in (select i.bioProject from Mapping i where " +
                        "lower(i.provider.abbr)=:provider and i.targetId = :id)"
                ).setParameter("provider", provider.toLowerCase()
                        ).setParameter("id", id).getResultList();
    }

}
