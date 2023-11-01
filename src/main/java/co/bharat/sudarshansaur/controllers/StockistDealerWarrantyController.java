package co.bharat.sudarshansaur.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.bharat.sudarshansaur.dto.ExternalWarrantyDetailsDTO;
import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.dto.WarrantyDetailsDTO;
import co.bharat.sudarshansaur.entity.StockistDealerWarranty;
import co.bharat.sudarshansaur.repository.StockistDealerWarrantyRepository;
import co.bharat.sudarshansaur.service.StockistDealerWarrantyService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/sdw")
public class StockistDealerWarrantyController {
	
	@Autowired
	private StockistDealerWarrantyRepository sdwRepository;
	
	@Autowired
	private StockistDealerWarrantyService sdwService;
	
	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<StockistDealerWarranty>> getSDW(@PathVariable Long id) {
		StockistDealerWarranty stockist = sdwRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Mappings Found"));
		return new ResponseEntity<>(new ResponseData<StockistDealerWarranty>("Mapping Fetched Successfully",
				HttpStatus.OK.value(), stockist, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "stockist/{id}" })
	public ResponseEntity<ResponseData<List<StockistDealerWarranty>>> getSDWByStockist(@PathVariable Long id) {
		List<StockistDealerWarranty> stockist = sdwRepository.findByStockistId(id).orElseThrow(() -> new EntityNotFoundException("No Mappings Found"));
		return new ResponseEntity<>(new ResponseData<List<StockistDealerWarranty>>("Stockists Mapping Fetched Successfully",
				HttpStatus.OK.value(), stockist, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "dealer/{id}" })
	public ResponseEntity<ResponseData<List<StockistDealerWarranty>>> getSDWByDealer(@PathVariable Long id) {
		List<StockistDealerWarranty> stockist = sdwRepository.findByDealerId(id).orElseThrow(() -> new EntityNotFoundException("No Mappings Found"));
		return new ResponseEntity<>(new ResponseData<List<StockistDealerWarranty>>("Dealer Mapping Fetched Successfully",
				HttpStatus.OK.value(), stockist, null), HttpStatus.OK);
	}

	@GetMapping(value = { "/crm" })
	public ResponseEntity<ResponseData<List<ExternalWarrantyDetailsDTO>>> getWarrantyByStockistCode(
			@RequestParam(name = "dealer_code", required = false) String dealerCode,
			@RequestParam(name = "mobile_number", required = false) String mobileNo) {
		
		List<ExternalWarrantyDetailsDTO> externalWarrantyDetailList = new ArrayList<>();;
		if(mobileNo!=null && dealerCode != null) {
			externalWarrantyDetailList = sdwService.findWarrantyDetailsByStockistCodeFromCRM(dealerCode, mobileNo);
		}
		if (externalWarrantyDetailList.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<ExternalWarrantyDetailsDTO>>("Mapping Not Found",
					HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<ExternalWarrantyDetailsDTO>>("Mapping Found",
				HttpStatus.OK.value(), externalWarrantyDetailList, null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<?>> createStockist(@RequestBody StockistDealerWarranty stockist) {
		StockistDealerWarranty newStockist = sdwRepository.save(stockist);
		return new ResponseEntity<>(
				new ResponseData<>("Mapping created Successfully", HttpStatus.OK.value(), newStockist, null),
				HttpStatus.OK);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStockist(@PathVariable("id") Long id) {
		sdwRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
