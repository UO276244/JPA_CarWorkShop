package uo.ri.cws.application.service.paymentmean.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;

public class PaymentmeanCrudServiceImpl implements PaymentMeanCrudService {

	

	@Override
	public CardDto addCard(CardDto card) throws BusinessException {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public VoucherDto addVoucher(VoucherDto voucher) throws BusinessException {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void deletePaymentMean(String id) throws BusinessException {
		throw new RuntimeException("Not yet implemented");
		
	}

	@Override
	public Optional<PaymentMeanDto> findById(String id)
			throws BusinessException {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public List<PaymentMeanDto> findPaymentMeansByClientId(String id)
			throws BusinessException {
		throw new RuntimeException("Not yet implemented");
	}

}
