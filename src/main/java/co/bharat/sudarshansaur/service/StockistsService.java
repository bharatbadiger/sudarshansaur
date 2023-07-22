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

import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.repository.StockistsRepository;

@Service
public class StockistsService {

	@Autowired
	private StockistsRepository stockistsRepository;

	public List<Stockists> getAllStockists() {
		return stockistsRepository.findAll();
	}
	
	public Stockists getStockist(Long id) {
		return stockistsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Stockist Found"));
	}
	
	public Stockists saveStockist(Stockists stockist) {
		return stockistsRepository.save(stockist);
	}

	//Updates ONLY the properties which are passed in the request data
	public Stockists updateStockist(Long id, Stockists updatedStockist) {
		Stockists existingStockist = stockistsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Stockist Found"));
		BeanUtils.copyProperties(updatedStockist, existingStockist, getNullPropertyNames(updatedStockist));
		return stockistsRepository.save(existingStockist);
	}
	
	//Find the properties which are not present in the request
    private String[] getNullPropertyNames(Stockists stockist) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(stockist);
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
