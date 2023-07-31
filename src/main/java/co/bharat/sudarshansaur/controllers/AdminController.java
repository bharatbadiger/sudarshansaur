package co.bharat.sudarshansaur.controllers;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.entity.Admin;
import co.bharat.sudarshansaur.repository.AdminRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/admin")
public class AdminController {
	@Autowired
	private AdminRepository adminRepository;


	@GetMapping(value = { "/{mobileNo}" })
	public ResponseEntity<ResponseData<Admin>> getAdmin(@PathVariable String mobileNo) {
		Admin admin = adminRepository.findById(mobileNo).orElseThrow(() -> new EntityNotFoundException("Admin Not Found"));
		return new ResponseEntity<>(new ResponseData<Admin>("Admin Fetched Successfully",
				HttpStatus.OK.value(), admin, null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseData<Admin>> createAdmin(@RequestBody Admin admin) {
		Admin newAdmin = adminRepository.save(admin);
		return new ResponseEntity<>(
				new ResponseData<>("Admin Created Successfully", HttpStatus.OK.value(), newAdmin, null),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAdmin(@PathVariable("id") String id) {
		adminRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
