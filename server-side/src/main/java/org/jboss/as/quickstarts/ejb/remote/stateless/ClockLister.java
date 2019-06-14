package org.jboss.as.quickstarts.ejb.remote.stateless;

public interface ClockLister {

    /**
     * Perform verification of enhancers being enabled
     */
    void verifyOrdersLoading();

    void verifyOrdersComplexLoading();

    void callSetIdOnAEnhancedProxy();

    void testDataSetup();

    void testDataCleanup();
}
