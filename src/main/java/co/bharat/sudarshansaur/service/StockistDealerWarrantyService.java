package co.bharat.sudarshansaur.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

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

import co.bharat.sudarshansaur.dto.ExternalStockistWarrantyDetailsResponse;
import co.bharat.sudarshansaur.dto.ExternalWarrantyDetailsDTO;
import co.bharat.sudarshansaur.dto.WarrantyDetailsDTO;
import co.bharat.sudarshansaur.repository.StockistDealerWarrantyRepository;

@Service
public class StockistDealerWarrantyService {

	@Autowired
	private StockistDealerWarrantyRepository stockistDealerWarrantyRepository;
	@Autowired
	RestTemplate restTemplate;
	@Value("${crm.warranty.list.stockistcode.url}")
	private String crmStockistWarrantyListUrl;

	
	private List<WarrantyDetailsDTO> convertToDTOListExternal(List<ExternalWarrantyDetailsDTO> warrantyDetailsList) {
		return warrantyDetailsList.stream().map(this::convertToDTOExternal).collect(Collectors.toList());
	}

	private WarrantyDetailsDTO convertToDTOExternal(ExternalWarrantyDetailsDTO warrantyDetails) {
		WarrantyDetailsDTO dto = new WarrantyDetailsDTO();
		BeanUtils.copyProperties(warrantyDetails, dto);
		return dto;
	}
	
	public List<WarrantyDetailsDTO> findWarrantyDetailsByStockistCodeFromCRM(String stockistCode, String mobileNo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("dealer_code", stockistCode);
		formData.add("mobile_number", mobileNo);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
		List<ExternalWarrantyDetailsDTO> externalWarrantyDetailsDTOList;
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(crmStockistWarrantyListUrl, requestEntity, String.class);
			String jsonResponse = response.getBody();
			//System.out.println(jsonResponse);
			ObjectMapper objectMapper = new ObjectMapper();
			ExternalStockistWarrantyDetailsResponse resultWrapper = objectMapper.readValue(jsonResponse,
					ExternalStockistWarrantyDetailsResponse.class);
			externalWarrantyDetailsDTOList = resultWrapper.getResults().getSerial();
		} catch (JsonProcessingException je) {
			System.out.println("Error in parsing response!"+ je.getMessage());
			throw new EntityNotFoundException("Warranties for this Mobile No is not found in CRM");
		}
		if (externalWarrantyDetailsDTOList.isEmpty()) {
			throw new EntityNotFoundException("Warranties for this Mobile No is not found in CRM");
		}
		return convertToDTOListExternal(externalWarrantyDetailsDTOList);
	}
}
