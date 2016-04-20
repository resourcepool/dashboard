package com.excilys.shoofleurs.dashboard.business.crud;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Stateless
public class CrudService<T> {

	@PersistenceContext(unitName = "mysql-pool")
	private EntityManager mEntityManager;


	public T findById(Class classEntity, int id) {
		return (T) mEntityManager.find(classEntity, id);
	}

	public T create(T entity) throws SQLException {
		mEntityManager.persist(entity);
		mEntityManager.flush();
		mEntityManager.refresh(entity);
		return entity;
	}


	public List<T> findWithNamedQuery(String namedQuery, Map<Object, Object> parameters, int start, int offset) {
		//Set<Map.Entry<Object, Object>> rawParameters = parameters.entrySet();
		Query query = mEntityManager.createNamedQuery(namedQuery);
		if(offset > 0) {
			query.setFirstResult(start).setMaxResults(offset);
		}

		/*for (Map.Entry entry : rawParameters) {
			query.setParameter((String) entry.getKey(), entry.getValue());
		}*/
		return (List<T>) query.getResultList();
	}
}
