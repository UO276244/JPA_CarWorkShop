package uo.ri.cws.application.service.vehicle.crud.commands;

import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.VehicleRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Vehicle;

public class FindAllVehicles implements Command<List<VehicleDto>>{
	
	private VehicleRepository repo = Factory.repository.forVehicle();

	@Override
	public List<VehicleDto> execute() throws BusinessException {
		
		List<Vehicle> all = repo.findAll();
		return DtoAssembler.toVehicleDtoList(all);
	}

}
