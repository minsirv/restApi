package org.minsirv.restApi.repository;

import org.minsirv.restApi.entity.NationalId;
import org.minsirv.restApi.entity.NationalId_;
import org.minsirv.restApi.repository.filters.NationalIdFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class NationalIdRepositoryImpl implements NationalIdRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<NationalId> search(NationalIdFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<NationalId> cq = cb.createQuery(NationalId.class);

        Root<NationalId> root = cq.from(NationalId.class);

        List<Predicate> predicates = new ArrayList<>();
        if(filter.getFrom() != null){
            predicates.add(cb.greaterThanOrEqualTo(root.get(NationalId_.dateOfBirth), filter.getFrom()));
        }
        if(filter.getTo() != null){
            predicates.add(cb.lessThanOrEqualTo(root.get(NationalId_.dateOfBirth), filter.getTo()));
        }
        if(Boolean.TRUE.equals(filter.getHasErrors())) {
            predicates.add(cb.not(cb.isEmpty(root.get(NationalId_.validationErrors))));
        }
        if(Boolean.FALSE.equals(filter.getHasErrors())) {
            predicates.add(cb.isEmpty(root.get(NationalId_.validationErrors)));
        }

        cq.where(predicates.toArray(new Predicate[]{}));
        cq.select(root);

        return em.createQuery(cq).getResultList();
    }

    @Override
    public NationalId getById(String id) {
        return em.createQuery("SELECT a FROM NationalId a where a.id = ?1", NationalId.class).setParameter(1, id)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public NationalId save(NationalId nationalId) {
        NationalId fromDb = getById(nationalId.getId());
        if (fromDb == null) {
            em.persist(nationalId);
            return nationalId;
        }
        return fromDb;
    }

    @Override
    public List<NationalId> findAll() {
        return em.createQuery("SELECT a FROM NationalId a", NationalId.class).getResultList();
    }

    @Override
    public void remove(NationalId nationalId) {
        em.remove(em.contains(nationalId) ? nationalId : em.merge(nationalId));
    }
}
