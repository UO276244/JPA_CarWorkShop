package uo.ri.cws.application.service.vehicle.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicle.crud.commands.FindAllVehicles;
import uo.ri.cws.application.service.vehicle.crud.commands.FindVehicleByPlate;
import uo.ri.cws.application.service.vehicle.crud.commands.UpdateModelWithType;
import uo.ri.cws.application.util.command.CommandExecutor;

public class VehicleCrudServiceImpl implements VehicleCrudService {

	private CommandExecutor executor = Factory.executor.forExecutor();

	@Override
	public Optional<VehicleDto> findVehicleByPlate(String plate)
			throws BusinessException {
		return executor.execute(new FindVehicleByPlate(plate));
	}

	@Override
	public void updateModelWithType() throws BusinessException {
		executor.execute(new UpdateModelWithType());
		
	}

	@Override
	public List<VehicleDto> findAllVehicles() throws BusinessException {
		return executor.execute(new FindAllVehicles());
	}
	
	
	
	

}
