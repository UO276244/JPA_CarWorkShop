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

public class FindClientById implements Command<Optional<ClientDto>>{
	
	private String id;
	private ClientRepository repo = Factory.repository.forClient();
	
	public FindClientById(String clientId) {
		ArgumentChecks.isNotNull(clientId);
		ArgumentChecks.isNotEmpty(clientId);
		this.id = clientId;
	}

	@Override
	public Optional<ClientDto> execute() throws BusinessException {

		Optional<Client> om = repo.findById(id);
		
		if(om.isPresent()) {
			
		return Optional.of(DtoAssembler.toDto(om.get()));
		
		}
		else {
			return Optional.empty();
		}

	}

}
