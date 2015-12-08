package net.dkahn.starter.tools.repository.jpa;

import net.dkahn.starter.tools.repository.IGenericRepository;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.path.PathBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericRepositoryJpa<T, PK extends Serializable> implements IGenericRepository<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;
    private SessionFactory sessionFactory;

    private Querydsl querydsl;


    @PersistenceContext
    private javax.persistence.EntityManager entityManager;

    /**
     * Encapulate EntityManger for Futur use
     * @return
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public JPAQuery createQuery() {
        return new JPAQuery(getEntityManager());
    }

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing.
     *
     * @param persistentClass the class type you'd like to persist
     */
    public GenericRepositoryJpa(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }


    public GenericRepositoryJpa() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }



    @PostConstruct
    private void initQueryDsl(){
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<T> path = resolver.createPath(persistentClass);
        PathBuilder<T> builder = new PathBuilder<T>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    /**
     * Obtain the session factory for work with session directly
     */
    @Deprecated
    public SessionFactory getSessionFactory() {
        Session session = (Session) entityManager.getDelegate();
        return session.getSessionFactory();
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return this.entityManager.createQuery(
                "select obj from " + this.persistentClass.getName() + " obj")
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Set<T> getAllDistinct() {
        return new LinkedHashSet(getAll());
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        T entity = this.entityManager.find(this.persistentClass, id);

        if (entity == null) {
            String msg = "Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...";
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(PK id) {
        T entity = this.entityManager.find(this.persistentClass, id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        return this.entityManager.merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T object) {
        this.entityManager.remove(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        this.entityManager.remove(this.get(id));
    }


    @Override
    public JPQLQuery applyPagination(Pageable pageable, JPQLQuery query) {
        return querydsl.applyPagination(pageable, query);
    }

    public void flush() {
        this.entityManager.flush();
    }

    @Override
    public List<T> getAllIn(List<PK> ids) {
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<T> path = resolver.createPath(persistentClass);
        PathBuilder<T> builder = new PathBuilder<T>(path.getType(), path.getMetadata());


        JPAQuery query = createQuery();
        query.from(path);

        query.where(builder.get("id").in(ids));
        return query.list(path);
    }

    @Override
    public JPQLQuery applySorting(Sort sort, JPQLQuery query) {
        return querydsl.applySorting(sort, query);
    }



    @Override
    public Long count(){
        JPAQuery query = createQuery();

        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<T> path = resolver.createPath(persistentClass);
        PathBuilder<T> builder = new PathBuilder<T>(path.getType(), path.getMetadata());

        query.from(builder);
        return query.count();
    }

}
