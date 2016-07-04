package com.packtpub.mmj.user.domain.repository;

import java.util.Collection;

/**
 *
 * @author Sourabh Sharma
 * @param <User>
 * @param <String>
 */
public interface UserRepository<Booking, String> extends Repository<Booking, String> {

    /**
     *
     * @param name
     * @return
     */
    boolean containsName(String name);

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Collection<Booking> findByName(String name) throws Exception;
}
