package net.straininfo2.grs.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.straininfo2.grs.grsmapping.Mapping;

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
		return (List<Mapping>)getEntityManager().createQuery("from Mapping m join fetch m.provider join fetch m.genomeProject").getResultList();
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
				"from Mapping m join fetch m.provider join fetch m.genomeProject " +
				"where lower(m.provider.abbr)=:abbr"
				).setParameter("abbr", provider.toLowerCase()).getResultList();
	}
	
	/**
	 * Query mappings through provider and provider id.
	 * 
	 * @param provider abbreviation of the provider name
	 * @param id string with provider ID
	 * @return list of mappings for this provider and id
	 */
	@SuppressWarnings("unchecked")
	public List<Mapping> mappingsFor(String provider, String id) {
		return (List<Mapping>)getEntityManager().createQuery(
				"from Mapping m join fetch m.provider join fetch m.genomeProject " +
				"where lower(m.provider.abbr)=:provider and m.targetId = :id"
				).setParameter("provider", provider.toLowerCase()
						).setParameter("id", id).getResultList();
	}
}
