package uo.ri.cws.domain;

import java.util.UUID;

import alb.util.assertion.ArgumentChecks;

public class Voucher extends PaymentMean {

	private String code;
	private double available = 0.0;
	private String description;

	Voucher() {
	}

	/**
	 * Augments the accumulated (super.pay(amount) ) and decrements the
	 * available
	 * 
	 * @throws IllegalStateException if not enough available to pay
	 */
	@Override
	public void pay(double amount) {
		if (amount > available) {
			throw new IllegalStateException("No enough money available");
		}

		this.available -= amount;
		super.pay(amount);
	}

	public Voucher(String code) {
		this(code, "no-description", 0.0);
	}

	public Voucher(String code, double amount) {
		this(code, "no-description", amount);
	}

	public Voucher(double amount, String description, Client client) {
		this(UUID.randomUUID().toString(), description, amount);
		
		ArgumentChecks.isNotNull(client);
		Associations.Pay.link(this, client);
	}
	public Voucher(String code, String description, double available) {

		ArgumentChecks.isTrue(available >= 0.0);
		ArgumentChecks.isNotNull(code);
		ArgumentChecks.isNotEmpty(code);
		this.code = code;

		this.description = description;
		this.available = available;
	}

	public String getCode() {
		return code;
	}

	public double getAvailable() {
		return available;
	}

	public String getDescription() {
		return description;
	}

}
