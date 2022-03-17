package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import uo.ri.cws.domain.base.BaseEntity;

//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//@Table(name = "TPaymentmeans")
public abstract class PaymentMean extends BaseEntity {
	// natural attributes
	private double accumulated = 0.0;

	// accidental attributes
//	@ManyToOne
	private Client client;
	//@OneToMany(mappedBy = "paymentMean")
	private Set<Charge> charges = new HashSet<>();

	public double getAccumulated() {
		return accumulated;
	}

	public Client getClient() {
		return client;
	}

	public void pay(double importe) {
		if (this.isValid()) {
			this.accumulated += importe;
		} else {
			throw new IllegalStateException("Unable to pay with this mean");
		}

	}

	void _setClient(Client client) {
		this.client = client;
	}

	public Set<Charge> getCharges() {
		return new HashSet<>(charges);
	}

	Set<Charge> _getCharges() {
		return charges;
	}

	public boolean isValid() {
		return true;
	}

}
