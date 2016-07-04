package com.packtpub.mmj.common;

import java.util.Map;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class MDCConcurrentCallable<K> implements Callable {

    private static final Logger LOG = LoggerFactory.getLogger(MDCConcurrentCallable.class);

    private final Callable<K> actual;
    private final Map parentMDC;

    public MDCConcurrentCallable(Callable<K> actual) {
        LOG.debug("Init MDCHystrixContextCallable...");
        this.actual = actual;
        this.parentMDC = MDC.getCopyOfContextMap();
        LOG.debug("actual --> " + actual);
        LOG.debug("this.parentMDC --> " + this.parentMDC);
    }

    @Override
    public K call() throws Exception {
        LOG.debug("Call using MDCHystrixContextCallable...");
        Map childMDC = MDC.getCopyOfContextMap();
        LOG.debug("childMDC --> " + childMDC);
        try {
            if (parentMDC != null) {
                MDC.setContextMap(parentMDC);
            }
            LOG.debug("parentMDC --> " + parentMDC);
            return actual.call();
        } finally {
            if (childMDC != null) {
                MDC.setContextMap(childMDC);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Non-Executable");
    }
}
