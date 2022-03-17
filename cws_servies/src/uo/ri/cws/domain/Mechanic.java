package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.base.BaseEntity;

public class Mechanic extends BaseEntity {

	private String dni;
	private String surname;
	private String name;

	private Set<WorkOrder> assigned = new HashSet<WorkOrder>();

	private Set<Intervention> interventions = new HashSet<Intervention>();

	Mechanic() {
	}

	public Mechanic(String dni) {
		this(dni, "no-name", "no-surname");
	}

	public Mechanic(String dni, String name, String surname) {

		ArgumentChecks.isNotNull(surname);
		ArgumentChecks.isNotNull(name);
		ArgumentChecks.isNotEmpty(surname);
		ArgumentChecks.isNotEmpty(name);
		ArgumentChecks.isNotNull(dni);
		ArgumentChecks.isNotEmpty(dni);
		this.dni = dni;
		this.name = name;
		this.surname = surname;

	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<WorkOrder> getAssigned() {
		return new HashSet<>(assigned);
	}

	Set<WorkOrder> _getAssigned() {
		return assigned;
	}

	public Set<Intervention> getInterventions() {
		return new HashSet<Intervention>(interventions);
	}

	Set<Intervention> _getInterventions() {
		return interventions;
	}

	@Override
	public String toString() {
		return "Mechanic [dni=" + dni + ", surname=" + surname + ", name="
				+ name + "]";
	}

	public String getSurname() {
		return surname;
	}

	public String getName() {
		return name;
	}

	public String getDni() {
		return dni;
	}

	public void setInterventions(Set<Intervention> interventions) {
		this.interventions = interventions;
	}

}
