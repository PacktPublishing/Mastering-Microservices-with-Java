package com.packtpub.mmj.user.domain.service;

import com.packtpub.mmj.user.domain.model.entity.User;
import com.packtpub.mmj.user.domain.model.entity.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Sourabh Sharma
 */
public interface UserService {

    /**
     *
     * @param booking
     * @throws Exception
     */
    public void add(User booking) throws Exception;

    /**
     *
     * @param booking
     * @throws Exception
     */
    public void update(User booking) throws Exception;

    /**
     *
     * @param id
     * @throws Exception
     */
    public void delete(String id) throws Exception;

    /**
     *
     * @param restaurantId
     * @return
     * @throws Exception
     */
    public Entity findById(String restaurantId) throws Exception;

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Collection<User> findByName(String name) throws Exception;

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Collection<User> findByCriteria(Map<String, ArrayList<String>> name) throws Exception;
}
