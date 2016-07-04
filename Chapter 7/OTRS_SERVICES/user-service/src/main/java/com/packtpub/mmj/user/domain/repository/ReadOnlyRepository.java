package com.packtpub.mmj.user.domain.repository;

import com.packtpub.mmj.user.domain.model.entity.Entity;
import java.util.Collection;

/**
 *
 * @author Sourabh Sharma
 * @param <TE>
 * @param <T>
 */
public interface ReadOnlyRepository<TE, T> {

    //long Count;
    /**
     *
     * @param id
     * @return
     */
    boolean contains(T id);

    /**
     *
     * @param id
     * @return
     */
    Entity get(T id);

    /**
     *
     * @return
     */
    Collection<TE> getAll();
}
