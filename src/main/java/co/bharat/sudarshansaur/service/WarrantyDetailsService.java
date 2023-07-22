package co.bharat.sudarshansaur.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
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
		WarrantyDetails warrantyDetails = warrantyDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No WarrantyDetail Found"));
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
	
	//Updates ONLY the properties which are passed in the request data
	public WarrantyDetails updateWarrantyDetail(String id, WarrantyDetails updatedWarrantyDetail) {
		WarrantyDetails existingWarrantyDetail = warrantyDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No WarrantyDetail Found"));
		BeanUtils.copyProperties(updatedWarrantyDetail, existingWarrantyDetail, getNullPropertyNames(updatedWarrantyDetail));
		return warrantyDetailsRepository.save(existingWarrantyDetail);
	}
	
	//Find the properties which are not present in the request
    private String[] getNullPropertyNames(WarrantyDetails warrantyDetail) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(warrantyDetail);
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            if (beanWrapper.getPropertyValue(propertyName) == null) {
                nullPropertyNames.add(propertyName);
            }
        }
        return nullPropertyNames.toArray(new String[0]);
    }
}
