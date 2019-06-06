package org.jboss.as.quickstarts.ejb.remote.stateless;

import java.util.List;

import org.jboss.as.quickstarts.model.Clock;

public interface ClockLister {

    /**
     * Trivial operation to run some sanity checks
     * @return
     */
    List<Clock> listAllClocks();

    /**
     * Trivial operation to run some sanity checks
     * @return
     */
    void storeNewClock(Clock newinstance);

    /**
     * Perform verification of enhancers being enabled
     */
    void verifyOrdersLoading();

    void testDataSetup();
}
