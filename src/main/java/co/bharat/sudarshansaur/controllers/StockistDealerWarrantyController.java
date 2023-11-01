package co.bharat.sudarshansaur.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.dto.WarrantyDetailsDTO;
import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.entity.StockistDealerWarranty;
import co.bharat.sudarshansaur.repository.DealersRepository;
import co.bharat.sudarshansaur.repository.StockistDealerWarrantyRepository;
import co.bharat.sudarshansaur.repository.StockistsRepository;
import co.bharat.sudarshansaur.repository.WarrantyRequestsRepository;
import co.bharat.sudarshansaur.service.StockistDealerWarrantyService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/sdw")
public class StockistDealerWarrantyController {
	
	@Autowired
	private StockistDealerWarrantyRepository sdwRepository;
	
	@Autowired
	private DealersRepository dealersRepository;
	
	@Autowired
	private StockistsRepository stockistsRepository;
	
	@Autowired
	private StockistDealerWarrantyService sdwService;
	
	@Autowired
	private WarrantyRequestsRepository warrantyRequestsRepository;
	
	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<StockistDealerWarranty>> getSDW(@PathVariable Long id) {
		StockistDealerWarranty stockist = sdwRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Mappings Found"));
		return new ResponseEntity<>(new ResponseData<StockistDealerWarranty>("Mapping Fetched Successfully",
				HttpStatus.OK.value(), stockist, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "stockist/{id}" })
	public ResponseEntity<ResponseData<List<StockistDealerWarranty>>> getSDWByStockist(@PathVariable Long id) {
		List<StockistDealerWarranty> stockist = sdwRepository.findByStockistId(id).orElseThrow(() -> new EntityNotFoundException("No Mappings Found"));
		List<Long> dealerIds = stockist.stream().map(StockistDealerWarranty::getDealerId).collect(Collectors.toList());
		List<Dealers> dealers =  dealersRepository.findAllById(dealerIds);
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
	public ResponseEntity<ResponseData<List<WarrantyDetailsDTO>>> getWarrantyByStockistCode(
			@RequestParam(name = "dealer_code", required = false) String dealerCode,
			@RequestParam(name = "mobile_number", required = false) String mobileNo) {
		
		List<WarrantyDetailsDTO> externalWarrantyDetailList = new ArrayList<>();;
		if(mobileNo!=null && dealerCode != null) {
			externalWarrantyDetailList = sdwService.findWarrantyDetailsByStockistCodeFromCRM(dealerCode, mobileNo);
		}
		if (externalWarrantyDetailList.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<WarrantyDetailsDTO>>("Mapping Not Found",
					HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);
		}
		
		// Retrieve the list of warrantySerialNo values from warrantyRequests
	    List<String> warrantySerialNumbers = warrantyRequestsRepository.findWarrantySerialNumbers();
	    // Filter externalWarrantyDetailList to exclude items with warrantySerialNo in warrantyRequests
	    externalWarrantyDetailList.removeIf(dto -> warrantySerialNumbers.contains(dto.getWarrantySerialNo()));


	    if (externalWarrantyDetailList.isEmpty()) {
	        return new ResponseEntity<>(new ResponseData<List<WarrantyDetailsDTO>>("Mapping Not Found in warrantyRequests",
	                HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);
	    }

		
		return new ResponseEntity<>(new ResponseData<List<WarrantyDetailsDTO>>("Mapping Found",
				HttpStatus.OK.value(), externalWarrantyDetailList, null), HttpStatus.OK);
	}

	/*
	 * @PostMapping public ResponseEntity<ResponseData<?>>
	 * createStockist(@RequestBody List<StockistDealerWarranty> stockist) {
	 * List<StockistDealerWarranty> newStockist = sdwRepository.saveAll(stockist);
	 * return new ResponseEntity<>( new
	 * ResponseData<>("Mapping created Successfully", HttpStatus.OK.value(),
	 * newStockist, null), HttpStatus.OK); }
	 */
	
	@PostMapping
	@Transactional  // Enable transaction management
	public ResponseEntity<ResponseData<?>> createStockists(@RequestBody List<StockistDealerWarranty> stockists) {
	    try {
	        List<StockistDealerWarranty> newStockists = sdwRepository.saveAll(stockists);
	        // If there are no exceptions, commit the transaction
	        return new ResponseEntity<>(
	            new ResponseData<>("Mappings created Successfully", HttpStatus.OK.value(), newStockists, null),
	            HttpStatus.OK);
	    } catch (Exception e) {
	        // If an exception occurs, rollback the transaction
	        return new ResponseEntity<>(
	            new ResponseData<>("Failed to create mappings", HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()),
	            HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStockist(@PathVariable("id") Long id) {
		sdwRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
