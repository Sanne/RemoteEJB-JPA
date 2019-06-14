/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.test.bytecode.enhancement.lazy.proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author Steve Ebersole
 */
@Entity(name = "Payment")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Payment extends Model {
	private Float amount;

	public Payment() {
	}

	public Payment(Integer oid, Float amount) {
		super.setId( oid );
		this.amount = amount;
	}


	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
