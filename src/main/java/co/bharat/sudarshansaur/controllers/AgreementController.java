package co.bharat.sudarshansaur.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.repository.DealersRepository;
import co.bharat.sudarshansaur.repository.StockistsRepository;
import co.bharat.sudarshansaur.util.AgreementFactory;

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
		InputStream pdf = AgreementFactory.generateStockistAgreement(stockists);
		org.apache.commons.io.IOUtils.copy(pdf, response.getOutputStream());
		response.flushBuffer();
	}

	@GetMapping(value = { "/dealer/{id}" })
	public void getAgreementForDealer(@PathVariable Long id, HttpServletResponse response) throws IOException {
		Dealers dealer = dealersRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No dealer Found"));

		Stockists stockists = stockistsRepository.findByStockistCode(dealer.getStockistCode()).orElseThrow(() -> new EntityNotFoundException("No Stockist Found"));

		response.setContentType("application/pdf");

		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=Agreement.pdf";
		response.setHeader(headerkey, headervalue);
		InputStream pdf = AgreementFactory.generateDealerAgreement(response,dealer, stockists);
		org.apache.commons.io.IOUtils.copy(pdf, response.getOutputStream());
		response.flushBuffer();
	}

}
