package net.straininfo2.grs.dao;

import net.straininfo2.grs.bioproject.BioProject;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

public interface BioProjectService {

    BioProject findBioProject(long id);

    /**
     * Finds the bioproject and attaches all mappings to it. Returns null
     * when nothing was found
     *
     * @param id a bioproject identifier
     * @return a bioproject or null when none was found
     */
    BioProject findFullGenomeProject(long id);

    @SuppressWarnings("unchecked")
    Collection<BioProject> allGenomeProjects();

    // transactional so the session stays open
    @Transactional
    Map<String, String> getAssociatedInformation(BioProject project);
}
