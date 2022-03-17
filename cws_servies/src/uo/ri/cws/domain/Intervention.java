package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import alb.util.assertion.ArgumentChecks;
import uo.ri.cws.domain.base.BaseEntity;


public class Intervention extends BaseEntity {

	private LocalDateTime date;
	private int minutes;

	private WorkOrder workOrder;

	private Mechanic mechanic;

	private Set<Substitution> substitutions = new HashSet<>();

	Intervention() {

	}

	public Intervention(Mechanic mc, WorkOrder wo, LocalDateTime date,
			int minutes) {
		ArgumentChecks.isNotNull(mc);
		ArgumentChecks.isNotNull(wo);
		ArgumentChecks.isTrue(minutes >= 0);
		ArgumentChecks.isNotNull(date);

		this.date = date.truncatedTo(ChronoUnit.MILLIS);
		this.mechanic = mc;
		this.workOrder = wo;
		this.minutes = minutes;
		Associations.Intervene.link(workOrder, this, mechanic);

	}

	public Intervention(WorkOrder workOrder, Mechanic mechanic) {
		this(mechanic, workOrder, LocalDateTime.now(), 0);
	}

	public Intervention(Mechanic mechanic, WorkOrder workOrder, int minutes) {
		this(mechanic, workOrder, LocalDateTime.now(), minutes);
	}

	void _setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	public Set<Substitution> getSubstitutions() {
		return new HashSet<>(substitutions);
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public int getMinutes() {
		return minutes;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public Mechanic getMechanic() {
		return mechanic;
	}

	@Override
	public String toString() {
		return "Intervention [date=" + date + ", minutes=" + minutes
				+ ", workOrder=" + workOrder + ", mechanic=" + mechanic + "]";
	}

	public double getAmount() {
		double workhand = (workOrder.getVehicle().getVehicleType()
				.getPricePerHour() * this.minutes) / 60;

		double subs = 0.0;

		for (Substitution sub : this.substitutions) {
			subs += sub.getAmount();
		}

		return workhand + subs;

	}

	public void setMinutes(int minutes) {

		ArgumentChecks.isTrue(minutes >= 0, "Minutes can't be negative");
		this.minutes = minutes;
	}

}
