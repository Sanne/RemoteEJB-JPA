/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.test.bytecode.enhancement.lazy.proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Steve Ebersole
 */
@Entity(name = "OrderSupplemental2")
@Table(name = "order_supp2")
public class OrderSupplemental2 extends Model {
	private Integer receivablesId;

	private Order order;

	public OrderSupplemental2() {
	}

	public OrderSupplemental2(Integer oid, Integer receivablesId) {
		super.setId( oid );
		this.receivablesId = receivablesId;
	}

	public Integer getReceivablesId() {
		return receivablesId;
	}

	public void setReceivablesId(Integer receivablesId) {
		this.receivablesId = receivablesId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
