package net.dkahn.starter.tools.service.impl;

import net.dkahn.starter.tools.repository.IGenericRepository;
import net.dkahn.starter.tools.service.GenericService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericServiceImpl<T, PK extends Serializable> implements GenericService<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * IGenericRepository instance, set by constructor of child classes
     */
    protected IGenericRepository<T, PK> repository;

    public GenericServiceImpl() {}

    public GenericServiceImpl(IGenericRepository<T, PK> repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<T> getAll() {
        return repository.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public T get(PK id) {
        return repository.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public boolean exists(PK id) {
        return repository.exists(id);
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        return repository.save(object);
    }

    /**
     * {@inheritDoc}
     */

    @Transactional
    public void remove(PK id) {
        repository.remove(id);
    }
}
