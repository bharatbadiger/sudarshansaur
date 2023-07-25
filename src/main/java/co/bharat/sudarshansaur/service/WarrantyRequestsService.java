package co.bharat.sudarshansaur.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.sudarshansaur.dto.WarrantyRequestsDTO;
import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.entity.WarrantyRequests;
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.enums.UserType;
import co.bharat.sudarshansaur.repository.CustomersRepository;
import co.bharat.sudarshansaur.repository.DealersRepository;
import co.bharat.sudarshansaur.repository.WarrantyDetailsRepository;
import co.bharat.sudarshansaur.repository.WarrantyRequestsRepository;

@Service
public class WarrantyRequestsService {

	@Autowired
	private WarrantyDetailsService warrantyDetailsService;
	@Autowired
	private WarrantyDetailsRepository warrantyDetailsRepository;
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
		WarrantyRequests warrantyRequests = warrantyRequestsRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No WarrantyRequest Found"));
		return convertToDTO(warrantyRequests);
	}

	private List<WarrantyRequestsDTO> convertToDTOList(List<WarrantyRequests> warrantyRequestsList) {
		return warrantyRequestsList.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private WarrantyRequestsDTO convertToDTO(WarrantyRequests warrantyRequests) {
		WarrantyRequestsDTO dto = new WarrantyRequestsDTO();
		BeanUtils.copyProperties(warrantyRequests, dto);
		// dto.setCustomers(warrantyRequests.getCustomers());
		if (warrantyRequests.getInitUserType() != null) {
			if (UserType.CUSTOMER.equals(warrantyRequests.getInitUserType())) {
				dto.setInitiatedBy(customersRepository.findById(Long.valueOf(warrantyRequests.getInitiatedBy()))
						.orElseThrow(() -> new EntityNotFoundException("No Customer Found with the given details")));
			} else if (UserType.DEALER.equals(warrantyRequests.getInitUserType())) {
				dto.setInitiatedBy(dealersRepository.findById(Long.valueOf(warrantyRequests.getInitiatedBy()))
						.orElseThrow(() -> new EntityNotFoundException("No Dealers Found with the given details")));
			}
		}

		return dto;
	}

	@Transactional
	public WarrantyRequests saveWarrantyRequests(WarrantyRequests warrantyRequests) {
		if (warrantyRequests.getWarrantyDetails().getWarrantySerialNo() != null) {
			WarrantyDetails existingWarrantyDetail = 
					warrantyDetailsRepository.findById(warrantyRequests.getWarrantyDetails().getWarrantySerialNo())
					.orElseThrow(() -> new EntityNotFoundException("No Warranty Detail Found"));
			if(existingWarrantyDetail.getDealers().getDealerId()!=warrantyRequests.getDealers().getDealerId()) {
				throw new EntityNotFoundException("This Warranty is not allocated to the given Dealer");
			}
			warrantyRequests.setWarrantyDetails(existingWarrantyDetail);
		}
		if (UserType.CUSTOMER.equals(warrantyRequests.getInitUserType())) {
			warrantyRequests.setCustomers(customersRepository.findById(warrantyRequests.getCustomers().getCustomerId())
					.orElseThrow(() -> new EntityNotFoundException("No Customer Found")));
		}
		if (UserType.DEALER.equals(warrantyRequests.getInitUserType())) {
			warrantyRequests.setDealers(dealersRepository.findById(warrantyRequests.getDealers().getDealerId())
					.orElseThrow(() -> new EntityNotFoundException("No Dealer Found")));
			//Add the customer if not present already(customerId will not be present)
			if(warrantyRequests.getCustomers().getCustomerId()==0) {
				customersRepository.save(warrantyRequests.getCustomers());
			}
		}
		WarrantyRequests newWarrantyRequests = warrantyRequestsRepository.save(warrantyRequests);
		if (AllocationStatus.APPROVED.equals(warrantyRequests.getAllocationStatus())) {
			WarrantyDetails updatedWarrantyDetails = warrantyRequests.getWarrantyDetails();
			warrantyDetailsService.updateWarrantyDetail(updatedWarrantyDetails.getWarrantySerialNo(),
					updatedWarrantyDetails);
		}
		return newWarrantyRequests;

	}

	public List<WarrantyRequestsDTO> getAllWarrantyRequestsForCustomer(long customerId) {
		return convertToDTOList(warrantyRequestsRepository.findByCustomersCustomerId(customerId)
				.orElseThrow(() -> new EntityNotFoundException("No WarrantyRequests for this Customer Found")));
	}

	public List<WarrantyRequestsDTO> getAllWarrantyRequestsForDealer(long dealerId) {
		return convertToDTOList(warrantyRequestsRepository.findByCustomersCustomerId(dealerId)
				.orElseThrow(() -> new EntityNotFoundException("No WarrantyRequests for this Dealer Found")));
	}
}
