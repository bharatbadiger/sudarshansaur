package co.bharat.sudarshansaur.service;

import co.bharat.sudarshansaur.dto.CDMReportDTO;
import co.bharat.sudarshansaur.dto.GuaranteeCardReport;
import co.bharat.sudarshansaur.dto.WarrantyRequestsDTO;
import co.bharat.sudarshansaur.entity.Address;
import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private CustomersService customersService;

    @Autowired
    private DealersService dealersService;

    @Autowired
    private StockistsService stockistsService;

    @Autowired
    private WarrantyRequestsService warrantyRequestsService;

    @Autowired
    private StockistDealerWarrantyService stockistDealerWarrantyService;

    @Autowired
    private WarrantyDetailsService warrantyDetailsService;

    public List<CDMReportDTO> cdmReport() {
        List<WarrantyRequestsDTO> warrantyRequestsList = warrantyRequestsService.getAllWarrantyRequests();
        List<CDMReportDTO> list = new ArrayList<>();
        warrantyRequestsList.forEach(request -> list.add(
                        CDMReportDTO.builder()
                                .cdmNumber("")
                                .customerName(request.getCustomers().getCustomerName())
                                .customerFullAddress(request.getInstallationAddress().toString())
                                .customerDistrict(Optional.ofNullable(request.getInstallationAddress()).map(Address::getDistrict).orElse(""))
                                .state(Optional.ofNullable(request.getInstallationAddress()).map(Address::getState).orElse(""))
                                .phone(request.getCustomers().getMobileNo())
                                .stockistFirmName(request.getWarrantyDetails().getCrmStockistName())
                                .stockistPlace(request.getWarrantyDetails().getCrmStockistDistrict())
                                .capacity(request.getWarrantyDetails().getLPD())
                                .model(request.getWarrantyDetails().getModel())
                                .invoiceNumber(request.getWarrantyDetails().getInvoiceNo())
                                .invoiceDate(DateUtil.formatDate(request.getWarrantyDetails().getInstallationDate()))
                                .quantity("1")
                                .installationDate(request.getInstallationDate())
                                .serialNumber(request.getWarrantyDetails().getWarrantySerialNo())
                                .build()
                )
        );

        return list;
    }

    public List<GuaranteeCardReport> guaranteeCardReport() {
        List<Stockists> stockistsList = stockistsService.getAllStockists();
        Map<String, Stockists> stockistsMap = stockistsList.stream()
                .collect(Collectors.toMap(Stockists::getMobileNo, stockist -> stockist));


        List<WarrantyRequestsDTO> warrantyRequestsList = warrantyRequestsService.getAllWarrantyRequests();
        List<GuaranteeCardReport> list = new ArrayList<>();
        AtomicInteger serialNumber = new AtomicInteger(1);
        warrantyRequestsList.forEach(request -> {
            list.add(GuaranteeCardReport.builder()
                    .srNo(String.valueOf(serialNumber.get()))
                    .createdDate(DateUtil.formatDate(request.getCreatedOn()))
                    .customerName(request.getCustomers().getCustomerName())
                    .phone1(request.getCustomers().getMobileNo())
                    .phone2(request.getMobile2())
                    .customerFullAddress(request.getInstallationAddress().toString())
                    .customerTaluka(Optional.ofNullable(request.getInstallationAddress()).map(Address::getTaluk).orElse(""))
                    .customerDistrict(Optional.ofNullable(request.getInstallationAddress()).map(Address::getDistrict).orElse(""))
                    .state(Optional.ofNullable(request.getInstallationAddress()).map(Address::getState).orElse(""))
                    .serialNumber(request.getWarrantyDetails().getWarrantySerialNo())
                    .itemDescription(request.getWarrantyDetails().getItemDescription())
                    .capacity(request.getWarrantyDetails().getLPD())
                    .model(request.getWarrantyDetails().getModel())
                    .invoiceNumber(request.getWarrantyDetails().getInvoiceNo())
                    .invoiceDate(DateUtil.formatDate(request.getWarrantyDetails().getInstallationDate()))
                    .guaranteePeriod(request.getWarrantyDetails().getGuaranteePeriod())
                    .installationDate(request.getInstallationDate())
                    .stockistName(request.getWarrantyDetails().getCrmStockistName())
                    .stockistDistrict(request.getWarrantyDetails().getCrmStockistDistrict())
                    .stockistCode(stockistsMap.getOrDefault(request.getWarrantyDetails().getCrmStockistMobileNo(), new Stockists()).getStockistCode())
                    .dealerName(request.getDealerInfo().getName())
                    .dealerMobile(request.getDealerInfo().getMobile())
                    .dealerPlace(request.getDealerInfo().getPlace())
                    .verificationDate(request.getVerifiedDate())
                    .verifiedBy(request.getVerifiedBy())
                    .photoStatus(request.getImages() == null || request.getImages().getImgSystemSerialNo().isEmpty() || request.getImages().getImgLiveSystem().isEmpty() ? "Pending" : "Uploaded")
                    .build());
            serialNumber.getAndIncrement();
        });
        return list;
    }
}
