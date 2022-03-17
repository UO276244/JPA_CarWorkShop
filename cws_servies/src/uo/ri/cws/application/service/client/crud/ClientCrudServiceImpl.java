package uo.ri.cws.application.service.client.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.crud.command.AddClient;
import uo.ri.cws.application.service.client.crud.command.DeleteClient;
import uo.ri.cws.application.service.client.crud.command.FindAllClients;
import uo.ri.cws.application.service.client.crud.command.FindClientByDni;
import uo.ri.cws.application.service.client.crud.command.FindClientById;
import uo.ri.cws.application.service.client.crud.command.FindClientsRecommendedBy;
import uo.ri.cws.application.service.client.crud.command.UpdateClient;
import uo.ri.cws.application.util.command.CommandExecutor;

public class ClientCrudServiceImpl implements ClientCrudService {

	private CommandExecutor executor = Factory.executor.forExecutor();

	@Override
	public ClientDto addClient(ClientDto cliente, String sponsorId) 
			throws BusinessException {
		return executor.execute(new AddClient(cliente, sponsorId));
	}

	@Override
	public void deleteClient(String idClient) throws BusinessException {
		executor.execute(new DeleteClient(idClient));

	}

	@Override
	public void updateClient(ClientDto cliente) throws BusinessException {
		executor.execute(new UpdateClient(cliente));

	}

	@Override
	public Optional<ClientDto> findClientById(String id)
			throws BusinessException {
		return executor.execute(new FindClientById(id));
	}

	@Override
	public List<ClientDto> findAllClients() throws BusinessException {
		return executor.execute(new FindAllClients());
	}

	@Override
	public Optional<ClientDto> findClientByDni(String dni)
			throws BusinessException {
		return executor.execute(new FindClientByDni(dni));
	}



	@Override
	public List<ClientDto> findClientsRecommendedBy(String sponsorID)
			throws BusinessException {
		return executor.execute(new FindClientsRecommendedBy(sponsorID));
	}

}
