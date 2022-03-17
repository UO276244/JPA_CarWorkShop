package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Vehicle;

public interface VehicleRepository extends Repository<Vehicle> {

	Optional<Vehicle> findByPlate(String plate);
	
	List<Vehicle> findAll();

}
