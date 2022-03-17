package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.base.BaseEntity;

public class Vehicle extends BaseEntity {

	private String plateNumber;
	private String make;
	private String model;

	private Client client;
	private VehicleType type;
	private Set<WorkOrder> workorders = new HashSet<WorkOrder>();

	Vehicle() {

	}

	public Vehicle(String plateNumber) {
		this(plateNumber, "no-make", "no-model");
	}

	public Vehicle(String plateNumber, String make, String model) {

		ArgumentChecks.isNotNull(make);
		ArgumentChecks.isNotEmpty(make);
		ArgumentChecks.isNotNull(model);
		ArgumentChecks.isNotEmpty(model);
		ArgumentChecks.isNotNull(plateNumber);
		ArgumentChecks.isNotEmpty(plateNumber);
		this.plateNumber = plateNumber;
		this.make = make;
		this.model = model;
	}

	public VehicleType getVehicleType() {
		return type;
	}

	/* package */ void _setVehicleType(VehicleType type) {
		this.type = type;
	}

	public Client getClient() {
		return client;
	}

	/* package */ void _setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "Vehicle [plateNumber=" + plateNumber + ", make=" + make
				+ ", model=" + model + "]";
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workorders);
	}

	/* package */ Set<WorkOrder> _getWorkOrders() {
		return workorders;
	}

	

	public void setType(VehicleType type) {
		this.type = type;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
