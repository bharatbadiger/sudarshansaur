package co.bharat.sudarshansaur.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.repository.CustomersRepository;

@Service
public class CustomersService {

	@Autowired
	private CustomersRepository customersRepository;

	public List<Customers> getAllCustomers() {
		return customersRepository.findAll();
	}
	
	public Customers getCustomer(Long id) {
		return customersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Customer Found"));
	}
	
	public Customers saveCustomer(Customers customer) {
		return customersRepository.save(customer);
	}

	//Updates ONLY the properties which are passed in the request data
	public Customers updateCustomer(Long id, Customers updatedCustomer) {
		Customers existingCustomer = customersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Customer Found"));
		BeanUtils.copyProperties(updatedCustomer, existingCustomer, getNullPropertyNames(updatedCustomer));
		return customersRepository.save(existingCustomer);
	}
	
	//Find the properties which are not present in the request
    private String[] getNullPropertyNames(Customers customer) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(customer);
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
