package org.jboss.as.quickstarts.ejb.remote.stateless;

import java.util.List;

import org.jboss.as.quickstarts.model.Clock;

public interface ClockLister {
    List<Clock> listAllClocks();
    void storeNewClock(Clock newinstance);
}
