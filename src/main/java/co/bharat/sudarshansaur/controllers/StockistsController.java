package co.bharat.sudarshansaur.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.bharat.sudarshansaur.dto.ExternalStockistsDetailsDTO;
import co.bharat.sudarshansaur.dto.ExternalWarrantyDetailsResultWrapper;
import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.dto.StockistsResponseDTO;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.interfaces.Users;
import co.bharat.sudarshansaur.repository.StockistsRepository;
import co.bharat.sudarshansaur.service.StockistsService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/stockists")
public class StockistsController {
	@Autowired
	private StockistsRepository stockistRepository;
	@Autowired
	private StockistsService stockistsService;

	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<StockistsResponseDTO>> getDealer(@PathVariable Long id) {
		StockistsResponseDTO stockist = stockistsService.getStockist(id);
		return new ResponseEntity<>(new ResponseData<StockistsResponseDTO>("Stockist Fetched Successfully",
				HttpStatus.OK.value(), stockist, null), HttpStatus.OK);
	}

//	@PostMapping(value = { "/authenticate" })
//	public ResponseEntity<ResponseData<StockistsResponseDTO>> authenticateCustomer(@Validated @RequestBody Stockists stockists) {
//		StockistsResponseDTO stockist = stockistsService.findByEmailAndPassword(stockists);
//		return new ResponseEntity<>(
//				new ResponseData<StockistsResponseDTO>("Stockist Fetched Successfully", HttpStatus.OK.value(), stockist, null),
//				HttpStatus.OK);
//	}

	@GetMapping
	public ResponseEntity<ResponseData<List<StockistsResponseDTO>>> getStockistsByAttributes(
			@RequestParam(name = "mobileNo", required = false) String mobileNo,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "status", required = false) UserStatus status) {

		List<Stockists> stockistsList = new ArrayList<>();

		if (mobileNo != null && email != null && status != null) {
			stockistsList.add(stockistRepository.findByMobileNoAndEmailAndStatus(mobileNo, email, status));
		} else if (mobileNo != null && email != null) {
			stockistsList.add(stockistRepository.findByMobileNoAndEmail(mobileNo, email));
		} else if (mobileNo != null && status != null) {
			stockistsList.add(stockistRepository.findByMobileNoAndStatus(mobileNo, status));
		} else if (mobileNo != null) {
			stockistsList.add(stockistRepository.findByMobileNo(mobileNo));
		} else if (status != null) {
			stockistsList = stockistRepository.findByStatus(status);
		} else {
			stockistsList = stockistRepository.findAll();
		}

		if (stockistsList.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<StockistsResponseDTO>>("No Stockists Found",
					HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<StockistsResponseDTO>>("Stockists Fetched Successfully",
				HttpStatus.OK.value(), stockistsService.convertToDTOList(stockistsList), null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "/crm" })
	public ResponseEntity<ResponseData<List<ExternalStockistsDetailsDTO>>> getStockistsByCodeAndNumber(
			@RequestParam(name = "dealer_code", required = false) String dealerCode,
			@RequestParam(name = "mobile_number", required = false) String mobileNo) {
		
		List<ExternalStockistsDetailsDTO> externalStockistDTOList = new ArrayList<>();;
		if(mobileNo!=null && dealerCode != null) {
			externalStockistDTOList = stockistsService.getStockistByDealerCodeAndMobileNo(dealerCode, mobileNo);
		} else if (mobileNo==null && dealerCode != null) {
			externalStockistDTOList = stockistsService.getStockistByDealerCode(dealerCode);
		}
		if (externalStockistDTOList.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<ExternalStockistsDetailsDTO>>("Stockist Not Found",
					HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<ExternalStockistsDetailsDTO>>("Stockists Fetched Successfully",
				HttpStatus.OK.value(), externalStockistDTOList, null), HttpStatus.OK);
	}

	@PostMapping(value = { "/many" })
	public ResponseEntity<Map<ResponseData<Users>, String>> createStockists(@RequestBody List<Stockists> stockists) {
		Map<ResponseData<Users>, String> responseMap = new HashMap<>();
		for (Stockists stockist : stockists) {
			try {
				StockistsResponseDTO newStockist = stockistsService.saveStockist(stockist);
				responseMap.put(new ResponseData<>("Stockist Created Successfully", HttpStatus.OK.value(), newStockist,
						stockist.getStockistId()), "Success");
			} catch (Exception e) {
				responseMap.put(new ResponseData<>("Stockist Creation Failed", HttpStatus.OK.value(), stockist,
						stockist.getStockistId()), e.getMessage());
			}
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<?>> createStockist(@RequestBody Stockists stockist) {
		StockistsResponseDTO newStockist = stockistsService.saveStockist(stockist);
		return new ResponseEntity<>(
				new ResponseData<>("Stockist created Successfully", HttpStatus.OK.value(), newStockist, null),
				HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<?>> updateStockist(@PathVariable(required = false) Long id,
			@RequestBody Stockists stockist) {
		StockistsResponseDTO updatedStockist = stockistsService.updateStockistAndReturnSTO(id, stockist);
		return new ResponseEntity<>(
				new ResponseData<>("Stockist Updated Successfully", HttpStatus.OK.value(), updatedStockist, null),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStockist(@PathVariable("id") Long id) {
		stockistRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
