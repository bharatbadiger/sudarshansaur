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

import co.bharat.sudarshansaur.dto.StockistsResponseDTO;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.repository.StockistsRepository;

@Service
public class StockistsService {

	@Autowired
	private StockistsRepository stockistsRepository;

	public List<Stockists> getAllStockists() {
		return stockistsRepository.findAll();
	}

	public StockistsResponseDTO getStockist(Long id) {
		return convertToDTO(
				stockistsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Stockist Found")));
	}

	public StockistsResponseDTO saveStockist(Stockists stockist) {
		Stockists newStockist = stockistsRepository.save(stockist);
		return convertToDTO(newStockist);
	}

	// Updates ONLY the properties which are passed in the request data
	public Stockists updateStockist(Long id, Stockists updatedStockist) {
		Stockists existingStockist = stockistsRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No Stockist Found"));
		BeanUtils.copyProperties(updatedStockist, existingStockist, getNullPropertyNames(updatedStockist));
		return stockistsRepository.save(existingStockist);
	}

	public StockistsResponseDTO updateStockistAndReturnSTO(Long id, Stockists updatedStockist) {
		return convertToDTO(updateStockist(id, updatedStockist));
	}

	// Find the properties which are not present in the request
	private String[] getNullPropertyNames(Stockists stockist) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(stockist);
		List<String> nullPropertyNames = new ArrayList<>();
		for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
			String propertyName = propertyDescriptor.getName();
			Object propertyValue = beanWrapper.getPropertyValue(propertyName);
			// Exclude properties with numeric types from being considered for updating
			if (propertyValue == null || (propertyValue instanceof String && ((String) propertyValue).isEmpty())
					|| (long.class.isAssignableFrom(propertyDescriptor.getPropertyType()))) {
				nullPropertyNames.add(propertyName);
			}
		}
		return nullPropertyNames.toArray(new String[0]);
	}

	private StockistsResponseDTO convertToDTO(Stockists stockists) {
		StockistsResponseDTO dto = new StockistsResponseDTO();
		BeanUtils.copyProperties(stockists, dto);
		return dto;
	}
	
	public List<StockistsResponseDTO> convertToDTOList(List<Stockists> stockistsList) {
		return stockistsList.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public StockistsResponseDTO findByEmailAndPassword(Stockists stockist) {
		return convertToDTO(stockistsRepository.findByEmailAndPassword(stockist.getEmail(), stockist.getPassword())
				.orElseThrow(() -> new EntityNotFoundException("Incorrect email and password")));
	}
}
