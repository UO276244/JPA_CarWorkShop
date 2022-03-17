package uo.ri.cws.domain;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.base.BaseEntity;

//@Entity
//@Table(name = "TSubstitutions", uniqueConstraints = {
//		@UniqueConstraint(columnNames = { "SPAREPART_ID", "INTERVENTION_ID" })
//// Name
//// of
//// columns,
//// no
//// attributes
//
//// As vehicle is a reference to other table, name of field column
//// here in uniqueConstraint should be NAMEATTRIBU_PKonTableReferenced
//})
public class Substitution extends BaseEntity {
	// natural attributes
	private int quantity;

	// accidental attributes
	//@ManyToOne
	private SparePart sparePart;
	//@ManyToOne
	private Intervention intervention;

	Substitution() {

	}

	public Substitution(SparePart sparePart, Intervention intervention) {
		this(sparePart, intervention, 1);
	}

	public Substitution(SparePart sparePart, Intervention intervention,
			int quantity) {

		ArgumentChecks.isNotNull(intervention);
		ArgumentChecks.isNotNull(sparePart);
		ArgumentChecks.isTrue(quantity > 0);
		this.quantity = quantity;

		Associations.Sustitute.link(sparePart, this, intervention);

	}

	void _setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	void _setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}

	public int getQuantity() {
		return quantity;
	}

	public SparePart getSparePart() {
		return sparePart;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	@Override
	public String toString() {
		return "Substitution [quantity=" + quantity + ", sparePart=" + sparePart
				+ ", intervention=" + intervention + "]";
	}

	public double getAmount() {
		return this.quantity * this.sparePart.getPrice();
	}

}
