package com.packtpub.mmj.mcrsrvc.domain.model;

import java.math.BigInteger;

/**
 *
 * @author Sourabh Sharma
 */
public class Table extends BaseEntity<BigInteger> {

    private int capacity;

    /**
     *
     * @param name
     * @param id
     * @param capacity
     */
    public Table(String name, BigInteger id, int capacity) {
        super(id, name);
        this.capacity = capacity;
    }

    /**
     *
     * @param capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     *
     * @return
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Overridden toString() method that return String presentation of the
     * Object
     *
     * @return
     */
    @Override
    public String toString() {
        return new StringBuilder("{id: ").append(id).append(", name: ")
                .append(name).append(", capacity: ").append(capacity).append("}").toString();
    }

}
