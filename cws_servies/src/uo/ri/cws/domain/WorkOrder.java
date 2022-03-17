package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.Invoice.InvoiceStatus;
import uo.ri.cws.domain.base.BaseEntity;


public class WorkOrder extends BaseEntity {
	

	public enum WorkOrderStatus {
		OPEN, ASSIGNED, FINISHED, INVOICED
	}

	private LocalDateTime date;
	private String description;
	private double amount = 0.0;
	private WorkOrderStatus status = WorkOrderStatus.OPEN;

	private boolean usedforvoucher;

	private Vehicle vehicle;
	private Mechanic mechanic;
	private Invoice invoice;
	private Set<Intervention> interventions = new HashSet<>();

	/**
	 * Mapper demands a default constructor
	 */

	/* package */ WorkOrder() {

	}

	public WorkOrder(Vehicle vehicle) {
		this(vehicle, LocalDateTime.now(), "no-description");
	}

	public WorkOrder(Vehicle vehicle, String description) {

		this(vehicle, LocalDateTime.now(), description);

	}

	public WorkOrder(Vehicle vehicle, LocalDateTime date, String description) {

		ArgumentChecks.isNotNull(vehicle);
		ArgumentChecks.isNotNull(date);
		this.description = description;
		this.date = date.truncatedTo(ChronoUnit.MILLIS);
		this.usedforvoucher = false;
		Associations.Fix.link(vehicle, this);

	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(date, vehicle);
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
		WorkOrder other = (WorkOrder) obj;
		return Objects.equals(date, other.date)
				&& Objects.equals(vehicle, other.vehicle);
	}

	/**
	 * A voucher cannot be used to generate voucher if: ->It has already been
	 * used ->It is not invoiced ->The invoice is not paid
	 * 
	 * @return
	 */
	public boolean canBeUsedForVoucher() {
		if (this.usedforvoucher || !this.getStatus()
				.equals(WorkOrderStatus.INVOICED)) {
			return false;
		} else if (this.invoice == null) {
			return false;
		} else if (!this.invoice.getStatus()
				.equals(InvoiceStatus.PAID)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Changes it to INVOICED state given the right conditions This method is
	 * called from Invoice.addWorkOrder(...)
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not FINISHED, or -
	 *                               The work order is not linked with the
	 *                               invoice
	 */
	public void markAsInvoiced() {

		if (!this.status.equals(WorkOrderStatus.FINISHED)
				|| this.invoice == null) {
			throw new IllegalStateException("Cannot mark as invoiced");
		}
		this.status = WorkOrderStatus.INVOICED;
	}

	/**
	 * Changes it to FINISHED state given the right conditions and computes the
	 * amount
	 *
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not in ASSIGNED
	 *                               state, or - The work order is not linked
	 *                               with a mechanic
	 */
	public void markAsFinished() {

		if (!this.status.equals(WorkOrderStatus.ASSIGNED)
				|| this.mechanic == null) {
			throw new IllegalStateException(
					"Cannot mark workorder as finished");
		}

		this.status = WorkOrderStatus.FINISHED;
		this.amount = 0.0;
		for (Intervention inter : interventions) {

			this.amount += inter.getAmount();

		}

	}

	/**
	 * Changes it back to FINISHED state given the right conditions This method
	 * is called from Invoice.removeWorkOrder(...)
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not INVOICED, or -
	 *                               The work order is still linked with the
	 *                               invoice
	 */
	public void markBackToFinished() {
		if (!this.status.equals(WorkOrderStatus.INVOICED)
				|| this.invoice != null) {
			throw new IllegalStateException(
					"Cannot move workorder back to finished");
		}
		this.status = WorkOrderStatus.FINISHED;

	}

	/**
	 * Links (assigns) the work order to a mechanic and then changes its status
	 * to ASSIGNED
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not in OPEN status,
	 *                               or - The work order is already linked with
	 *                               another mechanic
	 */
	public void assignTo(Mechanic mechanic) {
		if (!this.status.equals(WorkOrderStatus.OPEN)
				|| this.mechanic != null) {
			throw new IllegalStateException();
		}
		Associations.Assign.link(mechanic, this);
		this.status = WorkOrderStatus.ASSIGNED;
	}

	/**
	 * Unlinks (deassigns) the work order and the mechanic and then changes its
	 * status back to OPEN
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not in ASSIGNED
	 *                               status
	 */
	public void desassign() {
		if (!this.status.equals(WorkOrderStatus.ASSIGNED)) {
			throw new IllegalStateException();
		}
		Associations.Assign.unlink(mechanic, this);
		this.status = WorkOrderStatus.OPEN;
	}

	/**
	 * In order to assign a work order to another mechanic is first have to be
	 * moved back to OPEN state and unlinked from the previous mechanic.
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not in FINISHED
	 *                               status
	 */
	public void reopen() {
		if (!this.status.equals(WorkOrderStatus.FINISHED)) {
			throw new IllegalStateException();
		}

		Associations.Assign.unlink(mechanic, this);
		this.status = WorkOrderStatus.OPEN;
	}

	public boolean isInvoiced() {
		return status.equals(WorkOrderStatus.INVOICED);
	}

	public boolean isFinished() {
		return status.equals(WorkOrderStatus.FINISHED);
	}

	public void markAsUsedForVoucher() {
		this.usedforvoucher = true;
	}

	public Set<Intervention> getInterventions() {
		return new HashSet<>(interventions);
	}

	Set<Intervention> _getInterventions() {
		return interventions;
	}

	void _setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	void _setInvoice(Invoice invoice) {
		this.invoice = invoice;

	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public double getAmount() {
		return amount;
	}

	public WorkOrderStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "WorkOrder [date=" + date + ", description=" + description
				+ ", amount=" + amount + ", status=" + status + ", vehicle="
				+ vehicle + "]";
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public Mechanic getMechanic() {
		return mechanic;
	}

	public Invoice getInvoice() {
		return invoice;
	}

}
