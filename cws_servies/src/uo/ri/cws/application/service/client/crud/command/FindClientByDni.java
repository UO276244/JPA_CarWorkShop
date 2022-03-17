package uo.ri.cws.application.service.client.crud.command;

import java.util.Optional;

import alb.util.assertion.ArgumentChecks;
import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Client;

public class FindClientByDni implements Command<Optional<ClientDto>>{
	
	private String dni;
	private ClientRepository repo = Factory.repository.forClient();
	
	public FindClientByDni(String clientDni) {
		ArgumentChecks.isNotNull(clientDni);
		ArgumentChecks.isNotEmpty(clientDni);
		this.dni = clientDni;
	}

	@Override
	public Optional<ClientDto> execute() throws BusinessException {
		
		Optional<Client> om = repo.findByDni(dni);
		
		if(om.isPresent()) {
			
		return Optional.of(DtoAssembler.toDto(om.get()));
		
		}
		else {
			return Optional.empty();
		}
	}

}
