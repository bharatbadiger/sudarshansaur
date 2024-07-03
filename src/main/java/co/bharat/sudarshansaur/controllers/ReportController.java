package co.bharat.sudarshansaur.controllers;

import co.bharat.sudarshansaur.dto.CDMReportDTO;
import co.bharat.sudarshansaur.dto.ResponseData;
import co.bharat.sudarshansaur.entity.StockistDealerWarranty;
import co.bharat.sudarshansaur.service.ReportService;
import co.bharat.sudarshansaur.util.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/saur/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(value = {"/getCDMReport"})
    public ResponseEntity<InputStreamResource> getCDMReport() throws IOException, IllegalAccessException {
        System.out.println("inside getCDMReport");
        List<CDMReportDTO> list = reportService.cdmReport();
        CsvUtil<CDMReportDTO> csvUtil = new CsvUtil<>(CDMReportDTO.class);

        ByteArrayInputStream byteArrayInputStream = csvUtil.generateCSV(list);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=cdmreport.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(byteArrayInputStream));
    }
}
