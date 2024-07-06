package co.bharat.sudarshansaur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuaranteeCardReport {
    private String srNo;
    private String createdDate;
    private String customerName;
    private String phone1;
    private String phone2;
    private String customerFullAddress;
    private String customerTaluka;
    private String customerDistrict;
    private String state;
    private String serialNumber;
    private String itemDescription;
    private String capacity;
    private String model;
    private String invoiceDate;
    private String invoiceNumber;
    private String guaranteePeriod;
    private String installationDate;
    private String stockistCode;
    private String stockistName;
    private String stockistDistrict;
    private String dealerName;
    private String dealerMobile;
    private String dealerPlace;
    private String verificationDate;
    private String verifiedBy;
    private String photoStatus;

    @Override
    public String toString() {
        return srNo + ", " +
                createdDate + ", " +
                customerName + ", " +
                phone1 + ", " +
                phone2 + ", " +
                customerFullAddress + ", " +
                customerTaluka + ", " +
                customerDistrict + ", " +
                state + ", " +
                serialNumber + ", " +
                itemDescription + ", " +
                capacity + ", " +
                model + ", " +
                invoiceDate + ", " +
                invoiceNumber + ", " +
                guaranteePeriod + ", " +
                installationDate + ", " +
                stockistCode + ", " +
                stockistName + ", " +
                stockistDistrict + ", " +
                dealerName + ", " +
                dealerMobile + ", " +
                dealerPlace + ", " +
                verificationDate + ", " +
                verifiedBy + ", " +
                photoStatus;
    }
}
