package co.bharat.sudarshansaur.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.sudarshansaur.dto.CustomersResponseDTO;
import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.exception.handler.EntityValidationException;
import co.bharat.sudarshansaur.repository.CustomersRepository;

@Service
public class CustomersService {

	@Autowired
	private CustomersRepository customersRepository;
	
	public List<Customers> getAllCustomers() {
		return customersRepository.findAll();
	}
	
	public CustomersResponseDTO getCustomer(Long id) {
		return convertToDTO(customersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Customer Found")));
	}
	
	public CustomersResponseDTO saveCustomer(Customers customer) {
		//Check if any WarrantyRequests exists
		/*
		 * Optional<List<WarrantyRequests>> existingWR =
		 * warrantyRequestsRepository.findByCustomersCustomerIdAndStatus(customer.
		 * getCustomerId(), AllocationStatus.APPROVED); if(existingWR.isPresent() &&
		 * !existingWR.get().isEmpty()) { customer.setStatus(UserStatus.ACTIVE); } else
		 * { customer.setStatus(UserStatus.PENDING); }
		 */
		Customers newCustomer = new Customers();
		try {
			newCustomer = customersRepository.save(customer);
		} catch(ConstraintViolationException cve) {
			throw new EntityValidationException("User", "Mobile number already exists");
		}
		return convertToDTO(newCustomer);
	}
	
    public List<Object[]> getCountOfCustomersByStatus() {
        return customersRepository.countCustomersByStatus();
    }

	//Updates ONLY the properties which are passed in the request data
	public Customers updateCustomer(Long id, Customers updatedCustomer) {
		Customers existingCustomer = customersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Customer Found"));
		BeanUtils.copyProperties(updatedCustomer, existingCustomer, getNullPropertyNames(updatedCustomer));
		return customersRepository.save(existingCustomer);
	}
	
	public CustomersResponseDTO updateCustomerAndReturnDTO(Long id, Customers updatedCustomer) {
		return convertToDTO(updateCustomer(id,updatedCustomer));
	}
	
	//Find the properties which are not present in the request
    private String[] getNullPropertyNames(Customers customer) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(customer);
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
    
	private CustomersResponseDTO convertToDTO(Customers customers) {
		CustomersResponseDTO dto = new CustomersResponseDTO();
		BeanUtils.copyProperties(customers, dto);
		return dto;
	}
	
	public List<CustomersResponseDTO> convertToDTOList(List<Customers> stockistsList) {
		return stockistsList.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	/*
	 * public CustomersResponseDTO findByEmailAndPassword(Customers customer) {
	 * return
	 * convertToDTO(customersRepository.findByEmailAndPassword(customer.getEmail(),
	 * customer.getPassword()) .orElseThrow(() -> new
	 * EntityNotFoundException("Incorrect email and password"))); }
	 */
}
