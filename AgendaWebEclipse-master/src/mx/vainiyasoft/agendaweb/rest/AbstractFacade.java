package mx.vainiyasoft.agendaweb.rest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import mx.vainiyasoft.agendaweb.entity.AbstractEntity;

import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class AbstractFacade<T extends AbstractEntity> {

	private final Class<T> entityClass;
	
	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	protected abstract EntityManager getEntityManager();
	
	public String create(T entity) {
		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			em.refresh(entity);
			return "ok";
		} catch(Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	@SuppressWarnings("unchecked")
	public String edit(T entity) {
		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			T managed = em.merge(entity);
			em.getTransaction().commit();
			em.refresh(managed);
			entity.merge(managed);
			return "ok";
		} catch(Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	public String remove(T entity) {
		try {
			EntityManager em = getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(entity));
			em.getTransaction().commit();
			return "ok";
		} catch(Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}
	
	public List<T> findAll() {
		EntityManager em = getEntityManager();
		CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}
	
	public List<T> findAllByOwner(String owner) {
		EntityManager em = getEntityManager();
		String namedQuery = String.format("%s.findByPropietario", entityClass.getSimpleName());
		TypedQuery<T> query = em.createNamedQuery(namedQuery, entityClass);
		query.setParameter("propietario", owner);
		return query.getResultList();
	}
	
	public List<T> findRange(int ... range) {
		EntityManager em = getEntityManager();
		CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		TypedQuery<T> tq = em.createQuery(cq);
		tq.setMaxResults(range[1] - range[0] + 1);
		tq.setFirstResult(range[0]);
		return tq.getResultList();
	}
	
	public int count() {
		EntityManager em = getEntityManager();
		CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
		Root<T> rt = cq.from(entityClass);
		cq.select(em.getCriteriaBuilder().count(rt));
		TypedQuery<Long> q = em.createQuery(cq);
		return q.getSingleResult().intValue();
	}
	
	
}
