package co.bharat.sudarshansaur.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.sudarshansaur.dto.WarrantyRequestsDTO;
import co.bharat.sudarshansaur.entity.WarrantyRequests;
import co.bharat.sudarshansaur.enums.UserType;
import co.bharat.sudarshansaur.repository.CustomersRepository;
import co.bharat.sudarshansaur.repository.DealersRepository;
import co.bharat.sudarshansaur.repository.WarrantyRequestsRepository;

@Service
public class WarrantyRequestsService {

	@Autowired
	private WarrantyRequestsRepository warrantyRequestsRepository;
	@Autowired
	private CustomersRepository customersRepository;
	@Autowired
	private DealersRepository dealersRepository;

	public List<WarrantyRequestsDTO> getAllWarrantyRequests() {
		List<WarrantyRequests> warrantyRequestsList = warrantyRequestsRepository.findAll();
		return convertToDTOList(warrantyRequestsList);
	}
	
	public WarrantyRequestsDTO getWarrantyRequests(Long id) {
		WarrantyRequests warrantyRequests = warrantyRequestsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No WarrantyRequest Found"));
		return convertToDTO(warrantyRequests);
	}

	private List<WarrantyRequestsDTO> convertToDTOList(List<WarrantyRequests> warrantyRequestsList) {
		return warrantyRequestsList.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private WarrantyRequestsDTO convertToDTO(WarrantyRequests warrantyRequests) {
		WarrantyRequestsDTO dto = new WarrantyRequestsDTO();
		BeanUtils.copyProperties(warrantyRequests, dto);
		//dto.setCustomers(warrantyRequests.getCustomers());
		if (warrantyRequests.getInitUserType() != null) {
			if (UserType.CUSTOMER.equals(warrantyRequests.getInitUserType())) {
				dto.setInitiatedBy(customersRepository.findById(Long.valueOf(warrantyRequests.getInitiatedBy())).orElseThrow(() -> new EntityNotFoundException("No Customer Found with the given details")));
			} else if (UserType.DEALER.equals(warrantyRequests.getInitUserType())) {
				dto.setInitiatedBy(dealersRepository.findById(Long.valueOf(warrantyRequests.getInitiatedBy())).orElseThrow(() -> new EntityNotFoundException("No Dealers Found with the given details")));
			}
		}
		
		return dto;
	}
}
