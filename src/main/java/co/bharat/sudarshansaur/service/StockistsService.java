package co.bharat.sudarshansaur.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.bharat.sudarshansaur.dto.ExternalStockistsDetailsDTO;
import co.bharat.sudarshansaur.dto.ExternalStockistsDetailsResultWrapper;
import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.dto.StockistsResponseDTO;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.repository.StockistsRepository;

@Service
public class StockistsService {

	@Autowired
	private StockistsRepository stockistsRepository;
	@Autowired
	RestTemplate restTemplate;
	@Value("${crm.validation.url}")
	private String crmUrl;
	@Value("${crm.warranty.list.mobileno.url}")
	private String crmMobileNoUrl;
	@Value("${crm.validation.dealerbycodeandnumber.url}")
	private String crmDealerByCodeAndNumberUrl;
	@Value("${crm.validation.dealerbycode.url}")
	private String crmDealerByCodeUrl;

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

//	public StockistsResponseDTO findByEmailAndPassword(Stockists stockist) {
//		return convertToDTO(stockistsRepository.findByEmailAndPassword(stockist.getEmail(), stockist.getPassword())
//				.orElseThrow(() -> new EntityNotFoundException("Incorrect email and password")));
//	}
	
	public List<ExternalStockistsDetailsDTO> getStockistByDealerCodeAndMobileNo(String dealerCode, String mobileNo){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("dealer_code", dealerCode);
		formData.add("mobile_number", mobileNo);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
		List<ExternalStockistsDetailsDTO> externalStockistsDTOList;
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(crmDealerByCodeAndNumberUrl, requestEntity, String.class);
			String jsonResponse = response.getBody();
			//System.out.println(jsonResponse);
			ObjectMapper objectMapper = new ObjectMapper();
			ExternalStockistsDetailsResultWrapper resultWrapper = objectMapper.readValue(jsonResponse,
					ExternalStockistsDetailsResultWrapper.class);
			externalStockistsDTOList = resultWrapper.getResults();
		} catch (JsonProcessingException je) {
			System.out.println("Error in parsing response!");
			throw new EntityNotFoundException("Stockist Not Found");
		}
		if (externalStockistsDTOList.isEmpty()) {
			throw new EntityNotFoundException("Stockist Not Found");
		}
		
		return externalStockistsDTOList;
		
	}
	
	public List<ExternalStockistsDetailsDTO> getStockistByDealerCode(String dealerCode){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("dealer_code", dealerCode);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
		List<ExternalStockistsDetailsDTO> externalStockistsDTOList;
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(crmDealerByCodeUrl, requestEntity, String.class);
			String jsonResponse = response.getBody();
			//System.out.println(jsonResponse);
			ObjectMapper objectMapper = new ObjectMapper();
			ExternalStockistsDetailsResultWrapper resultWrapper = objectMapper.readValue(jsonResponse,
					ExternalStockistsDetailsResultWrapper.class);
			externalStockistsDTOList = resultWrapper.getResults();
		} catch (JsonProcessingException je) {
			System.out.println("Error in parsing response!");
			throw new EntityNotFoundException("Stockist Not Found");
		}
		if (externalStockistsDTOList.isEmpty()) {
			throw new EntityNotFoundException("Stockist Not Found");
		}
		
		stockistsRepository.findByStockistCode(dealerCode).orElseThrow(() -> new EntityNotFoundException("Stockist Not Registered"));
		
		return externalStockistsDTOList;
		
	}
}
