package co.bharat.sudarshansaur.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
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
import co.bharat.sudarshansaur.dto.WarrantyRequestsDTO;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.entity.WarrantyRequests;
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.enums.UserType;
import co.bharat.sudarshansaur.repository.CustomersRepository;
import co.bharat.sudarshansaur.repository.DealersRepository;
import co.bharat.sudarshansaur.repository.StockistsRepository;
import co.bharat.sudarshansaur.repository.WarrantyRequestsRepository;

@Service
public class WarrantyRequestsService {

	@Autowired
	private WarrantyDetailsService warrantyDetailsService;
	@Autowired
	private WarrantyRequestsRepository warrantyRequestsRepository;
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
	

	public List<WarrantyRequestsDTO> getAllWarrantyRequests() {
		List<WarrantyRequests> warrantyRequestsList = warrantyRequestsRepository.findAll();
		return convertToDTOList(warrantyRequestsList);
	}

	public WarrantyRequestsDTO getWarrantyRequests(Long id) {
		WarrantyRequests warrantyRequests = warrantyRequestsRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No WarrantyRequest Found"));
		return convertToDTO(warrantyRequests);
	}

	private List<WarrantyRequestsDTO> convertToDTOList(List<WarrantyRequests> warrantyRequestsList) {
		return warrantyRequestsList.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private WarrantyRequestsDTO convertToDTO(WarrantyRequests warrantyRequests) {
		WarrantyRequestsDTO dto = new WarrantyRequestsDTO();
		BeanUtils.copyProperties(warrantyRequests, dto);
		// dto.setCustomers(warrantyRequests.getCustomers());
		if (warrantyRequests.getInitUserType() != null) {
			if (UserType.CUSTOMER.equals(warrantyRequests.getInitUserType())) {
				dto.setInitiatedBy(customersRepository.findById(Long.valueOf(warrantyRequests.getInitiatedBy()))
						.orElseThrow(() -> new EntityNotFoundException("No Customer Found with the given details")));
			} else if (UserType.DEALER.equals(warrantyRequests.getInitUserType())) {
				dto.setInitiatedBy(dealersRepository.findById(Long.valueOf(warrantyRequests.getInitiatedBy()))
						.orElseThrow(() -> new EntityNotFoundException("No Dealers Found with the given details")));
			}
		}

		return dto;
	}

	public List<WarrantyRequestsDTO> getAllWarrantyRequestsForCustomer(long customerId) {
		return convertToDTOList(warrantyRequestsRepository.findByCustomersCustomerId(customerId)
				.orElseThrow(() -> new EntityNotFoundException("No WarrantyRequests for this Customer Found")));
	}

	public List<WarrantyRequestsDTO> getAllWarrantyRequestsForDealer(long dealerId) {
		return convertToDTOList(warrantyRequestsRepository.findByCustomersCustomerId(dealerId)
				.orElseThrow(() -> new EntityNotFoundException("No WarrantyRequests for this Dealer Found")));
	}
	
	@Transactional
	public WarrantyRequests saveWarrantyRequests(WarrantyRequests warrantyRequests) {
		
		if (warrantyRequests.getWarrantyDetails().getWarrantySerialNo() != null) {
			WarrantyDetails warrantyDetail= validateAndGetWarrantyDetailsFromCRM(warrantyRequests.getWarrantyDetails().getWarrantySerialNo());
			Stockists stockist = stockistsRepository.findByMobileNo(warrantyDetail.getCrmStockistMobileNo());
			if(stockist == null) {
				stockistsRepository.save(Stockists.builder()
						.email(warrantyDetail.getCrmStockistEmail())
						.mobileNo(warrantyDetail.getCrmStockistMobileNo())
						.stockistName(warrantyDetail.getCrmStockistName())
						.password(base64Encode(warrantyDetail.getCrmStockistMobileNo()))
						.build());
			}
			warrantyDetail.setStockists(stockist);
		}
		if (UserType.CUSTOMER.equals(warrantyRequests.getInitUserType())) {
			warrantyRequests.setCustomers(customersRepository.findById(warrantyRequests.getCustomers().getCustomerId())
					.orElseThrow(() -> new EntityNotFoundException("No Customer Found")));
		}
		if (UserType.DEALER.equals(warrantyRequests.getInitUserType())) {
			warrantyRequests.setDealers(dealersRepository.findById(warrantyRequests.getDealers().getDealerId())
					.orElseThrow(() -> new EntityNotFoundException("No Dealer Found")));
			//Add the customer if not present already(customerId will not be present)
			if(warrantyRequests.getCustomers().getCustomerId()==0) {
				customersRepository.save(warrantyRequests.getCustomers());
			}
		}
		WarrantyRequests newWarrantyRequests = warrantyRequestsRepository.save(warrantyRequests);
		if (AllocationStatus.APPROVED.equals(warrantyRequests.getAllocationStatus())) {
			WarrantyDetails updatedWarrantyDetails = warrantyRequests.getWarrantyDetails();
			warrantyDetailsService.updateWarrantyDetail(updatedWarrantyDetails.getWarrantySerialNo(),
					updatedWarrantyDetails);
		}
		return newWarrantyRequests;

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
