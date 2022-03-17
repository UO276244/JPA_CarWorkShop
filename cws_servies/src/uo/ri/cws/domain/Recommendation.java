package uo.ri.cws.domain;

import java.util.Objects;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.base.BaseEntity;

public class Recommendation extends BaseEntity {

	public Recommendation(Client sponsor, Client recommended) {

		ArgumentChecks.isNotNull(recommended);
		ArgumentChecks.isNotNull(sponsor);

		this.usedforvoucher = false;
		Associations.Recommend.link(this, sponsor, recommended);
	}

	Recommendation() {

	}

	private Client sponsor;

	private Client recommended;

	private boolean usedforvoucher;

	public boolean isusedforvoucher() {
		return this.usedforvoucher;
	}

	public void markAsUsed() {
		this.usedforvoucher = true;
	}

	/**
	 * Is elegible when it is not used and the recommended client has at least
	 * one workorder, and if the work order is paid
	 * 
	 * @return
	 */
	public boolean isElgible() {
		if (!this.usedforvoucher) {

			for (Vehicle v : this.recommended.getVehicles()) {
				for (WorkOrder wo : v.getWorkOrders()) {
					if (wo.isInvoiced() && wo.getInvoice().isSettled())
						return true;
				}
			}

		}

		return false;
	}

	public Client getSponsor() {
		return sponsor;
	}

	public Client getRecommended() {
		return recommended;
	}

	void _setSponsor(Client sponsor) {
		this.sponsor = sponsor;
	}

	void _setRecommended(Client recommended) {
		this.recommended = recommended;
	}

	@Override
	public String toString() {
		return "Recommendation [sponsor=" + sponsor + ", recommended="
				+ recommended + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(recommended, sponsor);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recommendation other = (Recommendation) obj;
		return Objects.equals(recommended, other.recommended)
				&& Objects.equals(sponsor, other.sponsor);
	}
}
