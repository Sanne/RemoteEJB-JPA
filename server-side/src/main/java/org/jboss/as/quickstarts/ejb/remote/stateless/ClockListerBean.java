package org.jboss.as.quickstarts.ejb.remote.stateless;

import java.util.List;
import java.util.Set;

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
import org.hibernate.test.bytecode.enhancement.lazy.proxy.Payment;

@Stateless
@Remote(ClockLister.class)
public class ClockListerBean implements ClockLister {

    @PersistenceContext(name = "primary")
    public EntityManager em;

    @Override
    @Transactional
    public void verifyOrdersLoading() {
        final Session session = em.unwrap(Session.class);

        final List<Order> orders = session.createQuery("select o from Order o", Order.class).list();
        for (Order order : orders) {
            System.out.println("Got Order#customer: " + order.getCustomer().getOid());

            if (order.getCustomer().getAddress() == null) {
                throw new RuntimeException("Failed assertions");
            }

            final PersistentAttributeInterceptable interceptable = (PersistentAttributeInterceptable) order.getCustomer().getAddress();
            final PersistentAttributeInterceptor interceptor = interceptable.$$_hibernate_getInterceptor();

            //Following class is not part of any Hibernate ORM release; expect the server to use the experimental patches:
            try {
                final Class<?> proxyLazinessInterceptor = Class.forName("org.hibernate.bytecode.enhance.spi.interceptor.EnhancementAsProxyLazinessInterceptor");
                if ( ! proxyLazinessInterceptor.isAssignableFrom( interceptor.getClass() ) ) {
                    throw new RuntimeException("Not an EnhancementAsProxyLazinessInterceptor");
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not load class EnhancementAsProxyLazinessInterceptor !");
            }
        }
    }

    @Override
    @Transactional
    public void verifyOrdersComplexLoading() {
        final Session session = em.unwrap(Session.class);

        StatementsCountChecker scc = new StatementsCountChecker(session);

        final List<Order> orders = session.createQuery("select o from Order o", Order.class).list();
        scc.verifyIncrement(1);

        for (Order order : orders) {
            // accessing the many-to-one's id should not trigger a load
            order.getCustomer().getOid();
            scc.verifyIncrement(0);

            // accessing the one-to-many should trigger a load
            final Set<Payment> orderPayments = order.getPayments();

            //Invoke size() to trigger initialization of the set
            orderPayments.size();
            scc.verifyIncrement(1);

            // access the non-inverse, logical 1-1
            order.getSupplemental();
            scc.verifyIncrement(0);
            if (order.getSupplemental() != null) {
                scc.verifyIncrement(0);
            }

            // access the inverse, logical 1-1
            order.getSupplemental2();
            scc.verifyIncrement(1);

            if (order.getSupplemental2() != null) {
                scc.verifyIncrement(0);
            }
        }
    }

    @Override
    @Transactional
    public void testDataSetup() {

        final Session session = em.unwrap(Session.class);

        final Address austin = new Address(1, "Austin");
        final Address london = new Address(2, "London");

        session.save(austin);
        session.save(london);

        final ForeignCustomer acme = new ForeignCustomer(1, "Acme", london, "1234");
        final ForeignCustomer acmeBrick = new ForeignCustomer(2, "Acme Brick", london, "9876", acme);

        final ForeignCustomer freeBirds = new ForeignCustomer(3, "Free Birds", austin, "13579");

        session.save(acme);
        session.save(acmeBrick);
        session.save(freeBirds);

        final Order order1 = new Order(1, "some text", freeBirds);
        freeBirds.getOrders().add(order1);
        session.save(order1);

        final OrderSupplemental orderSupplemental = new OrderSupplemental(1, 1);
        order1.setSupplemental(orderSupplemental);
        final OrderSupplemental2 orderSupplemental2_1 = new OrderSupplemental2(2, 2);
        order1.setSupplemental2(orderSupplemental2_1);
        orderSupplemental2_1.setOrder(order1);
        session.save(orderSupplemental);
        session.save(orderSupplemental2_1);

        final Order order2 = new Order(2, "some text", acme);
        acme.getOrders().add(order2);
        session.save(order2);

        final OrderSupplemental2 orderSupplemental2_2 = new OrderSupplemental2(3, 3);
        order2.setSupplemental2(orderSupplemental2_2);
        orderSupplemental2_2.setOrder(order2);
        session.save(orderSupplemental2_2);

        final CreditCardPayment payment1 = new CreditCardPayment(1, 1F, "1");
        session.save(payment1);
        order1.getPayments().add(payment1);

        final DebitCardPayment payment2 = new DebitCardPayment(2, 2F, "2");
        session.save(payment2);
        order1.getPayments().add(payment2);

    }

    @Override
    @Transactional
    public void testDataCleanup(){
        em.createQuery( "delete from CreditCardPayment" ).executeUpdate();
        em.createQuery( "delete from DebitCardPayment" ).executeUpdate();
        em.createQuery( "delete from OrderSupplemental2" ).executeUpdate();
        em.createQuery( "delete from Order" ).executeUpdate();
        em.createQuery( "delete from OrderSupplemental" ).executeUpdate();
        em.createQuery( "delete from DomesticCustomer" ).executeUpdate();
        em.createQuery( "delete from ForeignCustomer" ).executeUpdate();
        em.createQuery( "delete from Address" ).executeUpdate();
    }

}
