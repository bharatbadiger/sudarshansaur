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

import co.bharat.sudarshansaur.dto.DealersResponseDTO;
import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.repository.DealersRepository;

@Service
public class DealersService {

	@Autowired
	private DealersRepository dealersRepository;

	public List<Dealers> getAllDealers() {
		return dealersRepository.findAll();
	}
	
	public DealersResponseDTO getDealer(Long id) {
		return convertToDTO(dealersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Dealer Found")));
	}
	
	public DealersResponseDTO saveDealer(Dealers dealer) {
		Dealers newDealer = dealersRepository.save(dealer);
		return convertToDTO(newDealer);
	}

	//Updates ONLY the properties which are passed in the request data
	public Dealers updateDealer(Long id, Dealers updatedDealer) {
		Dealers existingDealer = dealersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Dealer Found"));
		BeanUtils.copyProperties(updatedDealer, existingDealer, getNullPropertyNames(updatedDealer));
		return dealersRepository.save(existingDealer);
	}
	
	public DealersResponseDTO updateDealerAndReturnDTO(Long id, Dealers updatedDealer) {
		return convertToDTO(updateDealer(id, updatedDealer));
	}
	
	//Find the properties which are not present in the request
    private String[] getNullPropertyNames(Dealers dealer) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(dealer);
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);
            // Exclude properties with numeric types from being considered for updating
            if (propertyValue == null || (propertyValue instanceof String && ((String) propertyValue).isEmpty())|| (long.class.isAssignableFrom(propertyDescriptor.getPropertyType()))) {
                nullPropertyNames.add(propertyName);
            }
        }
        return nullPropertyNames.toArray(new String[0]);
    }
    
	private DealersResponseDTO convertToDTO(Dealers dealers) {
		DealersResponseDTO dto = new DealersResponseDTO();
		BeanUtils.copyProperties(dealers, dto);
		return dto;
	}
	
	public List<DealersResponseDTO> convertToDTOList(List<Dealers> dealersList) {
		return dealersList.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
//	public DealersResponseDTO findByEmail(Dealers dealer) {
//		return convertToDTO(dealersRepository.findByEmail(dealer.getEmail())
//				.orElseThrow(() -> new EntityNotFoundException("Incorrect email and password")));
//	}
}
