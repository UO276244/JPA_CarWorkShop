package uo.ri.cws.application.service.mechanic.crud.command;

import alb.util.assertion.ArgumentChecks;
import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;

public class AddMechanic implements Command<MechanicDto> {

	private MechanicDto dto;
	private MechanicRepository repo = Factory.repository.forMechanic();

	public AddMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto);
		this.dto = dto;
	}

	@Override
	public MechanicDto execute() throws BusinessException {

		checkMechanicInDB(dto);

		Mechanic m = new Mechanic(dto.dni, dto.name, dto.surname);
		dto.id = m.getId();

		repo.add(m);

		return dto;
	}

	private void checkMechanicInDB(MechanicDto dto) throws BusinessException {
		if (repo.findByDni(dto.dni).isPresent()) {
			throw new BusinessException(
					"Mechanic already registered with dni: " + dto.dni);
		}

	}

}
