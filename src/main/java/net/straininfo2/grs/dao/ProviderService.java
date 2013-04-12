package net.straininfo2.grs.dao;

import net.straininfo2.grs.bioproject.mappings.Provider;
import net.straininfo2.grs.dto.ProviderDto;
import net.straininfo2.grs.dto.ProviderDtoCollection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class ProviderService {

    protected EntityManager em;

    @PersistenceContext
    protected void setEntityManager(EntityManager em) {
        this.em = em;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    public Provider findProvider(long id) {
        return getEntityManager().find(Provider.class, id);
    }

    public Provider findProviderWithMapping(long id) {
        return (Provider)getEntityManager().createQuery(
                "from Provider p left join fetch p.mapping where p.id=?"
                ).setParameter(1, id).getSingleResult();
    }

    public Provider findProviderWithMapping(String abbr) {
        return (Provider)getEntityManager().createQuery(
                "from Provider p left join fetch p.mapping where p.abbr=?"
                ).setParameter(1, abbr).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public ProviderDtoCollection allProviders() {
        List<Provider> providers = (List<Provider>)em.createQuery("from Provider").getResultList();
        List<ProviderDto> providerDtos = new ArrayList<ProviderDto>(providers.size());
        for (Provider provider : providers) {
            providerDtos.add(new ProviderDto(provider));
        }
        return new ProviderDtoCollection(providerDtos);
    }
}
