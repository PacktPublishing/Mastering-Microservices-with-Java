package com.packtpub.mmj.restsample.model;

import java.util.List;

/**
 *
 * @author sousharm
 */
public class Calculation {

    String function;
    private List<String> input;
    private List<String> output;

    /**
     *
     * @param input
     * @param output
     * @param function
     */
    public Calculation(List<String> input, List<String> output, String function) {
        this.function = function;
        this.input = input;
        this.output = output;
    }

    /**
     *
     * @return
     */
    public List<String> getInput() {
        return input;
    }

    /**
     *
     * @param input
     */
    public void setInput(List<String> input) {
        this.input = input;
    }

    /**
     *
     * @return
     */
    public List<String> getOutput() {
        return output;
    }

    /**
     *
     * @param output
     */
    public void setOutput(List<String> output) {
        this.output = output;
    }

    /**
     *
     * @return
     */
    public String getFunction() {
        return function;
    }

    /**
     *
     * @param function
     */
    public void setFunction(String function) {
        this.function = function;
    }

}
