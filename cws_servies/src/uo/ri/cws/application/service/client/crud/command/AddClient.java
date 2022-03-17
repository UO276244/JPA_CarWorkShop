package uo.ri.cws.application.service.client.crud.command;

import java.util.Optional;

import alb.util.assertion.ArgumentChecks;
import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;

public class AddClient implements Command<ClientDto> {

	private ClientDto client;
	private String idSponosr;
	private ClientRepository repo = Factory.repository.forClient();
	private PaymentMeanRepository repoPay = Factory.repository.forPaymentMean();

	public AddClient(ClientDto cliente, String idSponsor) {
		ArgumentChecks.isNotNull(cliente);
		ArgumentChecks.isNotNull(cliente.dni);
		ArgumentChecks.isNotEmpty(cliente.dni);
		ArgumentChecks.isNotEmpty(cliente.dni.trim());
		this.client = cliente;
		this.idSponosr = idSponsor;
	}

	@Override
	public ClientDto execute() throws BusinessException {
		checkClientInDB(client);

		Client sponsor = null;
		if(idSponosr != null) {
			checkSponsorExists(idSponosr).get();
		}
		Client c = new Client(client.dni, client.name, client.surname,sponsor);
		client.id = c.getId();
		client.email = c.getEmail();
		client.addressCity = c.getAddress().getCity();
		client.addressStreet = c.getAddress().getStreet();
		client.addressZipcode = c.getAddress().getZipCode();
		client.phone = c.getPhone();
		client.version = c.getVersion();
	

		
		repo.add(c);
		repoPay.add(new Cash(c));
		
		

		return client;
	}

	private void checkClientInDB(ClientDto dto) throws BusinessException {
		if (repo.findByDni(dto.dni).isPresent()) {
			throw new BusinessException(
					"Client already registered with dni: " + dto.dni);
		}

	}

	private Optional<Client> checkSponsorExists(String idSponsor)
			throws BusinessException {
		Optional<Client> found = repo.findById(idSponsor);
		if (found.isEmpty()) {
			throw new BusinessException(
					"Sponsor with id: " + idSponsor + "does not exist");
		} else {
			return found;
		}

	}

}
