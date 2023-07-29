package co.bharat.sudarshansaur.service;

import java.beans.PropertyDescriptor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

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

import co.bharat.sudarshansaur.dto.ExternalWarrantyDetailsDTO;
import co.bharat.sudarshansaur.dto.ExternalWarrantyDetailsResultWrapper;
import co.bharat.sudarshansaur.dto.WarrantyDetailsDTO;
import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.enums.UserType;
import co.bharat.sudarshansaur.repository.CustomersRepository;
import co.bharat.sudarshansaur.repository.DealersRepository;
import co.bharat.sudarshansaur.repository.StockistsRepository;
import co.bharat.sudarshansaur.repository.WarrantyDetailsRepository;

@Service
public class WarrantyDetailsService {

	@Autowired
	private WarrantyDetailsRepository warrantyDetailsRepository;
	@Autowired
	private CustomersRepository customersRepository;
	@Autowired
	private DealersRepository dealersRepository;
	@Autowired
	private StockistsRepository stockistsRepository;
	@Autowired
	RestTemplate restTemplate;
	@Value("${crm.validation.url}")
	private String crmUrl;

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
    
	@Transactional
	public WarrantyDetails createWarrantyDetails(WarrantyDetails warrantyDetailsRequests) {
		WarrantyDetails parsedWarrantyDetail = new WarrantyDetails();
		if (warrantyDetailsRequests.getWarrantySerialNo() != null) {
			parsedWarrantyDetail= validateAndGetWarrantyDetailsFromCRM(warrantyDetailsRequests.getWarrantySerialNo());
			Stockists stockist = stockistsRepository.findByMobileNo(parsedWarrantyDetail.getCrmStockistMobileNo());
			if(stockist == null) {
				Stockists newStockist = stockistsRepository.save(Stockists.builder()
						.email(parsedWarrantyDetail.getCrmStockistEmail())
						.mobileNo(parsedWarrantyDetail.getCrmStockistMobileNo())
						.stockistName(parsedWarrantyDetail.getCrmStockistName())
						.password(base64Encode(parsedWarrantyDetail.getCrmStockistMobileNo()))
						.build());
				parsedWarrantyDetail.setStockists(newStockist);
				
			} else {
				parsedWarrantyDetail.setStockists(stockist);
			}
		}
		if (UserType.CUSTOMER.equals(warrantyDetailsRequests.getInitUserType())) {
			parsedWarrantyDetail.setCustomer(customersRepository.findById(warrantyDetailsRequests.getCustomer().getCustomerId())
					.orElseThrow(() -> new EntityNotFoundException("No Customer Found")));
		}
		if (UserType.DEALER.equals(warrantyDetailsRequests.getInitUserType())) {
			parsedWarrantyDetail.setDealers(dealersRepository.findById(warrantyDetailsRequests.getDealers().getDealerId())
					.orElseThrow(() -> new EntityNotFoundException("No Dealer Found")));
			//Add the customer if not present already(customerId will not be present)
			if(warrantyDetailsRequests.getCustomer().getCustomerId()==0) {
				warrantyDetailsRequests.getCustomer().setPassword(base64Encode(parsedWarrantyDetail.getCrmCustomerMobileNo()));
				Customers newCustomer = customersRepository.save(warrantyDetailsRequests.getCustomer());
				parsedWarrantyDetail.setCustomer(newCustomer);
			} else {
				parsedWarrantyDetail.setCustomer(customersRepository.findById(warrantyDetailsRequests.getCustomer().getCustomerId())
						.orElseThrow(() -> new EntityNotFoundException("No Customer Found")));
			}
		}
		WarrantyDetails newWarrantyDetails = warrantyDetailsRepository.save(parsedWarrantyDetail);
		return newWarrantyDetails;

	}
	
	public WarrantyDetails validateAndGetWarrantyDetailsFromCRM(String warrantySerialNo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); 
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("serial_no", warrantySerialNo);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
		List<ExternalWarrantyDetailsDTO> externalWarrantyDetailsDTOList;
		try {
		ResponseEntity<String> response = restTemplate.postForEntity(crmUrl, requestEntity,String.class);
		String jsonResponse = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		//externalWarrantyDetailsDTO = objectMapper.readValue(jsonResponse, ExternalWarrantyDetailsDTO.class);
		ExternalWarrantyDetailsResultWrapper resultWrapper = objectMapper.readValue(jsonResponse, ExternalWarrantyDetailsResultWrapper.class);
        externalWarrantyDetailsDTOList = resultWrapper.getResults();
		} catch (JsonProcessingException je) {
			System.out.println("Error in parsing response!");
			throw new EntityNotFoundException("This Warranty is not found in CRM");
		}
		//ExternalWarrantyDetailsDTO responseBody = response.getBody();
		ExternalWarrantyDetailsDTO responseFromCRM = externalWarrantyDetailsDTOList.get(0);
		if(responseFromCRM ==null) {
			throw new EntityNotFoundException("This Warranty is not found in CRM");
		}
		WarrantyDetails warrantyDetail= new WarrantyDetails();
		BeanUtils.copyProperties(responseFromCRM, warrantyDetail);
		return warrantyDetail;
	}
	
    public static String base64Encode(String text) {
        byte[] encodedBytes = Base64.getEncoder().encode(text.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}
