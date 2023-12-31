package co.bharat.sudarshansaur.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import co.bharat.sudarshansaur.repository.StockistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	@Autowired
	private StockistsRepository stockistsRepository;

	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<WarrantyDetails>> getWarrantyDetail(@PathVariable String id) {
		WarrantyDetails warrantyDetails1 = warrantyDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Warranty Detail Found"));
		return new ResponseEntity<>(new ResponseData<WarrantyDetails>("WarrantyDetail Fetched Successfully",
				HttpStatus.OK.value(), warrantyDetails1, null), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<WarrantyDetails>>> getWarrantyDetailsByAttributes(
			@RequestParam(name = "mobileNo", required = false) String invoiceNo,
			@RequestParam(name = "allocationStatus", required = false) AllocationStatus allocationStatus) {

		List<WarrantyDetails> warrantyDetailsList;

		if (invoiceNo != null && allocationStatus != null) {
			// Fetch users by roleName and societyCode
			warrantyDetailsList = warrantyDetailsRepository.findByInvoiceNoAndAllocationStatus(invoiceNo, allocationStatus);
		} else if (invoiceNo != null) {
			// Fetch users by roleName and relationship
			warrantyDetailsList = warrantyDetailsRepository.findByInvoiceNo(invoiceNo);
		} else if (allocationStatus != null) {
			// Fetch users by societyCode and relationship
			warrantyDetailsList = warrantyDetailsRepository.findByAllocationStatus(allocationStatus);
		} else {
			// Return all users if no params are specified
			warrantyDetailsList = warrantyDetailsRepository.findAll();
		}
		
		List<WarrantyDetails> warrantyRequestsFiltered = warrantyDetailsList.stream().filter(warranty->!AllocationStatus.ALLOCATED.equals(warranty.getAllocationStatus())).collect(Collectors.toList());
		
		if (warrantyDetailsList.isEmpty()) {
			return new ResponseEntity<>(new ResponseData<List<WarrantyDetails>>("No WarrantyDetails Found",
					HttpStatus.NOT_FOUND.value(), warrantyRequestsFiltered, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<WarrantyDetails>>("WarrantyDetails Fetched Successfully",
				HttpStatus.OK.value(), warrantyRequestsFiltered, null), HttpStatus.OK);
	}
	
//	@GetMapping(value = { "customer/{id}" })
//	public ResponseEntity<ResponseData<List<WarrantyDetails>>> getWarrantyDetailsForCustomer(@PathVariable Long id) {
//		List<WarrantyDetails> warrantyRequests1 = warrantyDetailsRepository.findByCustomerCustomerId(id).orElseThrow(() -> new EntityNotFoundException("No WarrantyDetails Found"));
//		List<WarrantyDetails> warrantyRequestsFiltered = warrantyRequests1.stream().filter(warranty->!AllocationStatus.ALLOCATED.equals(warranty.getAllocationStatus())).collect(Collectors.toList());
//		return new ResponseEntity<>(new ResponseData<List<WarrantyDetails>>("WarrantyDetails Fetched Successfully",
//				HttpStatus.OK.value(), warrantyRequestsFiltered, null), HttpStatus.OK);
//	}
	
//	@GetMapping(value = { "dealer/{id}" })
//	public ResponseEntity<ResponseData<List<WarrantyDetails>>> getWarrantyDetailsForDealer(@PathVariable Long id) {
//		List<WarrantyDetails> warrantyRequests1 = warrantyDetailsRepository.findByDealersDealerId(id).orElseThrow(() -> new EntityNotFoundException("No WarrantyDetails Found"));
//		List<WarrantyDetails> warrantyRequestsFiltered = warrantyRequests1.stream().filter(warranty->!AllocationStatus.ALLOCATED.equals(warranty.getAllocationStatus())).collect(Collectors.toList());
//		return new ResponseEntity<>(new ResponseData<List<WarrantyDetails>>("WarrantyDetails Fetched Successfully",
//				HttpStatus.OK.value(), warrantyRequestsFiltered, null), HttpStatus.OK);
//	}
	
//	@GetMapping(value = { "stockist/{id}" })
//	public ResponseEntity<ResponseData<List<WarrantyDetails>>> getWarrantyDetailsForStockist(@PathVariable Long id) {
//		List<WarrantyDetails> warrantyRequests1 = warrantyDetailsRepository.findByStockistsStockistId(id).orElseThrow(() -> new EntityNotFoundException("No WarrantyDetails Found"));
//		List<WarrantyDetails> warrantyRequestsFiltered = warrantyRequests1.stream().filter(warranty->AllocationStatus.ALLOCATED.equals(warranty.getAllocationStatus())).collect(Collectors.toList());
//		return new ResponseEntity<>(new ResponseData<List<WarrantyDetails>>("WarrantyDetails Fetched Successfully",
//				HttpStatus.OK.value(), warrantyRequestsFiltered, null), HttpStatus.OK);
//	}
//
	@GetMapping(value = { "stockist/mobileNo/{mobileNo}" })
	public ResponseEntity<ResponseData<?>> getWarrantyDetailsForStockistByMobileNo(@RequestParam(defaultValue = "0", name = "pageNumber", required = false) int pageNumber,
	        @RequestParam(name = "pageSize", required = false) Integer pageSize, @PathVariable String mobileNo) {
		Sort sort = Sort.by("warrantySerialNo").descending();
		Pageable pageable;
		if(pageSize==null) {
			pageable = Pageable.unpaged();
		} else {
			pageable = PageRequest.of(pageNumber, pageSize, sort);
		}
		Page<WarrantyDetails> pageResult;
//		pageResult = stockistsRepository.findByStockistsMobileNo(mobileNo, pageable);
		Map<String, Object> response = new HashMap<>();
//	    response.put("warrantyDetails", pageResult.getContent());
//	    response.put("currentPage", pageResult.getNumber());
//	    response.put("totalItems", pageResult.getTotalElements());
//	    response.put("totalPages", pageResult.getTotalPages());
		return new ResponseEntity<>(new ResponseData<>("WarrantyDetails Fetched Successfully",
				HttpStatus.OK.value(), response, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "crm/{serialNo}" })
	public ResponseEntity<ResponseData<?>> getWarrantyDetailsForWarrantySerialNo(@PathVariable String serialNo) {
		WarrantyDetails warrantyDetails = warrantyDetailsService.getWarrantyDetailsFromCRM(serialNo);
		if(warrantyDetails == null) {
			return new ResponseEntity<>(new ResponseData<WarrantyDetails>("No WarrantyDetails Found",
					HttpStatus.NOT_FOUND.value(), null, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<>("WarrantyDetails Fetched Successfully",
				HttpStatus.OK.value(), warrantyDetails, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "stockist/crm/{mobileNo}" })
	public ResponseEntity<ResponseData<List<WarrantyDetailsDTO>>> getWarrantyDetailsForStockistMobileNoFromCRM(@RequestParam(defaultValue = "1", name = "pageNumber", required = false) int pageNumber,
	        @RequestParam(name = "pageSize", required = false) Integer pageSize, @PathVariable String mobileNo) {
		List<WarrantyDetailsDTO> externalWarrantyDetails = warrantyDetailsService.findWarrantyDetailsByMobileNoFromCRM(mobileNo);
		int totalResults =externalWarrantyDetails.size();
		if(pageSize==null) {
			pageSize = totalResults;
		}
		//int totalPages = (int) Math.ceil((double) totalResults / pageSize);
		int startIndex = Math.abs((pageNumber - 1) * pageSize);
		int endIndex = Math.min(startIndex + pageSize, totalResults);
		return new ResponseEntity<>(new ResponseData<List<WarrantyDetailsDTO>>("WarrantyDetails Fetched Successfully",
				HttpStatus.OK.value(), externalWarrantyDetails.subList(startIndex, endIndex), externalWarrantyDetails.size()), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ResponseData<?>> createWarrantyDetail(@RequestBody WarrantyDetails warrantyDetail) {
		WarrantyDetails createdWarranty = warrantyDetailsService.createWarrantyDetails(warrantyDetail);
		return new ResponseEntity<>(
				new ResponseData<>("WarrantyDetail Created Successfully", HttpStatus.OK.value(), createdWarranty, null),
				HttpStatus.OK);
	}

	@PostMapping(value = { "/all" })
	public ResponseEntity<List<ResponseData<WarrantyDetails>>> createWarrantyDetails(
			@RequestBody List<WarrantyDetails> warrantyDetails) {
		List<ResponseData<WarrantyDetails>> responseList = new ArrayList<>();
		for (WarrantyDetails warrantyDetail : warrantyDetails) {
			try {
				WarrantyDetails newStockist = warrantyDetailsService.createWarrantyDetails(warrantyDetail);
				responseList.add(new ResponseData<WarrantyDetails>("WarrantyDetail Created Successfully",
						HttpStatus.OK.value(), newStockist, "Success"));
			} catch (Exception e) {
				responseList.add(new ResponseData<WarrantyDetails>("WarrantyDetail Creation Failed",
						HttpStatus.OK.value(), warrantyDetail, e.getMessage()));
			}
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<?>> updateWarrantyDetail(@PathVariable(required = false) String id,
			@RequestBody WarrantyDetails warrantyDetail) {
//		if (warrantyDetail.getCustomer() != null) {
//			Optional<Customers> customerDetails = customersRepository
//					.findById(warrantyDetail.getCustomer().getCustomerId());
//			if (customerDetails.isPresent()) {
//				warrantyDetail.setCustomer(customerDetails.get());
//			}
//		}
		WarrantyDetails updatedWarrantyDetails = warrantyDetailsService.updateWarrantyDetail(id, warrantyDetail);
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
