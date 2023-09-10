package co.bharat.sudarshansaur.controllers;

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

import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.dto.WarrantyRequestsDTO;
import co.bharat.sudarshansaur.entity.WarrantyRequests;
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.repository.WarrantyRequestsRepository;
import co.bharat.sudarshansaur.service.WarrantyRequestsService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/warrantyRequests")
public class WarrantyRequestsController {
	@Autowired
	private WarrantyRequestsRepository warrantyRequestsRepository;
	@Autowired
	private WarrantyRequestsService warrantyRequestsService;

	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<WarrantyRequests>> getWarrantyRequest(@PathVariable Long id) {
		WarrantyRequests warrantyRequests = warrantyRequestsRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No WarrantyRequest Found"));
		return new ResponseEntity<>(new ResponseData<WarrantyRequests>("WarrantyRequest Fetched Successfully",
				HttpStatus.OK.value(), warrantyRequests, null), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<WarrantyRequests>>> getWarrantyRequestsByAttributes(
			@RequestParam(name = "warrantySerialNo", required = false) String warrantySerialNo,
			@RequestParam(name = "allocationStatus", required = false) AllocationStatus allocationStatus) {

		List<WarrantyRequests> warrantyRequestsList=null;

		if (warrantySerialNo != null && allocationStatus != null) {
			// Fetch users by roleName and societyCode
			//customers = warrantyRequestsRepository.findByInvoiceNoAndAllocationStatus(invoiceNo, allocationStatus);
		} else if (warrantySerialNo != null) {
			// Fetch users by roleName and relationship
			//customers = warrantyRequestsRepository.findByInvoiceNo(invoiceNo);
		} else if (allocationStatus != null) {
			// Fetch users by societyCode and relationship
			//customers = warrantyRequestsRepository.findByAllocationStatus(allocationStatus);
		} else {
			// Return all users if no params are specified
			warrantyRequestsList = warrantyRequestsRepository.findAllByOrderByCreatedOnDesc();
		}

		if (warrantyRequestsList==null) {
			return new ResponseEntity<>(new ResponseData<List<WarrantyRequests>>("No WarrantyRequests Found",
					HttpStatus.NOT_FOUND.value(), warrantyRequestsList, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseData<List<WarrantyRequests>>("WarrantyRequests Fetched Successfully",
				HttpStatus.OK.value(), warrantyRequestsList, null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<?>> createWarrantyRequests(@Validated @RequestBody WarrantyRequestsDTO warrantyRequests) {
		WarrantyRequests newWarrantyRequests = warrantyRequestsService.saveWarrantyRequests(warrantyRequests);
		return new ResponseEntity<>(
				new ResponseData<>("WarrantyRequest Created Successfully", HttpStatus.OK.value(), newWarrantyRequests, null),
				HttpStatus.OK);
	}

	@PostMapping(value = { "/many" })
	public ResponseEntity<Map<ResponseData<WarrantyRequests>, String>> createWarrantyRequests(
			@RequestBody List<WarrantyRequests> warrantyRequests) {
		Map<ResponseData<WarrantyRequests>, String> responseMap = new HashMap<>();
		for (WarrantyRequests warrantyRequest : warrantyRequests) {
			try {
				WarrantyRequests newStockist = warrantyRequestsRepository.save(warrantyRequest);
				responseMap.put(new ResponseData<WarrantyRequests>("WarrantyRequest Created Successfully",
						HttpStatus.OK.value(), newStockist, warrantyRequest.getWarrantyDetails().getWarrantySerialNo()), "Success");
			} catch (Exception e) {
				responseMap.put(new ResponseData<WarrantyRequests>("WarrantyRequest Creation Failed",
						HttpStatus.OK.value(), warrantyRequest, warrantyRequest.getWarrantyDetails().getWarrantySerialNo()), e.getMessage());
			}
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<?>> updateWarrantyRequest(@PathVariable(required = false) Long id,
			@RequestBody WarrantyRequestsDTO warrantyRequests) {
		WarrantyRequests updatedWarrantyRequests = warrantyRequestsService.saveWarrantyRequests(warrantyRequests);
		return new ResponseEntity<>(
				new ResponseData<>("WarrantyRequest Updated Successfully", HttpStatus.OK.value(), updatedWarrantyRequests, null),
				HttpStatus.OK);
	}
	
	@GetMapping(value = { "customer/{id}" })
	public ResponseEntity<ResponseData<List<WarrantyRequestsDTO>>> getWarrantyRequestForCustomer(@PathVariable Long id) {
		List<WarrantyRequestsDTO> warrantyRequests1 = warrantyRequestsService.getAllWarrantyRequestsForCustomer(id);
		return new ResponseEntity<>(new ResponseData<List<WarrantyRequestsDTO>>("WarrantyRequests Fetched Successfully",
				HttpStatus.OK.value(), warrantyRequests1, null), HttpStatus.OK);
	}
	
	@GetMapping(value = { "dealer/{id}" })
	public ResponseEntity<ResponseData<List<WarrantyRequestsDTO>>> getWarrantyRequestForDealer(@PathVariable Long id) {
		List<WarrantyRequestsDTO> warrantyRequests1 = warrantyRequestsService.getAllWarrantyRequestsForDealer(id);
		return new ResponseEntity<>(new ResponseData<List<WarrantyRequestsDTO>>("WarrantyRequest Fetched Successfully",
				HttpStatus.OK.value(), warrantyRequests1, null), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWarrantyRequest(@PathVariable("id") Long id) {
		warrantyRequestsRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
