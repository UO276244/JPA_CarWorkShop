package uo.ri.cws.demo;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.BusinessFactory;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.infrastructure.persistence.jpa.executor.JpaExecutorFactory;
import uo.ri.cws.infrastructure.persistence.jpa.repository.JpaRepositoryFactory;

public class DemoTests {

	private List<String> types = Arrays.asList(
			"Furgoneta", "Quad", "Moto", "Coche", "Camion", "Tractor"
		);
	
	@Before
	public void setUp() {
		Factory.service = new BusinessFactory();
		Factory.repository = new JpaRepositoryFactory();
		Factory.executor = new JpaExecutorFactory();
	}

	/**
	 * Given: The default set of vehicles on the DB (250 vehicles)
	 * When: We invoke the method updateModelWithType()
	 * Then: we get all the vehicle updated with the type name prefixed
	 */
	@Test
	public void test() throws BusinessException {
		VehicleCrudService service = Factory.service.forVehicleCrudService();
		List<VehicleDto> given = service.findAllVehicles();
		
		service.updateModelWithType();
		
		List<VehicleDto> then = service.findAllVehicles();

		assertTrue( noneStartsWithVehicleType( given )  );
		assertTrue( allStartsWithVehicleType( then )  );
	}

	private boolean noneStartsWithVehicleType(List<VehicleDto> list) {
		return list.stream()
				.noneMatch( v -> isTypePrefixed( v.model ) );
	}

	private boolean allStartsWithVehicleType(List<VehicleDto> list) {
		return list.stream()
				.allMatch( v -> isTypePrefixed( v.model ) );
	}

	private boolean isTypePrefixed(String model) {
		return types.stream().anyMatch( t -> model.startsWith( t ) );
	}

}
