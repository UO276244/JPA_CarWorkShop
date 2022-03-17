package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.base.BaseEntity;

public class SparePart extends BaseEntity {

	private String code;
	private String description;
	private double price;

	private Set<Substitution> substitutions = new HashSet<>();

	SparePart() {
	}

	public SparePart(String code) {
		this(code, "no-description", 0.0);
	}

	public SparePart(String code, String description, double price) {

		ArgumentChecks.isNotNull(code);
		ArgumentChecks.isNotEmpty(code);
		this.code = code;
		ArgumentChecks.isTrue(price >= 0);
		this.description = description;
		this.price = price;
	}

	public Set<Substitution> getSustitutions() {
		return new HashSet<>(substitutions);
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public Set<Substitution> getSubstitutions() {
		return substitutions;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

}
