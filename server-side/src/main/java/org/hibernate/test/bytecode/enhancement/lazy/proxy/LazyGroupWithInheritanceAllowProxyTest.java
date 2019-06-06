/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.test.bytecode.enhancement.lazy.proxy;
/*
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.bytecode.enhance.spi.interceptor.EnhancementAsProxyLazinessInterceptor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;
import org.hibernate.stat.Statistics;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.bytecode.enhancement.BytecodeEnhancerRunner;
import org.hibernate.testing.bytecode.enhancement.CustomEnhancementContext;
import org.hibernate.testing.bytecode.enhancement.EnhancementOptions;
import org.hibernate.testing.bytecode.enhancement.EnhancerTestContext;
import org.hibernate.testing.junit4.BaseNonConfigCoreFunctionalTestCase;
import org.hibernate.test.bytecode.enhancement.lazy.group.BidirectionalLazyGroupsInEmbeddableTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;*/

public class LazyGroupWithInheritanceAllowProxyTest /*extends BaseNonConfigCoreFunctionalTestCase*/ {
/*

	@Test
	public void baseline() {
		inTransaction(
				session -> {
					final List<Order> orders = session.createQuery( "select o from Order o", Order.class ).list();
					for ( Order order : orders ) {
						if ( order.getCustomer().getOid() == null ) {
							System.out.println( "Got Order#customer: " + order.getCustomer().getOid() );
						}

					}

				}
		);
	}

	@Test
	public void testMergingUninitializedProxy() {
		inTransaction(
				session -> {
					final List<Order> orders = session.createQuery( "select o from Order o", Order.class ).list();
					for ( Order order : orders ) {
						System.out.println( "Got Order#customer: " + order.getCustomer().getOid() );
						assertThat( order.getCustomer().getAddress(), notNullValue() );
						final PersistentAttributeInterceptable interceptable = (PersistentAttributeInterceptable) order.getCustomer().getAddress();
						final PersistentAttributeInterceptor interceptor = interceptable.$$_hibernate_getInterceptor();
						assertThat( interceptor, instanceOf( EnhancementAsProxyLazinessInterceptor.class ) );
					}
				}
		);
	}


	@Test
	public void queryEntityWithAssociationToAbstract() {

		inTransaction(
				session -> {
		final Statistics stats = sessionFactory().getStatistics();
		stats.clear();

		final AtomicInteger expectedQueryCount = new AtomicInteger( 0 );
					final List<Order> orders = session.createQuery( "select o from Order o", Order.class ).list();

					expectedQueryCount.set( 1 );

					assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );

					for ( Order order : orders ) {
						System.out.println( "############################################" );
						System.out.println( "Starting Order #" + order.getOid() );

						// accessing the many-to-one's id should not trigger a load
						if ( order.getCustomer().getOid() == null ) {
							System.out.println( "Got Order#customer: " + order.getCustomer().getOid() );
						}
						assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );

						// accessing the one-to-many should trigger a load
						final Set<Payment> orderPayments = order.getPayments();
						System.out.println( "Number of payments = " + orderPayments.size() );
						expectedQueryCount.getAndIncrement();
						assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );

						// access the non-inverse, logical 1-1
						order.getSupplemental();
						assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );
						if ( order.getSupplemental() != null ) {
							System.out.println( "Got Order#supplemental = " + order.getSupplemental().getOid() );
							assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );
						}

						// access the inverse, logical 1-1
						order.getSupplemental2();
						expectedQueryCount.getAndIncrement();
						assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );
						if ( order.getSupplemental2() != null ) {
							System.out.println( "Got Order#supplemental2 = " + order.getSupplemental2().getOid() );
							assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );
						}
					}
				}
		);
	}

	*/
/**
	 * Same test as {@link #queryEntityWithAssociationToAbstract()}, but using runtime
	 * fetching to issues just a single select
	 *//*

	@Test
	public void queryEntityWithAssociationToAbstractRuntimeFetch() {
		final Statistics stats = sessionFactory().getStatistics();
		stats.clear();

		final AtomicInteger expectedQueryCount = new AtomicInteger( 0 );

		inTransaction(
				session -> {
					final String qry = "select o from Order o join fetch o.customer c join fetch o.payments join fetch o.supplemental join fetch o.supplemental2";

					final List<Order> orders = session.createQuery( qry, Order.class ).list();

					// oh look - just a single query for all the data we will need.  hmm, crazy
					expectedQueryCount.set( 1 );
					assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );

					for ( Order order : orders ) {
						System.out.println( "############################################" );
						System.out.println( "Starting Order #" + order.getOid() );

						// accessing the many-to-one's id should not trigger a load
						if ( order.getCustomer().getOid() == null ) {
							System.out.println( "Got Order#customer: " + order.getCustomer().getOid() );
						}
						assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );

						// accessing the one-to-many should trigger a load
						final Set<Payment> orderPayments = order.getPayments();
						System.out.println( "Number of payments = " + orderPayments.size() );

						// loaded already
						// expectedQueryCount.getAndIncrement();
						assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );

						// access the non-inverse, logical 1-1
						order.getSupplemental();
						assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );
						if ( order.getSupplemental() != null ) {
							System.out.println( "Got Order#supplemental = " + order.getSupplemental().getOid() );
							assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );
						}

						// access the inverse, logical 1-1
						order.getSupplemental2();

						// loaded already
						// expectedQueryCount.getAndIncrement();
						assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );
						if ( order.getSupplemental2() != null ) {
							System.out.println( "Got Order#supplemental2 = " + order.getSupplemental2().getOid() );
							assertEquals( expectedQueryCount.get(), stats.getPrepareStatementCount() );
						}
					}
				}
		);
	}

	@After
	public void cleanUpTestData() {
		inTransaction(
				session -> {
					session.createQuery( "delete from CreditCardPayment" ).executeUpdate();
					session.createQuery( "delete from DebitCardPayment" ).executeUpdate();

					session.createQuery( "delete from OrderSupplemental2" ).executeUpdate();

					session.createQuery( "delete from Order" ).executeUpdate();

					session.createQuery( "delete from OrderSupplemental" ).executeUpdate();

					session.createQuery( "delete from DomesticCustomer" ).executeUpdate();
					session.createQuery( "delete from ForeignCustomer" ).executeUpdate();

					session.createQuery( "delete from Address" ).executeUpdate();
				}
		);
	}

	@Override
	protected void applyMetadataSources(MetadataSources sources) {
		super.applyMetadataSources( sources );

		sources.addAnnotatedClass( Customer.class );
		sources.addAnnotatedClass( ForeignCustomer.class );
		sources.addAnnotatedClass( DomesticCustomer.class );

		sources.addAnnotatedClass( Payment.class );
		sources.addAnnotatedClass( CreditCardPayment.class );
		sources.addAnnotatedClass( DebitCardPayment.class );

		sources.addAnnotatedClass( Address.class );

		sources.addAnnotatedClass( Order.class );
		sources.addAnnotatedClass( OrderSupplemental.class );
		sources.addAnnotatedClass( OrderSupplemental2.class );
	}
*/

}
