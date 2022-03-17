package uo.ri.cws.application.service.vehicle.crud.commands;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.VehicleRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Vehicle;

public class UpdateModelWithType implements Command<Optional<Void>>{

	private VehicleRepository repo = Factory.repository.forVehicle();
	
	@Override
	public Optional<Void> execute() throws BusinessException {
		
		List<Vehicle> allVehicles = repo.findAll();
		
		for(Vehicle v : allVehicles) {
			
			v.setModel(v.getVehicleType().getName() + "_" + v.getModel());
			
		}
		
		return null;
		
	}

}
