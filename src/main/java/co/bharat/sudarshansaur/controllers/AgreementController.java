package co.bharat.sudarshansaur.controllers;

import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.dto.WarrantyRequestsDTO;
import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.entity.WarrantyRequests;
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.repository.DealersRepository;
import co.bharat.sudarshansaur.repository.StockistsRepository;
import co.bharat.sudarshansaur.repository.WarrantyRequestsRepository;
import co.bharat.sudarshansaur.service.WarrantyRequestsService;
import co.bharat.sudarshansaur.util.AgreementFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/sudarshansaur/agreement")
public class AgreementController {
	@Autowired
	private DealersRepository dealersRepository;

	@Autowired
	private StockistsRepository stockistsRepository;

	@GetMapping(value = { "/stockist/{id}" })
	public void getAgreementForStockist(@PathVariable Long id, HttpServletResponse response) throws IOException {
		Stockists stockists = stockistsRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No dealer Found"));

		response.setContentType("application/pdf");

		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=Agreement.pdf";
		response.setHeader(headerkey, headervalue);
		AgreementFactory.generateStockistAgreement(response,stockists);
	}

	@GetMapping(value = { "/dealer/{id}" })
	public void getAgreementForDealer(@PathVariable Long id, HttpServletResponse response) throws IOException {
		Dealers dealer = dealersRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No dealer Found"));

		response.setContentType("application/pdf");

		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=Agreement.pdf";
		response.setHeader(headerkey, headervalue);
		AgreementFactory.generateDealerAgreement(response,dealer);
	}

}
