package uo.ri.cws.application.service.client.crud.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import alb.util.assertion.ArgumentChecks;
import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Recommendation;

public class FindClientsRecommendedBy implements Command<List<ClientDto>>{
	
	
	private String id;
	private ClientRepository repo = Factory.repository.forClient();
	
	public FindClientsRecommendedBy(String id) {
		ArgumentChecks.isNotNull(id);
		ArgumentChecks.isNotEmpty(id);
		this.id = id;
		
	}

	@Override
	public List<ClientDto> execute() throws BusinessException {
		
		Optional<Client> om = repo.findById(id);
		
		List<ClientDto> clientsSponsored = new ArrayList<ClientDto>();
		
		if(om.isPresent() && !om.get().getSponsored().isEmpty()) {
			
			for(Recommendation rec : om.get().getSponsored()) {
				clientsSponsored.add(DtoAssembler.toDto(rec.getRecommended()));
			}
			
		}
		return clientsSponsored;
		
		
	}

}
