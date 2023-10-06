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

import co.bharat.sudarshansaur.dto.DealersResponseDTO;
import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.interfaces.Users;
import co.bharat.sudarshansaur.repository.DealersRepository;
import co.bharat.sudarshansaur.service.DealersService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/dealers")
public class DealersController {
	@Autowired
	private DealersRepository dealerRepository;
	@Autowired
	private DealersService dealersService;

	@GetMapping(value = { "/{id}" })
	public ResponseEntity<ResponseData<DealersResponseDTO>> getDealer(@PathVariable Long id) {
		DealersResponseDTO dealer = dealersService.getDealer(id);
		return new ResponseEntity<>(new ResponseData<DealersResponseDTO>("Dealer Fetched Successfully",
				HttpStatus.OK.value(), dealer, null), HttpStatus.OK);
	}

//	@PostMapping(value = { "/authenticate" })
//	public ResponseEntity<ResponseData<DealersResponseDTO>> authenticateCustomer(
//			@Validated @RequestBody Dealers dealers) {
//		DealersResponseDTO dealer = dealersService.findByEmailAndPassword(dealers);
//		return new ResponseEntity<>(new ResponseData<DealersResponseDTO>("Dealer Fetched Successfully",
//				HttpStatus.OK.value(), dealer, null), HttpStatus.OK);
//	}

	@GetMapping
	public ResponseEntity<ResponseData<List<DealersResponseDTO>>> getDealersByAttributes(
			@RequestParam(name = "mobileNo", required = false) String mobileNo,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "status", required = false) UserStatus status) {

		List<Dealers> dealersList;
		if (mobileNo != null && email != null && status != null) {
			dealersList = dealerRepository.findByMobileNoAndEmailAndStatus(mobileNo, email, status);
		} else if (mobileNo != null && email != null) {
			dealersList = dealerRepository.findByMobileNoAndEmail(mobileNo, email);
		} else if (mobileNo != null && status != null) {
			// Fetch dealers by roleName and societyCode
			dealersList = dealerRepository.findByMobileNoAndStatus(mobileNo, status);
		} else if (mobileNo != null) {
			// Fetch dealers by roleName and relationship
			dealersList = dealerRepository.findByMobileNo(mobileNo);
		} else if (status != null) {
			// Fetch dealers by societyCode and relationship
			dealersList = dealerRepository.findByStatus(status);
		} else {
			// Return all dealers if no params are specified
			dealersList = dealerRepository.findAll();
		}

		if (dealersList.isEmpty()) {
			return new ResponseEntity<>(
					new ResponseData<List<DealersResponseDTO>>("No Dealers Found", HttpStatus.NOT_FOUND.value(), null, null),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(
				new ResponseData<List<DealersResponseDTO>>("Dealers Fetched Successfully", HttpStatus.OK.value(), dealersService.convertToDTOList(dealersList), null),
				HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<?>> createDealer(@PathVariable(required = false) Long id,
			@RequestBody Dealers dealer) {
		DealersResponseDTO updatedDealer = dealersService.saveDealer(dealer);
		return new ResponseEntity<>(
				new ResponseData<>("Dealer Created Successfully", HttpStatus.OK.value(), updatedDealer, null),
				HttpStatus.OK);
	}

	@PostMapping(value = { "/many" })
	public ResponseEntity<Map<ResponseData<Users>, String>> createDealers(@RequestBody List<Dealers> dealers) {
		Map<ResponseData<Users>, String> responseMap = new HashMap<>();
		for (Dealers dealer : dealers) {
			try {
				DealersResponseDTO updatedDealer = dealersService.saveDealer(dealer);
				responseMap.put(new ResponseData<Users>("Dealer Created Successfully", HttpStatus.OK.value(),
						updatedDealer, dealer.getDealerId()), "Success");
			} catch (Exception e) {
				responseMap.put(new ResponseData<Users>("Dealer Creation Failed", HttpStatus.OK.value(), dealer,
						dealer.getDealerId()), e.getMessage());
			}
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@PutMapping(value = { "/", "/{id}" })
	public ResponseEntity<ResponseData<DealersResponseDTO>> updateDealer(@PathVariable(required = false) Long id,
			@RequestBody Dealers dealer) {
		DealersResponseDTO updatedDealer = dealersService.updateDealerAndReturnDTO(id, dealer);
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
