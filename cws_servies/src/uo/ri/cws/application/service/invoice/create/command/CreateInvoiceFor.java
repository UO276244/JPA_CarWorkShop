package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import alb.util.assertion.ArgumentChecks;
import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.WorkOrder.WorkOrderStatus;

public class CreateInvoiceFor implements Command<InvoiceDto> {

	private List<String> workOrderIds;
	private List<WorkOrder> workOrders;
	private WorkOrderRepository wrkrsRepo = Factory.repository.forWorkOrder();
	private InvoiceRepository invsRepo = Factory.repository.forInvoice();

	public CreateInvoiceFor(List<String> workOrderIds) {
		ArgumentChecks.isNotNull(workOrderIds);

		if (workOrderIds.isEmpty()) {
			throw new IllegalArgumentException(
					"WorkOrder id´s list cannot be empty");
		}

		for (String id : workOrderIds) {
			ArgumentChecks.isNotNull(id,
					"There are null values in the ids list");
			ArgumentChecks.isNotEmpty(id, "There are empty values in the list");
		}

		this.workOrderIds = workOrderIds;

	}

	@Override
	public InvoiceDto execute() throws BusinessException {

		this.workOrders = wrkrsRepo.findByIds(workOrderIds);

		Long nextNum = invsRepo.getNextInvoiceNumber();
		

		checkAllAreFinished(this.workOrders);

		checkAllExist(this.workOrderIds, this.workOrders);

		Invoice i = new Invoice(nextNum, this.workOrders);
		invsRepo.add(i);

		return DtoAssembler.toDto(i);

	}

	private void checkAllAreFinished(List<WorkOrder> workOrders) throws BusinessException {

		BusinessChecks.isTrue(workOrders.stream().allMatch(w -> w.getStatus()
				.equals(WorkOrderStatus.FINISHED)));

	}

	private void checkAllExist(List<String> workOrderIds,
			List<WorkOrder> workOrders) throws BusinessException {
		BusinessChecks.isTrue(workOrderIds.size() == workOrders.size());
	}

}
