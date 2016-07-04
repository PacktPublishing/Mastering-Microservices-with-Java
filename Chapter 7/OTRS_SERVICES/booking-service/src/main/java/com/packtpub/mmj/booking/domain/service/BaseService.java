package com.packtpub.mmj.booking.domain.service;

import com.packtpub.mmj.booking.domain.repository.Repository;
import java.util.Collection;

/**
 *
 * @author Sourabh Sharma
 * @param <TE>
 * @param <T>
 */
public abstract class BaseService<TE, T> extends ReadOnlyBaseService<TE, T> {

    private Repository<TE, T> _repository;

    BaseService(Repository<TE, T> repository) {
        super(repository);
        _repository = repository;
    }

    /**
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public TE add(TE entity) throws Exception {
        return _repository.add(entity);
    }

    /**
     *
     * @return
     */
    public Collection<TE> getAll() {
        return _repository.getAll();
    }
}
