package uo.ri.cws.application.service.client.crud.command;

import java.util.Optional;

import alb.util.assertion.ArgumentChecks;
import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Address;
import uo.ri.cws.domain.Client;

public class UpdateClient implements Command<Void> {

	private ClientDto dto;
	private ClientRepository repo = Factory.repository.forClient();

	public UpdateClient(ClientDto dto) {
		ArgumentChecks.isNotNull(dto,"Dto cannot be null");
		ArgumentChecks.isNotEmpty(dto.dni,"Dni cannot be empty");
		ArgumentChecks.isNotEmpty(dto.id,"Id cannot be empty");
		ArgumentChecks.isNotNull(dto.dni,"Dni cannot be null");
		ArgumentChecks.isNotNull(dto.id,"Id cannot be null");
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<Client> oc = repo.findById(dto.id);
		BusinessChecks.exists(oc, "Client does not exist");

		Client c = oc.get();
		

		
		BusinessChecks.hasVersion(c, dto.version);
		
		c.setName(dto.name);
		c.setSurname(dto.surname);
		c.setPhone(dto.phone);
		c.setEmail(dto.email);
		c.setAddress(new Address(dto.addressStreet, dto.addressCity, 
				dto.addressZipcode));

		return null;

	}

}
