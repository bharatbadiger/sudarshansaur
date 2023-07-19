package co.bharat.sudarshansaur.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.repository.DealersRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/dealers")
public class DealersController {
	@Autowired
	private DealersRepository dealerRepository;

	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<Dealers>> getDealer(@PathVariable Long id) {
		Optional<Dealers> customer1 = dealerRepository.findById(id);
		if (customer1.isPresent()) {
			return new ResponseEntity<>(new ResponseData<Dealers>("Dealer Fetched Successfully",
					HttpStatus.OK.value(), customer1.get(), null), HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseData<Dealers>("No Dealer Found", HttpStatus.NOT_FOUND.value(), null, null),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<Dealers>>> getDealersByAttributes(
			@RequestParam(name = "mobileNo", required = false) String mobileNo,
			@RequestParam(name = "status", required = false) UserStatus status) {

		List<Dealers> customers;

		if (mobileNo != null && status != null) {
			// Fetch dealers by roleName and societyCode
			customers = dealerRepository.findByMobileNoAndStatus(mobileNo, status);
		} else if (mobileNo != null) {
			// Fetch dealers by roleName and relationship
			customers = dealerRepository.findByMobileNo(mobileNo);
		} else if (status != null) {
			// Fetch dealers by societyCode and relationship
			customers = dealerRepository.findByStatus(status);
		} else {
			// Return all dealers if no params are specified
			customers = dealerRepository.findAll();
		}

		if (customers.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<Dealers>>("No Dealers Found",
					HttpStatus.NOT_FOUND.value(), customers, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<Dealers>>("Dealers Fetched Successfully",
				HttpStatus.OK.value(), customers, null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<?>> createDealer(@PathVariable(required = false) Long id,
			@RequestBody Dealers dealer) {
		Dealers updatedDealer = dealerRepository.save(dealer);
		return new ResponseEntity<>(
				new ResponseData<>("Dealer Created Successfully", HttpStatus.OK.value(), updatedDealer, null),
				HttpStatus.OK);
	}
	
	@PostMapping(value = { "/many" })
	public ResponseEntity<Map<ResponseData<Dealers>, String>> createDealers(@RequestBody List<Dealers> customers) {
		Map<ResponseData<Dealers>, String> responseMap = new HashMap<>();
		for (Dealers customer : customers) {
			try {
				Dealers newCustomer = dealerRepository.save(customer);
				responseMap.put(new ResponseData<Dealers>("Dealer Created Successfully", HttpStatus.OK.value(),
						newCustomer, customer.getDealerId()), "Success");
			} catch (Exception e) {
				responseMap.put(new ResponseData<Dealers>("Dealer Creation Failed", HttpStatus.OK.value(),
						customer, customer.getDealerId()), e.getMessage());
			}
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<?>> updateDealer(@PathVariable(required = false) Long id,
			@RequestBody Dealers dealer) {
		Optional<Dealers> existingDealer = dealerRepository.findById(id);
		if (!existingDealer.isPresent()) {
			return new ResponseEntity<>(
					new ResponseData<Dealers>("Dealer Not Found", HttpStatus.NOT_FOUND.value(), null, null),
					HttpStatus.NOT_FOUND);

		}
		Dealers updatedDealer = dealerRepository.save(dealer);
		return new ResponseEntity<>(
				new ResponseData<>("Dealer Updated Successfully", HttpStatus.OK.value(), updatedDealer, null),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDealer(@PathVariable("id") Long id) {
		dealerRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
