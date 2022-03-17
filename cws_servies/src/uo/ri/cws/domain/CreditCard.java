package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.Objects;

import alb.util.assertion.ArgumentChecks;

public class CreditCard extends PaymentMean {
	
	private String number;
	private String type;
	private LocalDate validThru;

	CreditCard() {
	}

	public CreditCard(String number) {
		this(number, "UNKNOWN", LocalDate.now().plusDays(1L));

	}

	public CreditCard(String number, String type, LocalDate validThru) {

		ArgumentChecks.isNotNull(type);
		ArgumentChecks.isNotEmpty(type);
		ArgumentChecks.isNotNull(validThru);
		ArgumentChecks.isNotNull(number);
		ArgumentChecks.isNotEmpty(number);

		this.type = type;
		this.validThru = validThru;
		this.number = number;
	}

	@Override
	public boolean isValid() {
		return isValidNow();
	}

	public String getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

	public LocalDate getValidThru() {
		return validThru;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCard other = (CreditCard) obj;
		return Objects.equals(number, other.number);
	}

	@Override
	public String toString() {
		return "CreditCard [number=" + number + ", type=" + type
				+ ", validThru=" + validThru + "]";
	}

	public boolean isValidNow() {
		return this.validThru.isAfter(LocalDate.now());
	}

	public void setValidThru(LocalDate date) {
		if (this.validThru.isAfter(date) && super.getAccumulated() != 0) {
			throw new IllegalStateException(
					"Cannot change datre to previous one if credit card has been used to pay");
		}
		this.validThru = date;
	}

}
