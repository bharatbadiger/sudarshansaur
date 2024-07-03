package co.bharat.sudarshansaur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CDMReportDTO {
    private String cdmNumber;
    private String customerName;
    private String customerFullAddress;
    private String customerDistrict;
    private String state;
    private String phone;
    private String stockistFirmName;
    private String stockistPlace;
    private String capacity;
    private String model;
    private String invoiceNumber;
    private String invoiceDate;
    private String quantity;
    private String installationDate;
    private String serialNumber;

    @Override
    public String toString() {
        return  cdmNumber + "," +
                customerName + "," +
                customerFullAddress + "," +
                customerDistrict + "," +
                state + "," +
                phone + "," +
                stockistFirmName + "," +
                stockistPlace + "," +
                capacity + "," +
                model + "," +
                invoiceNumber + "," +
                invoiceDate + "," +
                quantity + "," +
                installationDate + "," +
                serialNumber;
    }
}
