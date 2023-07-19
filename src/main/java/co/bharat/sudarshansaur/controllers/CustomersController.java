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
import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.repository.CustomersRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/customers")
public class CustomersController {
	@Autowired
	private CustomersRepository customerRepository;

	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<Customers>> getCustomer(@PathVariable Long id) {
		Optional<Customers> customer1 = customerRepository.findById(id);
		if (customer1.isPresent()) {
			return new ResponseEntity<>(new ResponseData<Customers>("Customer Fetched Successfully",
					HttpStatus.OK.value(), customer1.get(), null), HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseData<Customers>("No Customer Found", HttpStatus.NOT_FOUND.value(), null, null),
				HttpStatus.NOT_FOUND);
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<Customers>>> getCustomersByAttributes(
			@RequestParam(name = "mobileNo", required = false) String mobileNo,
			@RequestParam(name = "status", required = false) UserStatus status) {

		List<Customers> customers;

		if (mobileNo != null && status != null) {
			// Fetch customers by mobileNo and status
			customers = customerRepository.findByMobileNoAndStatus(mobileNo, status);
		} else if (mobileNo != null) {
			// Fetch customers by mobileNo
			customers = customerRepository.findByMobileNo(mobileNo);
		} else if (status != null) {
			// Fetch customers by status
			customers = customerRepository.findByStatus(status);
		} else {
			// Return all customers if no params are specified
			customers = customerRepository.findAll();
		}

		if (customers.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<Customers>>("No Customers Found",
					HttpStatus.NOT_FOUND.value(), customers, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<Customers>>("Customers Fetched Successfully",
				HttpStatus.OK.value(), customers, null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Map<ResponseData<Customers>, String>> createCustomer(@RequestBody List<Customers> customers) {
		Map<ResponseData<Customers>, String> responseMap = new HashMap<>();
		for (Customers customer : customers) {
			try {
				Customers newCustomer = customerRepository.save(customer);
				responseMap.put(new ResponseData<Customers>("Customer Created Successfully", HttpStatus.OK.value(),
						newCustomer, customer.getCustomerId()), "Success");
			} catch (Exception e) {
				responseMap.put(new ResponseData<Customers>("Customer Creation Failed", HttpStatus.OK.value(),
						customer, customer.getCustomerId()), e.getMessage());
			}
		}
		return new ResponseEntity<>(responseMap	, HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<?>> updateCustomer(@PathVariable(required = false) Long id,
			@RequestBody Customers customer) {
		Optional<Customers> existingCustomer = customerRepository.findById(id);
		if (!existingCustomer.isPresent()) {
			return new ResponseEntity<>(
					new ResponseData<Customers>("Customer Not Found", HttpStatus.NOT_FOUND.value(), null, null),
					HttpStatus.NOT_FOUND);

		}
		Customers updatedCustomer = customerRepository.save(customer);
		return new ResponseEntity<>(
				new ResponseData<>("Customer Updated Successfully", HttpStatus.OK.value(), updatedCustomer, null),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
		customerRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
