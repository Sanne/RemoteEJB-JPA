package org.jboss.as.quickstarts.ejb.remote.stateless;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.as.quickstarts.model.Clock;

@Stateless
@Remote(ClockLister.class)
public class ClockListerBean implements ClockLister {

    @PersistenceContext(name = "primary")
    public EntityManager em;

    @Override
    public List<Clock> listAllClocks() {
        return em.createQuery("from Clock").getResultList();
    }

    @Override
    public void storeNewClock(final Clock newinstance) {
        em.persist(newinstance);
    }

}
