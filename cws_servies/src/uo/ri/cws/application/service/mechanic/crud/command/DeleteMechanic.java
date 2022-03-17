package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import alb.util.assertion.ArgumentChecks;
import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.WorkOrder;

public class DeleteMechanic implements Command<Void> {

	private MechanicRepository repo = Factory.repository.forMechanic();

	private String mechanicId;

	public DeleteMechanic(String mechanicId) {
		ArgumentChecks.isNotNull(mechanicId);
		ArgumentChecks.isNotEmpty(mechanicId);

		this.mechanicId = mechanicId;
	}

	@Override
	public Void execute() throws BusinessException {

		Optional<Mechanic> m = repo.findById(mechanicId);
		checkCnBeDeleted(m);
		repo.remove(m.get());

		return null;
	}

	private void checkCnBeDeleted(Optional<Mechanic> m)
			throws BusinessException {
		BusinessChecks.isTrue(m.isPresent(), "Mechanic does not exists.");
		BusinessChecks.isTrue(m.get().getInterventions().isEmpty(),
				"Unable to delete, mechanic has dependencies.");
		
		
		for(WorkOrder i : m.get().getAssigned()) {

			BusinessChecks.isTrue(i == null,
					"Unable to delete, mechanic has workorders.");
		}
		

	}

}
