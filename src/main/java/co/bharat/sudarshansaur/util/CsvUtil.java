package co.bharat.sudarshansaur.util;

import co.bharat.sudarshansaur.dto.CDMReportDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;

//public class CsvUtil {
//
//    public static ByteArrayInputStream generateCSV(List<CDMReportDTO> list) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        // Define CSV format with headers
//        final CSVFormat format = CSVFormat.DEFAULT
//                .withHeader("CDM Number", "Customer's Name", "Customer's Address", "District",
//                        "State or Union Territory", "Customer's Phone No.", "Dealer", "Place",
//                        "Capacity (LPD)", "Model", "Invoice Number", "Invoice Date",
//                        "Total Qty", "Installation date", "System Serial Number");
//
//        try (CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
//            for (CDMReportDTO item : list) {
//                // Print each record, ensuring each field is quoted
//                csvPrinter.printRecord(
//                        item.getCdmNumber(),
//                        item.getCustomerName(),
//                        item.getCustomerFullAddress(),
//                        item.getCustomerDistrict(),
//                        item.getState(),
//                        item.getPhone(),
//                        item.getStockistFirmName(),
//                        item.getStockistPlace(),
//                        item.getCapacity(),
//                        item.getModel(),
//                        item.getInvoiceNumber(),
//                        item.getInvoiceDate(),
//                        item.getQuantity(),
//                        item.getInstallationDate(),
//                        item.getSerialNumber()
//                );
//            }
//        }
//
//        // Return ByteArrayInputStream containing the CSV data
//        return new ByteArrayInputStream(out.toByteArray());
//    }
//}


public class CsvUtil<T> {

    private final Class<T> type;

    public CsvUtil(Class<T> type) {
        this.type = type;
    }

    public ByteArrayInputStream generateCSV(List<T> list, List<String> headers) throws IOException, IllegalAccessException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Define CSV format with custom headers
        final CSVFormat format = CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0]));

        try (CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (T item : list) {
                // Extract values for each field and print as CSV record
                Object[] values = extractValues(item, headers.size());
                csvPrinter.printRecord(values);
            }
        }

        // Return ByteArrayInputStream containing the CSV data
        return new ByteArrayInputStream(out.toByteArray());
    }

    // Helper method to extract values from an object instance
    private Object[] extractValues(T item, int numFields) throws IllegalAccessException {
        Object[] values = new Object[numFields];
        java.lang.reflect.Field[] fields = type.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            values[i] = fields[i].get(item);
        }
        return values;
    }
}