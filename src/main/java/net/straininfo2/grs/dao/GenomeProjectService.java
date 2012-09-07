package net.straininfo2.grs.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import net.straininfo2.grs.grsmapping.GenomeProject;

public class GenomeProjectService {

	protected EntityManager em;

	@PersistenceContext
	protected void setEntityManager(EntityManager em) {
		this.em = em;
	}

	protected EntityManager getEntityManager() {
		return em;
	}
	
	public GenomeProject findGenomeProject(long id) {
		return getEntityManager().find(GenomeProject.class, id);
	}

	/**
	 * Finds the genome project and attaches all mappings to it. Returns null
	 * when nothing was found
	 * 
	 * @param id a genome project identifier
	 * @return a genome project or null when none was found
	 */
	public GenomeProject findFullGenomeProject(long id) {
		try {
			return (GenomeProject) getEntityManager().createQuery(
				"from GenomeProject g left join fetch g.mappings m left join fetch m.provider "
						+ "where g.id=?").setParameter(1, id).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<GenomeProject> allGenomeProjects() {
		return (List<GenomeProject>) getEntityManager().createQuery(
				"from GenomeProject").getResultList();
	}

}
