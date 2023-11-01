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
import co.bharat.sudarshansaur.dto.WarrantyDealerMappingDTO;
import co.bharat.sudarshansaur.dto.WarrantyDetailsDTO;
import co.bharat.sudarshansaur.dto.WarrantyStockistMappingDTO;
import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.entity.StockistDealerWarranty;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.entity.WarrantyRequests;
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
	
	@GetMapping
	public ResponseEntity<ResponseData<List<StockistDealerWarranty>>> getAllSDW() {
		List<StockistDealerWarranty> stockist = sdwRepository.findAll();
		return new ResponseEntity<>(new ResponseData<List<StockistDealerWarranty>>("Mappings Fetched Successfully",
				HttpStatus.OK.value(), stockist, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "stockist/{id}" })
	public ResponseEntity<ResponseData<List<WarrantyDealerMappingDTO>>> getSDWByStockist(@PathVariable Long id) {
		List<StockistDealerWarranty> stockistWarrantyDtls = sdwRepository.findByStockistId(id).orElseThrow(() -> new EntityNotFoundException("No Mappings Found"));
	    List<WarrantyDealerMappingDTO> result = new ArrayList<>();

	    for (StockistDealerWarranty warranty : stockistWarrantyDtls) {
	        long dealerId = warranty.getDealerId();
	        String warrantySerialNo = warranty.getWarrantySerialNo();

	        // Fetch the associated Dealers object based on dealerId
	        Dealers dealer = dealersRepository.findById(dealerId).orElse(null);

	        if (dealer != null) {
	            // Fetch the associated warrantyRequests object based on warrantySerialNo
	            WarrantyRequests warrantyRequests = warrantyRequestsRepository.findByWarrantyDetailsWarrantySerialNo(warrantySerialNo).orElse(null);

	            WarrantyDealerMappingDTO dto = new WarrantyDealerMappingDTO(dealer, warrantySerialNo, warrantyRequests);
                result.add(dto);
	        }
	    }
		return new ResponseEntity<>(new ResponseData<List<WarrantyDealerMappingDTO>>("Stockists Mapping Fetched Successfully",
				HttpStatus.OK.value(), result, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "dealer/{id}" })
	public ResponseEntity<ResponseData<List<WarrantyStockistMappingDTO>>> getSDWByDealer(@PathVariable Long id) {
		List<StockistDealerWarranty> dealerWarrantyDtls = sdwRepository.findByDealerId(id).orElseThrow(() -> new EntityNotFoundException("No Mappings Found"));
		List<WarrantyStockistMappingDTO> result = new ArrayList<>();

	    for (StockistDealerWarranty warranty : dealerWarrantyDtls) {
	        long stockistId = warranty.getStockistId();
	        String warrantySerialNo = warranty.getWarrantySerialNo();

	        // Fetch the associated Dealers object based on dealerId
	        Stockists stockist = stockistsRepository.findById(stockistId).orElse(null);

	        if (stockist != null) {
	            // Fetch the associated warrantyRequests object based on warrantySerialNo
	            WarrantyRequests warrantyRequests = warrantyRequestsRepository.findByWarrantyDetailsWarrantySerialNo(warrantySerialNo).orElse(null);

	            WarrantyStockistMappingDTO dto = new WarrantyStockistMappingDTO(stockist, warrantySerialNo, warrantyRequests);
	            result.add(dto);
	        }
	    }
		return new ResponseEntity<>(new ResponseData<List<WarrantyStockistMappingDTO>>("Dealer Mapping Fetched Successfully",
				HttpStatus.OK.value(), result, null), HttpStatus.OK);
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
		System.out.println("Count of Serial Nos present before: " + externalWarrantyDetailList.size());
		// Retrieve the list of warrantySerialNo values from warrantyRequests
	    List<String> warrantySerialNumbers = getWarrantySerialNumbersFromWarrantyRequests();
	    System.out.println("List of Serial Nos present: " + warrantySerialNumbers);
	    // Filter externalWarrantyDetailList to exclude items with warrantySerialNo in warrantyRequests
	    //externalWarrantyDetailList.removeIf(dto -> warrantySerialNumbers.contains(dto.getWarrantySerialNo()));
	    externalWarrantyDetailList.stream().filter((e)-> !warrantySerialNumbers.contains(e.getWarrantySerialNo())).collect(Collectors.toList());
	    System.out.println("Count of Serial Nos present after: " + externalWarrantyDetailList.size());
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
	
    public List<String> getWarrantySerialNumbersFromWarrantyRequests() {
        List<WarrantyRequests> warrantyRequests = warrantyRequestsRepository.findAll();
        return warrantyRequests.stream()
            .map(warrantyRequest -> warrantyRequest.getWarrantyDetails().getWarrantySerialNo())
            .collect(Collectors.toList());
    }
}
