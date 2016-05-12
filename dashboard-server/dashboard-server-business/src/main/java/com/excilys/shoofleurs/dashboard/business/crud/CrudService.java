package com.excilys.shoofleurs.dashboard.business.crud;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <T> Type of the entity associated with Crud Operation. Initialize by the DAOs.
 */
@SuppressWarnings("unchecked")
@Stateless
public class CrudService<T> {

	@PersistenceContext(unitName = "mysql-pool")
	private EntityManager mEntityManager;


	/**
	 * Get an entity by its id.
	 * @param classEntity Entity's class
	 * @param id Id to find
	 * @return The object reprensting the entity
	 * @throws SQLException Test if something went wrong with the database
	 */
	protected T findById(Class classEntity, int id) throws SQLException {
		return (T) mEntityManager.find(classEntity, id);
	}

	/**
	 * Persit the entity into the database.
	 * @param entity Entity to persist
	 * @return The entity persisted with the refreshed ID
	 * @throws SQLException Test if something went wrong with the database
	 */
	public T create(T entity) throws SQLException {
		mEntityManager.persist(entity);
		mEntityManager.flush();
		mEntityManager.refresh(entity);
		return entity;
	}

	/**
	 * Update the entity into the database.
	 * @param entity Entity to update
	 * @return The entity updated and refreshed
	 * @throws SQLException Test if something went wrong with the database
	 */
	public T update(T entity) throws SQLException {
		mEntityManager.merge(entity);
		mEntityManager.flush();
		return entity;
	}

	/**
	 * Delete an entity from the database.
	 * @param entity Entity to delete
	 * @throws SQLException Test if something went wrong with the database
	 */
	public void delete(T entity) throws SQLException {
		mEntityManager.remove(entity);
		mEntityManager.flush();
	}

	/**
	 * Execute a named query and get the result. This method accept multiple parameters optionnal.
	 * @param namedQuery Named query to execute (The named queries are declared inside entities)
	 * @param parameters Optionnal parameters for the request as map (key : name of parameter)
	 * @param start Optionnal, use by the pagination, reprents the begining of the range
	 * @param offset Optionnal, use by the pagination, represents the limit of the range
	 * @return List of result
	 * @throws SQLException Test if something went wrong with the database
	 */
	protected List<T> findWithNamedQuery(String namedQuery, Map<Object, Object> parameters, int start, int offset)
			throws SQLException{
		Query query = mEntityManager.createNamedQuery(namedQuery);
		if (parameters != null && !parameters.isEmpty()) {
			Set<Map.Entry<Object, Object>> rawParameters = parameters.entrySet();
			for (Map.Entry entry : rawParameters) {
				query.setParameter((String) entry.getKey(), entry.getValue());
			}
		}

		if(offset > 0) {
			query.setFirstResult(start).setMaxResults(offset);
		}

		return (List<T>) query.getResultList();
	}
}
