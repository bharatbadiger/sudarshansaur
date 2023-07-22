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

import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.repository.DealersRepository;

@Service
public class DealersService {

	@Autowired
	private DealersRepository dealersRepository;

	public List<Dealers> getAllDealers() {
		return dealersRepository.findAll();
	}
	
	public Dealers getDealer(Long id) {
		return dealersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Dealer Found"));
	}
	
	public Dealers saveDealer(Dealers dealer) {
		return dealersRepository.save(dealer);
	}

	//Updates ONLY the properties which are passed in the request data
	public Dealers updateDealer(Long id, Dealers updatedDealer) {
		Dealers existingDealer = dealersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Dealer Found"));
		BeanUtils.copyProperties(updatedDealer, existingDealer, getNullPropertyNames(updatedDealer));
		return dealersRepository.save(existingDealer);
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
}
