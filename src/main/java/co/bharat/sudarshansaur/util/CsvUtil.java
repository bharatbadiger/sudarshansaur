package co.bharat.sudarshansaur.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


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