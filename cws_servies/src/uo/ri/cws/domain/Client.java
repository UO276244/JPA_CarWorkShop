package uo.ri.cws.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.Invoice.InvoiceStatus;
import uo.ri.cws.domain.WorkOrder.WorkOrderStatus;
import uo.ri.cws.domain.base.BaseEntity;

public class Client extends BaseEntity {

	private String dni; 
	
	private String name;
	private String surname;
	private String email;
	private String phone;
	private Address address;

	private Set<Vehicle> vehicles = new HashSet<>();

	private Set<PaymentMean> paymentMeans = new HashSet<>();

	private Set<Recommendation> sponsored = new HashSet<>();

	private Recommendation recommended;

	/**
	 * Mapper demands a default constructor
	 */
	/* package */ Client() {

	}

	public Client(String dni) {

		this(dni, "no-name", "no-surname");

	}

	public Client(String dni, String name, String surname) {

		this(dni,name,surname,"no-email","no-phone",new Address(), null);
	}
	
	public Client(String dni, String name, String surname, Client sponsor) {
		this(dni,name,surname,"no-email","no-phone",new Address(), sponsor);
	}

	public Client(String dni, String name, String surname, String email,
			String phone, Address address, Client sponsor) {
		
		
		
		ArgumentChecks.isNotNull(dni);
		ArgumentChecks.isNotEmpty(dni);

		ArgumentChecks.isNotNull(name);
		ArgumentChecks.isNotEmpty(name);
		ArgumentChecks.isNotNull(surname);
		ArgumentChecks.isNotEmpty(surname);

		this.dni = dni;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		if (sponsor != null) {
			Associations.Recommend.link(recommended, sponsor, this);
		}
		
		

	}

	// FOR EXTENSION

	// FOR EXTENSION
	public List<WorkOrder> getWorkOrdersAvailableForVoucher() {

		List<WorkOrder> clientWorkOrders = new ArrayList<WorkOrder>();

		for (Vehicle v : this.vehicles) {
			for (WorkOrder w : v.getWorkOrders()) {

				// If the workorder is not in the list, and it is invoiced,
				// the invoice is paid, and it has been not used for voucher,
				// then add to the list
				if (!clientWorkOrders.contains(w) && w.canBeUsedForVoucher()) {
					clientWorkOrders.add(w);
				}
			}
		}

		return clientWorkOrders;

	}

	/*
	 * A client is elegible if it has at least 3 non-used for voucher
	 * recommendatios, the client has paid all his/her workorders and the
	 * recommended clients have at least one aid workorder
	 */
	public boolean eligibleForRecommendationVoucher() {

		// If less than 3 recommendations are made, or no vehicles are own,
		// return false
		Set<Recommendation> recommendationsMade = this.getSponsored();
		if (this.getVehicles().isEmpty() || recommendationsMade.size() < 3) {
			return false;
		}

		// If there is at least one workorder not paid, return false
		boolean hasAllWorkOrdersInvoicedPaid = true;
		for (Vehicle v : this.getVehicles()) {
			for (WorkOrder w : v.getWorkOrders()) {
				if (!w.getStatus().equals(WorkOrderStatus.INVOICED)
						|| (w.getStatus()
								.equals(WorkOrderStatus.INVOICED)
								&& w.getInvoice() != null
								&& w.getInvoice().getStatus()
										.equals(InvoiceStatus.NOT_YET_PAID
												))) {
					hasAllWorkOrdersInvoicedPaid = false;
				}
			}
		}

		if (!hasAllWorkOrdersInvoicedPaid) {
			return false;
		}

		// If there are not at least three elegible recommendations
		// return false
		Set<Recommendation> recommendationsElegible = new HashSet<>();
		;
		for (Recommendation r : recommendationsMade) {
			if (r.isElgible()) {
				recommendationsElegible.add(r);
			}
		}

		if (recommendationsElegible.size() < 3) {
			return false;
		}

		return true;
	}

	public List<Recommendation> elegibleFor3RecVoucher() {

		List<Recommendation> list = new ArrayList<Recommendation>();

		if (eligibleForRecommendationVoucher()) {
			for (Recommendation rec : sponsored) {
				if (rec.isElgible()) {
					list.add(rec);
				}
			}
		}

		return list;

	}

	public Set<Recommendation> getSponsored() {
		return new HashSet<>(sponsored);
	}

	/* package */ Set<Recommendation> _getSponsored() {
		return this.sponsored;
	}

	public Recommendation getRecommended() {
		return this.recommended;
	}

	/* package */ void _setRecommended(Recommendation recommendedBy) {
		this.recommended = recommendedBy;
	}

	/**
	 * Return copy
	 * 
	 * @return
	 */
	public Set<Vehicle> getVehicles() {
		return new HashSet<>(vehicles);
	}

	/* package */ Set<Vehicle> _getVehicles() {
		return vehicles;
	}

	@Override
	public String toString() {
		return "Client [dni=" + dni + ", name=" + name + ", surname=" + surname
				+ ", email=" + email + ", phone=" + phone + ", address="
				+ address + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getDni() {
		return dni;
	}

	public Set<PaymentMean> getPaymentMeans() {
		return new HashSet<>(paymentMeans);
	}

	/* package */ Set<PaymentMean> _getPaymentMeans() {
		return this.paymentMeans;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dni);
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
		Client other = (Client) obj;
		return Objects.equals(dni, other.dni);
	}

}
