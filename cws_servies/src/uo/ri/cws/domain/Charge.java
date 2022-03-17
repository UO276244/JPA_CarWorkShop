package uo.ri.cws.domain;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.Invoice.InvoiceStatus;
import uo.ri.cws.domain.base.BaseEntity;


public class Charge extends BaseEntity {

	private double amount = 0.0;

	private Invoice invoice;

	private PaymentMean paymentMean;

	Charge() {

	}

	public Charge(Invoice invoice, PaymentMean paymentMean, double amount) {
		ArgumentChecks.isTrue(amount >= 0);
		this.amount = amount;

		ArgumentChecks.isNotNull(paymentMean);
		ArgumentChecks.isNotNull(invoice);

		// store the amount
		// increment the paymentMean accumulated -> paymentMean.pay( amount )
		paymentMean.pay(amount);
		// link invoice, this and paymentMean
		Associations.Charges.link(paymentMean, this, invoice);
	}

	public double getAmount() {
		return amount;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	void _setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public PaymentMean getPaymentMean() {
		return paymentMean;
	}

	void _setPaymentMean(PaymentMean paymentMean) {
		this.paymentMean = paymentMean;
	}

	/**
	 * Unlinks this charge and restores the accumulated to the payment mean
	 * 
	 * @throws IllegalStateException if the invoice is already settled
	 */
	public void rewind() {
		// asserts the invoice is not in PAID status
		if (invoice.getStatus().equals(InvoiceStatus.PAID)) {
			throw new IllegalStateException("Invoice already settled");
		}
		// decrements the payment mean accumulated ( paymentMean.pay( -amount) )
		paymentMean.pay(-amount);
		// unlinks invoice, this and paymentMean

		Associations.Charges.unlink(this);
	}

}
