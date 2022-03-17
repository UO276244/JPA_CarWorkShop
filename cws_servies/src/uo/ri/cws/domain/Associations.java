package uo.ri.cws.domain;

public class Associations {

	public static class Own {

		public static void link(Client client, Vehicle vehicle) {

			vehicle._setClient(client);// Very important the order
			client._getVehicles().add(vehicle);
		}

		public static void unlink(Client client, Vehicle vehicle) {

			client._getVehicles().remove(vehicle); // Very important the order
			// if client where part of the identity, the
			// hassh method would not generate the same
			vehicle._setClient(null);
		}

	}

	public static class Classify {

		public static void link(VehicleType vehicleType, Vehicle vehicle) {
			vehicle._setVehicleType(vehicleType);
			vehicleType._getVehicles().add(vehicle);
		}

		public static void unlink(VehicleType vehicleType, Vehicle vehicle) {

			vehicleType._getVehicles().remove(vehicle);
			vehicle._setVehicleType(null);
		}

	}

	public static class Pay {

		public static void link(PaymentMean pm, Client client) {
			pm._setClient(client);
			client._getPaymentMeans().add(pm);
		}

		public static void unlink(Client client, PaymentMean pm) {

			client._getPaymentMeans().remove(pm);
			pm._setClient(null);
		}

	}

	public static class Fix {

		public static void link(Vehicle vehicle, WorkOrder workOrder) {
			workOrder._setVehicle(vehicle);
			vehicle._getWorkOrders().add(workOrder);
		}

		public static void unlink(Vehicle vehicle, WorkOrder workOrder) {

			vehicle._getWorkOrders().remove(workOrder);
			workOrder._setVehicle(null);
		}

	}

	public static class ToInvoice {

		public static void link(Invoice invoice, WorkOrder workOrder) {
			workOrder._setInvoice(invoice);
			invoice._getWorkOrders().add(workOrder);

		}

		public static void unlink(Invoice invoice, WorkOrder workOrder) {

			invoice._getWorkOrders().remove(workOrder);
			workOrder._setInvoice(null);
		}
	}

	public static class Charges {

		public static void link(PaymentMean pm, Charge charge,
				Invoice inovice) {
			charge._setInvoice(inovice);
			charge._setPaymentMean(pm);
			pm._getCharges().add(charge);
			inovice._getCharges().add(charge);
		}

		public static void unlink(Charge charge) {
			charge.getInvoice()._getCharges().remove(charge);
			charge.getPaymentMean()._getCharges().remove(charge);
			charge._setInvoice(null);
			charge._setPaymentMean(null);
		}

	}

	public static class Assign {

		public static void link(Mechanic mechanic, WorkOrder workOrder) {
			workOrder._setMechanic(mechanic);
			mechanic._getAssigned().add(workOrder);
		}

		public static void unlink(Mechanic mechanic, WorkOrder workOrder) {

			mechanic._getAssigned().remove(workOrder);
			workOrder._setMechanic(null);
		}

	}

	public static class Intervene {

		public static void link(WorkOrder workOrder, Intervention intervention,
				Mechanic mechanic) {

			// First the one-side relations
			intervention._setMechanic(mechanic);
			intervention._setWorkOrder(workOrder);

			// The the many-side relations
			workOrder._getInterventions().add(intervention);
			mechanic._getInterventions().add(intervention);
		}

		public static void unlink(Intervention intervention) {

			// First the many-side relations
			intervention.getWorkOrder()._getInterventions()
					.remove(intervention);
			intervention.getMechanic()._getInterventions().remove(intervention);

			// Then the one-side relations
			intervention._setMechanic(null);
			intervention._setWorkOrder(null);
		}

	}

	public static class Sustitute {

		public static void link(SparePart spare, Substitution sustitution,
				Intervention intervention) {

			// First one-side
			sustitution._setIntervention(intervention);
			sustitution._setSparePart(spare);

			// Then many-side
			spare._getSubstitutions().add(sustitution);
			intervention._getSubstitutions().add(sustitution);
		}

		public static void unlink(Substitution sustitution) {
			// first many-side
			sustitution.getSparePart()._getSubstitutions().remove(sustitution);
			sustitution.getIntervention()._getSubstitutions()
					.remove(sustitution);

			// Then one-side
			sustitution._setIntervention(null);
			sustitution._setSparePart(null);
		}

	}

	public static class Recommend {

		public static void link(Recommendation rec, Client sponsor,
				Client recommended) {

			rec._setRecommended(recommended);
			rec._setSponsor(sponsor);

			recommended._setRecommended(rec);
			sponsor._getSponsored().add(rec);

		}

		public static void unlink(Recommendation rec) {

			rec.getRecommended()._setRecommended(null);
			rec.getSponsor()._getSponsored().remove(rec);

			rec._setRecommended(null);
			rec._setSponsor(null);

		}

	}

}
