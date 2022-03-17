package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.base.BaseEntity;

public class VehicleType extends BaseEntity {

	private String name;
	private double pricePerHour;

	private Set<Vehicle> vehicles = new HashSet<>();

	VehicleType() {

	}

	public VehicleType(String name) {
		this(name, 0.0);
	}

	public VehicleType(String name, double priceperhour) {

		ArgumentChecks.isNotNull(name);
		ArgumentChecks.isNotEmpty(name);
		this.name = name;
		ArgumentChecks.isNotNull(priceperhour);
		ArgumentChecks.isTrue(priceperhour >= 0.0);
		this.pricePerHour = priceperhour;
	}

	public Set<Vehicle> getVehicles() {
		return new HashSet<>(vehicles);
	}

	Set<Vehicle> _getVehicles() {
		return vehicles;
	}

	@Override
	public String toString() {
		return "VehicleType [name=" + name + ", pricePerHour=" + pricePerHour
				+ "]";
	}

	public double getPricePerHour() {
		return pricePerHour;
	}

	public String getName() {
		return name;
	}

}
