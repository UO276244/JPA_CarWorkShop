package uo.ri.cws.application.service.paymentmean.voucher.crud.command;

import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Recommendation;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.WorkOrder;

public class GenerateVouchers implements Command<Integer>{

	private ClientRepository repo = Factory.repository.forClient();
	private PaymentMeanRepository payRepo = Factory.repository.forPaymentMean();
	private InvoiceRepository invRepo = Factory.repository.forInvoice();
	
	
	
	@Override
	public Integer execute() throws BusinessException {

		int total = 
		generateVoucherPer3Recommendations() + 
		generateVoucherPer3WorkOrders() +
		generateVoucherPer500Invoice();
		
		
		return total;
	}
	
	
	private int generateVoucherPer3Recommendations() {
		

		List<Client> allClients = repo.findAll();
		List<Recommendation> elegibleRecommendations;
		Voucher generated;
		
		int vouchersGenerated = 0;
		
		for(Client c : allClients) {
			
			elegibleRecommendations = c.elegibleFor3RecVoucher();
			while(elegibleRecommendations.size() >= 3) {
				
				elegibleRecommendations.get(0).markAsUsed();;
				elegibleRecommendations.get(1).markAsUsed();
				elegibleRecommendations.get(2).markAsUsed();
				
				generated = new Voucher( 25.0, "By recommendation",c);
				
			
				payRepo.add(generated);
				
				elegibleRecommendations.remove(0);
				elegibleRecommendations.remove(0);
				elegibleRecommendations.remove(0);
				
				vouchersGenerated++;
				
			}
			
		}
		
		
		return vouchersGenerated;
		
	}
	
	private int generateVoucherPer3WorkOrders() {
		
List<Client> allClients = repo.findAll();
		
		List<WorkOrder> elegibles;
		Voucher generated;
		

		int vouchersGenerated = 0;
		for (Client c : allClients) {

			//If client has at lest 3 elegible workorders
			elegibles =  c.getWorkOrdersAvailableForVoucher();
			while(elegibles.size() >= 3) {
				
				elegibles.get(0).markAsUsedForVoucher();
				elegibles.get(1).markAsUsedForVoucher();
				elegibles.get(2).markAsUsedForVoucher();
				
				generated = new Voucher( 20.0, "By three workorders",c);
				
			
				payRepo.add(generated);
				
				elegibles.remove(0);
				elegibles.remove(0);
				elegibles.remove(0);
				
				vouchersGenerated++;
			}
			

		}

		return vouchersGenerated;
		
	}
	
	private int generateVoucherPer500Invoice() {
		

		List<Invoice> allInvoices = invRepo.findAll();
		Client clientOfVoucher;
		
		int vouchersGenerated = 0;

		for (Invoice i : allInvoices) {

			if (i.canGenerate500Voucher()) {

				// I do this loop in order to get one element of the set, since
				// there is no get() method in set
				for (WorkOrder wo : i.getWorkOrders()) {

					clientOfVoucher = wo.getVehicle().getClient();

					payRepo.add(new Voucher(30.0, "By invoice over 500",
							clientOfVoucher));
					vouchersGenerated++;
					i.markAsUsed();
					break;
				}

			}

		}

	

		return vouchersGenerated;
		
	}

}
