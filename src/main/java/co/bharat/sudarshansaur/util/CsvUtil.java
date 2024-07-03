package co.bharat.sudarshansaur.util;

import co.bharat.sudarshansaur.dto.CDMReportDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvUtil {

    public static ByteArrayInputStream generateCSV(List<CDMReportDTO> list) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Define CSV format with headers
        final CSVFormat format = CSVFormat.DEFAULT
                .withHeader("CDM Number", "Customer's Name", "Customer's Address", "District",
                        "State or Union Territory", "Customer's Phone No.", "Dealer", "Place",
                        "Capacity (LPD)", "Model", "Invoice Number", "Invoice Date",
                        "Total Qty", "Installation date", "System Serial Number");

        try (CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (CDMReportDTO item : list) {
                // Print each record, ensuring each field is quoted
                csvPrinter.printRecord(
                        item.getCdmNumber(),
                        item.getCustomerName(),
                        item.getCustomerFullAddress(),
                        item.getCustomerDistrict(),
                        item.getState(),
                        item.getPhone(),
                        item.getStockistFirmName(),
                        item.getStockistPlace(),
                        item.getCapacity(),
                        item.getModel(),
                        item.getInvoiceNumber(),
                        item.getInvoiceDate(),
                        item.getQuantity(),
                        item.getInstallationDate(),
                        item.getSerialNumber()
                );
            }
        }

        // Return ByteArrayInputStream containing the CSV data
        return new ByteArrayInputStream(out.toByteArray());
    }
}