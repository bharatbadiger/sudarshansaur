package co.bharat.sudarshansaur.controllers;

import co.bharat.sudarshansaur.dto.CDMReportDTO;
import co.bharat.sudarshansaur.dto.GuaranteeCardReport;
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
        List<String> customHeaders = Arrays.asList("CDM Number", "Customer's Name", "Customer's Address", "District",
                "State or Union Territory", "Customer's Phone No.", "Dealer / (Stockist)", "Place / District",
                "Capacity (LPD)", "Model", "Invoice Number", "Invoice Date",
                "Total Qty", "Installation date", "System Serial Number");
        ByteArrayInputStream byteArrayInputStream = csvUtil.generateCSV(list,customHeaders);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=cdmreport.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @GetMapping(value = {"/getGuaranteeCardReport"})
    public ResponseEntity<InputStreamResource> getGuaranteeCardReport() throws IOException, IllegalAccessException {
        System.out.println("inside getGuaranteeCardReport");
        List<GuaranteeCardReport> list = reportService.guaranteeCardReport();

        CsvUtil<GuaranteeCardReport> csvUtil = new CsvUtil<>(GuaranteeCardReport.class);
        List<String> customHeaders = Arrays.asList("Sr. No","Create Date","Customer Name","Mobile Number1","Mobile Number2","Full Address","Taluka","District	","State	","System Serial No.","Item Description	","LPD	","Model	","Company Invoice Date","Company Invoice Number","Guarantee Period","Installation Date","Stockist Code","Business Name (Stockist)","Stockist District","Dealer Name","Dealer Mobile","Dealer Place","Verification Date","Verify by (Name)","Photos Status");
        ByteArrayInputStream byteArrayInputStream = csvUtil.generateCSV(list,customHeaders);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=GuaranteeCardReport.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(byteArrayInputStream));
    }
}
