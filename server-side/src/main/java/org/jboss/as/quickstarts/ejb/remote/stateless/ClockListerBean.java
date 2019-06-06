package org.jboss.as.quickstarts.ejb.remote.stateless;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;
import org.hibernate.test.bytecode.enhancement.lazy.proxy.Address;
import org.hibernate.test.bytecode.enhancement.lazy.proxy.CreditCardPayment;
import org.hibernate.test.bytecode.enhancement.lazy.proxy.DebitCardPayment;
import org.hibernate.test.bytecode.enhancement.lazy.proxy.ForeignCustomer;
import org.hibernate.test.bytecode.enhancement.lazy.proxy.Order;
import org.hibernate.test.bytecode.enhancement.lazy.proxy.OrderSupplemental;
import org.hibernate.test.bytecode.enhancement.lazy.proxy.OrderSupplemental2;
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

    @Override
    public void verifyOrdersLoading() {

        final Session session = em.unwrap(Session.class);
        boolean failed = false;

        final List<Order> orders = session.createQuery( "select o from Order o", Order.class ).list();
        for ( Order order : orders ) {
            System.out.println( "Got Order#customer: " + order.getCustomer().getOid() );

            if ( order.getCustomer().getAddress() == null) {
                failed = true;
            }

            final PersistentAttributeInterceptable interceptable = (PersistentAttributeInterceptable) order.getCustomer().getAddress();
            final PersistentAttributeInterceptor interceptor = interceptable.$$_hibernate_getInterceptor();
//            assertThat( interceptor, instanceOf( EnhancementAsProxyLazinessInterceptor.class ) );
        }

        if (failed) {
            throw new RuntimeException("Failed assertions");
        }

    }

    @Override
    @Transactional
    public void testDataSetup() {

        final Session session = em.unwrap(Session.class);

        final Address austin = new Address( 1, "Austin" );
        final Address london = new Address( 2, "London" );

        session.save( austin );
        session.save( london );

        final ForeignCustomer acme = new ForeignCustomer( 1, "Acme", london, "1234" );
        final ForeignCustomer acmeBrick = new ForeignCustomer( 2, "Acme Brick", london, "9876", acme );

        final ForeignCustomer freeBirds = new ForeignCustomer( 3, "Free Birds", austin, "13579" );

        session.save( acme );
        session.save( acmeBrick );
        session.save( freeBirds );

        final Order order1 = new Order( 1, "some text", freeBirds );
        freeBirds.getOrders().add( order1 );
        session.save( order1 );

        final OrderSupplemental orderSupplemental = new OrderSupplemental( 1, 1 );
        order1.setSupplemental( orderSupplemental );
        final OrderSupplemental2 orderSupplemental2_1 = new OrderSupplemental2( 2, 2 );
        order1.setSupplemental2( orderSupplemental2_1 );
        orderSupplemental2_1.setOrder( order1 );
        session.save( orderSupplemental );
        session.save( orderSupplemental2_1 );

        final Order order2 = new Order( 2, "some text", acme );
        acme.getOrders().add( order2 );
        session.save( order2 );

        final OrderSupplemental2 orderSupplemental2_2 = new OrderSupplemental2( 3, 3 );
        order2.setSupplemental2( orderSupplemental2_2 );
        orderSupplemental2_2.setOrder( order2 );
        session.save( orderSupplemental2_2 );

        final CreditCardPayment payment1 = new CreditCardPayment( 1, 1F, "1" );
        session.save( payment1 );
        order1.getPayments().add( payment1 );

        final DebitCardPayment payment2 = new DebitCardPayment( 2, 2F, "2" );
        session.save( payment2 );
        order1.getPayments().add( payment2 );

    }

}
