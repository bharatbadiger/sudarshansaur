package co.bharat.sudarshansaur.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.sudarshansaur.dto.WarrantyDetailsDTO;
import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.repository.WarrantyDetailsRepository;

@Service
public class WarrantyDetailsService {

	@Autowired
	private WarrantyDetailsRepository warrantyDetailsRepository;

	public List<WarrantyDetailsDTO> getAllWarrantyDetails() {
		List<WarrantyDetails> warrantyDetailsList = warrantyDetailsRepository.findAll();
		return convertToDTOList(warrantyDetailsList);
	}
	
	public WarrantyDetailsDTO getWarrantyDetails(String id) {
		WarrantyDetails warrantyDetails = warrantyDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No WarrantyDetail Found"));;
		return convertToDTO(warrantyDetails);
	}

	private List<WarrantyDetailsDTO> convertToDTOList(List<WarrantyDetails> warrantyDetailsList) {
		return warrantyDetailsList.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private WarrantyDetailsDTO convertToDTO(WarrantyDetails warrantyDetails) {
		WarrantyDetailsDTO dto = new WarrantyDetailsDTO();
		BeanUtils.copyProperties(warrantyDetails, dto);
		dto.setCustomer(warrantyDetails.getCustomer().getCustomerId());
		return dto;
	}
}
