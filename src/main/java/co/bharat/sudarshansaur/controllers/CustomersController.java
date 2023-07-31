package co.bharat.sudarshansaur.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import co.bharat.sudarshansaur.dto.CustomersResponseDTO;
import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.dto.StatusCountDTO;
import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.interfaces.Users;
import co.bharat.sudarshansaur.repository.CustomersRepository;
import co.bharat.sudarshansaur.service.CustomersService;
import co.bharat.sudarshansaur.service.StatusCountService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/customers")
public class CustomersController {
	@Autowired
	private CustomersRepository customerRepository;

	@Autowired
	private CustomersService customersService;
	
	@Autowired
	private StatusCountService statusCountService;

	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<CustomersResponseDTO>> getCustomer(@PathVariable Long id) {
		CustomersResponseDTO customer = customersService.getCustomer(id);
		return new ResponseEntity<>(new ResponseData<CustomersResponseDTO>("Customer Fetched Successfully",
				HttpStatus.OK.value(), customer, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "/count" })
	public ResponseEntity<ResponseData<StatusCountDTO>> getAllcounts() {
		StatusCountDTO statusCounts = statusCountService.getMergedStatusCounts();
		return new ResponseEntity<>(new ResponseData<StatusCountDTO>("Counts Fetched Successfully",
				HttpStatus.OK.value(), statusCounts, null), HttpStatus.OK);
	}

	@PostMapping(value = { "/authenticate" })
	public ResponseEntity<ResponseData<CustomersResponseDTO>> authenticateCustomer(@Validated @RequestBody Customers customers) {
		CustomersResponseDTO customer = customersService.findByEmailAndPassword(customers);
		return new ResponseEntity<>(
				new ResponseData<CustomersResponseDTO>("Customer Fetched Successfully", HttpStatus.OK.value(), customer, null),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<CustomersResponseDTO>>> getCustomersByAttributes(
			@RequestParam(name = "mobileNo", required = false) String mobileNo,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "status", required = false) UserStatus status) {

		List<Customers> customersList;

		if (mobileNo != null && email != null && status != null) {
			customersList = customerRepository.findByMobileNoAndEmailAndStatus(mobileNo, email, status);
		} else if (mobileNo != null && email != null) {
			customersList = customerRepository.findByMobileNoAndEmail(mobileNo, email);
		} else if (mobileNo != null && status != null) {
			customersList = customerRepository.findByMobileNoAndStatus(mobileNo, status);
		} else if (mobileNo != null) {
			customersList = customerRepository.findByMobileNo(mobileNo);
		} else if (status != null) {
			customersList = customerRepository.findByStatus(status);
		} else {
			customersList = customerRepository.findAll();
		}

		if (customersList.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<CustomersResponseDTO>>("No Customers Found",
					HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<CustomersResponseDTO>>("Customers Fetched Successfully",
				HttpStatus.OK.value(), customersService.convertToDTOList(customersList), null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<CustomersResponseDTO>> createCustomer(@RequestBody Customers customer) {
		CustomersResponseDTO newCustomer = customersService.saveCustomer(customer);
		return new ResponseEntity<>(
				new ResponseData<>("Customer Created Successfully", HttpStatus.OK.value(), newCustomer, null),
				HttpStatus.OK);
	}

	@PostMapping(value = { "/many" })
	public ResponseEntity<Map<ResponseData<Users>, String>> createCustomers(@RequestBody List<Customers> customers) {
		Map<ResponseData<Users>, String> responseMap = new HashMap<>();
		for (Customers customer : customers) {
			try {
				CustomersResponseDTO newCustomer = customersService.saveCustomer(customer);
				responseMap.put(new ResponseData<Users>("Customer Created Successfully", HttpStatus.OK.value(),
						newCustomer, customer.getCustomerId()), "Success");
			} catch (Exception e) {
				responseMap.put(new ResponseData<Users>("Customer Creation Failed", HttpStatus.OK.value(), customer,
						customer.getCustomerId()), e.getMessage());
			}
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<?>> updateCustomer(@PathVariable(required = false) Long id,
			@RequestBody Customers customer) {
		CustomersResponseDTO updatedCustomer = customersService.updateCustomerAndReturnDTO(id, customer);
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
