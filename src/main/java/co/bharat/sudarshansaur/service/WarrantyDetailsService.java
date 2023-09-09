package co.bharat.sudarshansaur.service;

import java.beans.PropertyDescriptor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
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
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.enums.UserStatus;
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
	@Value("${crm.warranty.list.mobileno.url}")
	private String crmMobileNoUrl;

	public List<WarrantyDetailsDTO> getAllWarrantyDetails() {
		List<WarrantyDetails> warrantyDetailsList = warrantyDetailsRepository.findAll();
		return convertToDTOList(warrantyDetailsList);
	}

	public WarrantyDetailsDTO getWarrantyDetails(String id) {
		WarrantyDetails warrantyDetails = warrantyDetailsRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No WarrantyDetail Found"));
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

	private List<WarrantyDetailsDTO> convertToDTOListExternal(List<ExternalWarrantyDetailsDTO> warrantyDetailsList) {
		return warrantyDetailsList.stream().map(this::convertToDTOExternal).collect(Collectors.toList());
	}

	private WarrantyDetailsDTO convertToDTOExternal(ExternalWarrantyDetailsDTO warrantyDetails) {
		WarrantyDetailsDTO dto = new WarrantyDetailsDTO();
		BeanUtils.copyProperties(warrantyDetails, dto);
		return dto;
	}

	// Updates ONLY the properties which are passed in the request data
	public WarrantyDetails updateWarrantyDetail(String id, WarrantyDetails updatedWarrantyDetail) {
		WarrantyDetails existingWarrantyDetail = warrantyDetailsRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No Warranty Detail Found"));
		BeanUtils.copyProperties(updatedWarrantyDetail, existingWarrantyDetail,
				getNullPropertyNames(updatedWarrantyDetail));
		return warrantyDetailsRepository.save(existingWarrantyDetail);
	}

	// Find the properties which are not present in the request
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
			warrantyDetailsRepository.findByWarrantySerialNo(warrantyDetailsRequests.getWarrantySerialNo())
					.ifPresent(existingWarrantyDetail -> {
						if (!AllocationStatus.DECLINED.equals(existingWarrantyDetail.getAllocationStatus())) {
							throw new EntityExistsException("This warranty serial no already exists!");
						}
					});
			parsedWarrantyDetail = validateAndGetWarrantyDetailsFromCRM(warrantyDetailsRequests);
			parsedWarrantyDetail.setAllocationStatus(warrantyDetailsRequests.getAllocationStatus());
			if(warrantyDetailsRequests.getStockists()==null) {
				Stockists stockist = stockistsRepository.findByMobileNo(parsedWarrantyDetail.getCrmStockistMobileNo());
				if (stockist == null) {
					Stockists newStockist = stockistsRepository
							.save(Stockists.builder().email(parsedWarrantyDetail.getCrmStockistEmail())
									.mobileNo(parsedWarrantyDetail.getCrmStockistMobileNo())
									.stockistName(parsedWarrantyDetail.getCrmStockistName())
									.password(base64Encode(parsedWarrantyDetail.getCrmStockistMobileNo()))
									.status(UserStatus.CREATED).build());
					parsedWarrantyDetail.setStockists(newStockist);
	
				} else {
					parsedWarrantyDetail.setStockists(stockist);
				}
			} else {
				parsedWarrantyDetail.setStockists(warrantyDetailsRequests.getStockists());
			}
		}
		if (warrantyDetailsRequests.getDealers() != null) {
			parsedWarrantyDetail
					.setDealers(dealersRepository.findById(warrantyDetailsRequests.getDealers().getDealerId())
							.orElseThrow(() -> new EntityNotFoundException("No Dealer Found")));
			// Add the customer if not present already(customerId will not be present)
			if (warrantyDetailsRequests.getCustomer() == null) {
				if(parsedWarrantyDetail.getCrmCustomerMobileNo()!=null) {
					Customers cust = Customers.builder().mobileNo(parsedWarrantyDetail.getCrmCustomerMobileNo())
							//.password(base64Encode(parsedWarrantyDetail.getCrmCustomerMobileNo()))
							.customerName(parsedWarrantyDetail.getCrmCustomerName()).build();
					Customers newCustomer = customersRepository.save(cust);
					parsedWarrantyDetail.setCustomer(newCustomer);
				}
			} else if (warrantyDetailsRequests.getCustomer().getCustomerId() == 0){
				warrantyDetailsRequests.getCustomer();
				//.setPassword(base64Encode(parsedWarrantyDetail.getCrmCustomerMobileNo()));
				Customers newCustomer = customersRepository.save(warrantyDetailsRequests.getCustomer());
				parsedWarrantyDetail.setCustomer(newCustomer);
			} else {
				parsedWarrantyDetail
						.setCustomer(customersRepository.findById(warrantyDetailsRequests.getCustomer().getCustomerId())
								.orElseThrow(() -> new EntityNotFoundException("No Customer Found")));
			}
		}
		WarrantyDetails newWarrantyDetails = warrantyDetailsRepository.save(parsedWarrantyDetail);
		return newWarrantyDetails;

	}

	public WarrantyDetails validateAndGetWarrantyDetailsFromCRM(WarrantyDetails warrantyDetailsRequests) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("serial_no", warrantyDetailsRequests.getWarrantySerialNo());
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
		List<ExternalWarrantyDetailsDTO> externalWarrantyDetailsDTOList;
		try {
			System.out.println("Sending Request to CRM");
			ResponseEntity<String> response = restTemplate.postForEntity(crmUrl, requestEntity, String.class);
			System.out.println("Received Response from CRM");
			String jsonResponse = response.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			ExternalWarrantyDetailsResultWrapper resultWrapper = objectMapper.readValue(jsonResponse,
					ExternalWarrantyDetailsResultWrapper.class);
			externalWarrantyDetailsDTOList = resultWrapper.getResults();
		} catch (JsonProcessingException je) {
			System.out.println("Error in parsing response!");
			throw new EntityNotFoundException("This Warranty is not found in CRM");
		}
		if (externalWarrantyDetailsDTOList.isEmpty()) {
			throw new EntityNotFoundException("This Warranty is not found in CRM");
		}
		ExternalWarrantyDetailsDTO responseFromCRM = externalWarrantyDetailsDTOList.get(0);
		if (responseFromCRM == null) {
			throw new EntityNotFoundException("This Warranty is not found in CRM");
		}
		WarrantyDetails warrantyDetail = new WarrantyDetails();
		BeanUtils.copyProperties(responseFromCRM, warrantyDetail);
		warrantyDetail.setAllocationStatus(warrantyDetailsRequests.getAllocationStatus());
		warrantyDetail.setInitUserType(warrantyDetailsRequests.getInitUserType());
		warrantyDetail.setInitiatedBy(warrantyDetailsRequests.getInitiatedBy());
		warrantyDetail.setApprovedBy(warrantyDetailsRequests.getApprovedBy());
		return warrantyDetail;
	}
	
	public WarrantyDetails getWarrantyDetailsFromCRM(String warrantySerialNo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("serial_no", warrantySerialNo);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
		List<ExternalWarrantyDetailsDTO> externalWarrantyDetailsDTOList;
		try {
			System.out.println("Sending Request to CRM");
			ResponseEntity<String> response = restTemplate.postForEntity(crmUrl, requestEntity, String.class);
			System.out.println("Received Response from CRM");
			String jsonResponse = response.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			ExternalWarrantyDetailsResultWrapper resultWrapper = objectMapper.readValue(jsonResponse,
					ExternalWarrantyDetailsResultWrapper.class);
			externalWarrantyDetailsDTOList = resultWrapper.getResults();
		} catch (JsonProcessingException je) {
			System.out.println("Error in parsing response!");
			throw new EntityNotFoundException("This Warranty is not found in CRM");
		}
		if (externalWarrantyDetailsDTOList.isEmpty()) {
			throw new EntityNotFoundException("This Warranty is not found in CRM");
		}
		ExternalWarrantyDetailsDTO responseFromCRM = externalWarrantyDetailsDTOList.get(0);
		WarrantyDetails warrantyDetail = new WarrantyDetails();
		BeanUtils.copyProperties(responseFromCRM, warrantyDetail);
		return warrantyDetail;
	}

	public static String base64Encode(String text) {
		byte[] encodedBytes = Base64.getEncoder().encode(text.getBytes(StandardCharsets.UTF_8));
		return new String(encodedBytes, StandardCharsets.UTF_8);
	}

	public List<WarrantyDetailsDTO> findWarrantyDetailsByMobileNoFromCRM(String mobileNo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("mobile_no", mobileNo);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
		List<ExternalWarrantyDetailsDTO> externalWarrantyDetailsDTOList;
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(crmMobileNoUrl, requestEntity, String.class);
			String jsonResponse = response.getBody();
			//System.out.println(jsonResponse);
			ObjectMapper objectMapper = new ObjectMapper();
			ExternalWarrantyDetailsResultWrapper resultWrapper = objectMapper.readValue(jsonResponse,
					ExternalWarrantyDetailsResultWrapper.class);
			externalWarrantyDetailsDTOList = resultWrapper.getResults();
		} catch (JsonProcessingException je) {
			System.out.println("Error in parsing response!");
			throw new EntityNotFoundException("Warranties for this Mobile No is not found in CRM");
		}
		if (externalWarrantyDetailsDTOList.isEmpty()) {
			throw new EntityNotFoundException("Warranties for this Mobile No is not found in CRM");
		}
		List<WarrantyDetails> internalWarrantyList = warrantyDetailsRepository.findByStockistsMobileNo(mobileNo, null)
				.getContent();
		if (!internalWarrantyList.isEmpty()) {
			//Get all the serialNos in a Map
			Map<String, WarrantyDetails> map2 = internalWarrantyList.stream()
					.collect(Collectors.toMap(WarrantyDetails::getWarrantySerialNo, wd -> wd));
			//Filter out serialNos present in existing DB
			List<ExternalWarrantyDetailsDTO> differenceList = externalWarrantyDetailsDTOList.stream()
	                .filter(wd -> !map2.containsKey(wd.getWarrantySerialNo()))
	                .collect(Collectors.toList());
			return convertToDTOListExternal(differenceList);

		}
		return convertToDTOListExternal(externalWarrantyDetailsDTOList);
	}
}
