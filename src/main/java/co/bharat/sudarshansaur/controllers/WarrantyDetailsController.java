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
import co.bharat.sudarshansaur.dto.WarrantyDetailsDTO;
import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.repository.CustomersRepository;
import co.bharat.sudarshansaur.repository.WarrantyDetailsRepository;
import co.bharat.sudarshansaur.service.WarrantyDetailsService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/warrantyDetails")
public class WarrantyDetailsController {
	@Autowired
	private WarrantyDetailsRepository warrantyDetailsRepository;
	@Autowired
	private WarrantyDetailsService warrantyDetailsService;
	@Autowired
	private CustomersRepository customersRepository;

	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<WarrantyDetailsDTO>> getWarrantyDetail(@PathVariable String id) {
		WarrantyDetailsDTO warrantyDetails1 = warrantyDetailsService.getWarrantyDetails(id);
		return new ResponseEntity<>(new ResponseData<WarrantyDetailsDTO>("WarrantyDetail Fetched Successfully",
				HttpStatus.OK.value(), warrantyDetails1, null), HttpStatus.OK);
		/*
		 * return new ResponseEntity<>( new
		 * ResponseData<WarrantyDetails>("No WarrantyDetail Found",
		 * HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);
		 */
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<WarrantyDetails>>> getWarrantyDetailsByAttributes(
			@RequestParam(name = "mobileNo", required = false) String invoiceNo,
			@RequestParam(name = "allocationStatus", required = false) AllocationStatus allocationStatus) {

		List<WarrantyDetails> customers;

		if (invoiceNo != null && allocationStatus != null) {
			// Fetch users by roleName and societyCode
			customers = warrantyDetailsRepository.findByInvoiceNoAndAllocationStatus(invoiceNo, allocationStatus);
		} else if (invoiceNo != null) {
			// Fetch users by roleName and relationship
			customers = warrantyDetailsRepository.findByInvoiceNo(invoiceNo);
		} else if (allocationStatus != null) {
			// Fetch users by societyCode and relationship
			customers = warrantyDetailsRepository.findByAllocationStatus(allocationStatus);
		} else {
			// Return all users if no params are specified
			customers = warrantyDetailsRepository.findAll();
		}

		if (customers.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<WarrantyDetails>>("No WarrantyDetails Found",
					HttpStatus.NOT_FOUND.value(), customers, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<WarrantyDetails>>("WarrantyDetails Fetched Successfully",
				HttpStatus.OK.value(), customers, null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<?>> createWarrantyDetail(@RequestBody WarrantyDetails warrantyDetail) {
		/*
		 * if (warrantyDetail.getCustomer() != null &&
		 * warrantyDetail.getCustomer().getCustomerId() > 0) { Optional<Customers>
		 * customerDetails = customersRepository
		 * .findById(warrantyDetail.getCustomer().getCustomerId()); if
		 * (customerDetails.isPresent()) {
		 * warrantyDetail.setCustomer(customerDetails.get()); } }
		 */
		WarrantyDetails createdWarranty = warrantyDetailsService.createWarrantyDetails(warrantyDetail);
		return new ResponseEntity<>(
				new ResponseData<>("WarrantyDetail Created Successfully", HttpStatus.OK.value(), createdWarranty, null),
				HttpStatus.OK);
	}

	@PostMapping(value = { "/many" })
	public ResponseEntity<Map<ResponseData<WarrantyDetails>, String>> createWarrantyDetails(
			@RequestBody List<WarrantyDetails> warrantyDetails) {
		Map<ResponseData<WarrantyDetails>, String> responseMap = new HashMap<>();
		for (WarrantyDetails warrantyDetail : warrantyDetails) {
			try {
				WarrantyDetails newStockist = warrantyDetailsRepository.save(warrantyDetail);
				responseMap.put(new ResponseData<WarrantyDetails>("WarrantyDetail Created Successfully",
						HttpStatus.OK.value(), newStockist, warrantyDetail.getWarrantySerialNo()), "Success");
			} catch (Exception e) {
				responseMap.put(new ResponseData<WarrantyDetails>("WarrantyDetail Creation Failed",
						HttpStatus.OK.value(), warrantyDetail, warrantyDetail.getWarrantySerialNo()), e.getMessage());
			}
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<?>> updateWarrantyDetail(@PathVariable(required = false) String id,
			@RequestBody WarrantyDetails warrantyDetail) {
		Optional<WarrantyDetails> existingUser = warrantyDetailsRepository.findById(id);
		if (!existingUser.isPresent()) {
			return new ResponseEntity<>(new ResponseData<WarrantyDetails>("WarrantyDetail Not Found",
					HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);

		}
		if (warrantyDetail.getCustomer() != null && warrantyDetail.getCustomer().getCustomerId() > 0) {
			Optional<Customers> customerDetails = customersRepository
					.findById(warrantyDetail.getCustomer().getCustomerId());
			if (customerDetails.isPresent()) {
				warrantyDetail.setCustomer(customerDetails.get());
			}
		}
		WarrantyDetails updatedWarrantyDetails = warrantyDetailsRepository.save(warrantyDetail);
		return new ResponseEntity<>(
				new ResponseData<>("WarrantyDetail Updated Successfully", HttpStatus.OK.value(), updatedWarrantyDetails, null),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWarrantyDetail(@PathVariable("id") String id) {
		warrantyDetailsRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
