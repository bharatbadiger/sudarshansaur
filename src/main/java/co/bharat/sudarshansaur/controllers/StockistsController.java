package co.bharat.sudarshansaur.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.enums.UserStatus;
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
	public ResponseEntity<ResponseData<Stockists>> getDealer(@PathVariable Long id) {
		Optional<Stockists> stockist1 = stockistRepository.findById(id);
		if (stockist1.isPresent()) {
			return new ResponseEntity<>(new ResponseData<Stockists>("Stockist Fetched Successfully",
					HttpStatus.OK.value(), stockist1.get(), null), HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseData<Stockists>("No Stockist Found", HttpStatus.NOT_FOUND.value(), null, null),
				HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value = { "/authenticate" })
	public ResponseEntity<ResponseData<Stockists>> authenticateCustomer(@Validated @RequestBody Customers customer) {
		Stockists stockist = stockistRepository.findByEmailAndPassword(customer.getEmail(),customer.getPassword()).orElseThrow(() -> new EntityNotFoundException("Incorrect email and password"));
		return new ResponseEntity<>(new ResponseData<Stockists>("Stockist Fetched Successfully",
					HttpStatus.OK.value(), stockist, null), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<Stockists>>> getStockistsByAttributes(
			@RequestParam(name = "mobileNo", required = false) String mobileNo,
			@RequestParam(name = "status", required = false) UserStatus status) {

		List<Stockists> customers;

		if (mobileNo != null && status != null) {
			// Fetch stockists by roleName and societyCode
			customers = stockistRepository.findByMobileNoAndStatus(mobileNo, status);
		} else if (mobileNo != null) {
			// Fetch stockists by roleName and relationship
			customers = stockistRepository.findByMobileNo(mobileNo);
		} else if (status != null) {
			// Fetch stockists by societyCode and relationship
			customers = stockistRepository.findByStatus(status);
		} else {
			// Return all stockists if no params are specified
			customers = stockistRepository.findAll();
		}

		if (customers.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<Stockists>>("No Stockists Found",
					HttpStatus.NOT_FOUND.value(), customers, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<Stockists>>("Stockists Fetched Successfully",
				HttpStatus.OK.value(), customers, null), HttpStatus.OK);
	}

	@PostMapping(value = { "/many" })
	public ResponseEntity<Map<ResponseData<Stockists>, String>> createStockists(@RequestBody List<Stockists> stockists) {
		Map<ResponseData<Stockists>, String> responseMap = new HashMap<>();
		for (Stockists stockist : stockists) {
			try {
				Stockists newStockist = stockistRepository.save(stockist);
				responseMap.put(new ResponseData<Stockists>("Stockist Created Successfully", HttpStatus.OK.value(),
						newStockist, stockist.getStockistId()), "Success");
			} catch (Exception e) {
				responseMap.put(new ResponseData<Stockists>("Stockist Creation Failed", HttpStatus.OK.value(),
						stockist, stockist.getStockistId()), e.getMessage());
			}
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<?>> createStockist(
			@RequestBody Stockists stockist) {
		Stockists updatedStockist = stockistRepository.save(stockist);
		return new ResponseEntity<>(
				new ResponseData<>("Stockist created Successfully", HttpStatus.OK.value(), updatedStockist, null),
				HttpStatus.OK);
	}
	
	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<?>> updateStockist(@PathVariable(required = false) Long id,
			@RequestBody Stockists stockist) {
		Stockists updatedStockist = stockistsService.updateStockist(id, stockist);
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
