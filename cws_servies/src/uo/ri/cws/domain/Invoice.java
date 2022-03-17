package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import alb.util.assertion.ArgumentChecks;
import alb.util.math.Round;
import uo.ri.cws.domain.base.BaseEntity;

public class Invoice extends BaseEntity {
	public enum InvoiceStatus {
		NOT_YET_PAID, PAID
	}

	private final double VAT_OLD = 0.18;

	private final double VAT_CURRENT = 0.21;

	private final double OVERPAID_LIMIT = 0.01;

	private final double UNDERPAID_LIMIT = 0.01;

	private Long number;
	private LocalDate date;
	private double amount;
	double vat;
	private InvoiceStatus status = InvoiceStatus.NOT_YET_PAID;

	// FOR EXTENSION
	private boolean usedforvoucher = false;

	private Set<WorkOrder> workOrders = new HashSet<>();

	private Set<Charge> charges = new HashSet<>();

	Invoice() {
	}

	public Invoice(Long number) {
		// call full constructor with sensible defaults
		this(number, LocalDate.now(), new ArrayList<WorkOrder>());
	}

	public Invoice(Long number, LocalDate date) {
		// call full constructor with sensible defaults
		this(number, date, new ArrayList<WorkOrder>());
	}

	public Invoice(Long number, List<WorkOrder> workOrders) {
		this(number, LocalDate.now(), workOrders);

	}

	// full constructor
	public Invoice(Long number, LocalDate date, List<WorkOrder> workOrders) {
		// check arguments (always), through IllegalArgumentException
		ArgumentChecks.isNotNull(number);
		ArgumentChecks.isNotNull(date);
		ArgumentChecks.isNotNull(workOrders);

		// store the number
		this.number = number;
		// store a copy of the date
		this.date = date;
		// add every work order calling addWorkOrder( w )
		

		if (date.isBefore(LocalDate.of(2012, 7, 1))) {
			this.vat = VAT_OLD;
		} else {
			this.vat = VAT_CURRENT;
		}
		
		for (WorkOrder wo : workOrders) { // =>Move this to method
			addWorkOrder(wo);
		}

	}

	// FOR EXTENSION

	public void markAsUsed() {

		if (!canGenerate500Voucher()) {
			throw new IllegalStateException(
					"Invoice does not fulfill conditios to generate voucher");
		}
		this.usedforvoucher = true;
	}

	public boolean isUsedForVoucher() {
		return this.usedforvoucher;
	}

	public boolean canGenerate500Voucher() {

		if (this.usedforvoucher
				|| !this.getStatus().equals(InvoiceStatus.PAID)
				|| this.getAmount() < 500) {

			return false;

		}

		return true;

	}

	public Long getNumber() {
		return number;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getAmount() {

		return this.amount;

	}

	public double getVat() {
		return vat;
	}

	public InvoiceStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Invoice [number=" + number + ", date=" + date + ", amount="
				+ amount + ", vat=" + vat + ", status=" + status + "]";
	}

	/**
	 * Computes amount and vat (vat depends on the date)
	 */
	private void computeAmount() {
		double amount = 0.0;
		for (WorkOrder wo : workOrders) {

			amount += wo.getAmount();
		}

		this.amount = Round.twoCents(amount * (1.0 + vat ))  ;

	}

	/**
	 * Adds (double links) the workOrder to the invoice and updates the amount
	 * and vat
	 * 
	 * @param workOrder
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
	 */
	public void addWorkOrder(WorkOrder workOrder) {
		if (!this.status.equals(InvoiceStatus.NOT_YET_PAID)) {
			throw new IllegalStateException(
					"Cannot link if invoice status is paid");
		}
		ArgumentChecks.isNotNull(workOrder);

		Associations.ToInvoice.link(this, workOrder);
		workOrder.markAsInvoiced();
		computeAmount();

	}

	/**
	 * Removes a work order from the invoice and recomputes amount and vat
	 * 
	 * @param workOrder
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
	 */
	public void removeWorkOrder(WorkOrder workOrder) {
		if (!this.status.equals(InvoiceStatus.NOT_YET_PAID)) {
			throw new IllegalStateException(
					"Cannot link if invoice status is paid");
		}
		ArgumentChecks.isNotNull(workOrder);

		Associations.ToInvoice.unlink(this, workOrder);
		workOrder.markBackToFinished();
		computeAmount();
	}

	/**
	 * Marks the invoice as PAID, but
	 * 
	 * @throws IllegalStateException if - Is already settled - Or the amounts
	 *                               paid with charges to payment means do not
	 *                               cover the total of the invoice
	 */
	public void settle() {
		if (this.status.equals(InvoiceStatus.PAID)) {
			throw new IllegalStateException("Invoice already PAID");
		}

		double currentPaid = getTotalCharges();
		if (currentPaid < this.getAmount() - UNDERPAID_LIMIT) {
			throw new IllegalStateException(
					"Invoice's charges do not cover the ammount of the invoice");
		}

		if (currentPaid > this.getAmount() + OVERPAID_LIMIT) {
			throw new IllegalStateException("Invoice is being overpaid");
		}

		this.status = InvoiceStatus.PAID;
	}

	public boolean isSettled() {
		return this.status.equals(InvoiceStatus.PAID);
	}

	private double getTotalCharges() {
		double d = 0.0;
		for (Charge ch : charges) {
			d += ch.getAmount();
		}

		return d;
	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	public Set<Charge> getCharges() {
		return new HashSet<>(charges);
	}

	Set<Charge> _getCharges() {
		return charges;
	}

	public boolean isNotSettled() {
		return !this.status.equals(InvoiceStatus.PAID);
	}

}
