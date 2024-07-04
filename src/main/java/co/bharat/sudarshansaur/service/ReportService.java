package co.bharat.sudarshansaur.service;

import co.bharat.sudarshansaur.dto.CDMReportDTO;
import co.bharat.sudarshansaur.dto.WarrantyDetailsDTO;
import co.bharat.sudarshansaur.dto.WarrantyRequestsDTO;
import co.bharat.sudarshansaur.entity.*;
import co.bharat.sudarshansaur.repository.CustomersRepository;
import co.bharat.sudarshansaur.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<CDMReportDTO> cdmReport(){
        System.out.println("inside service cdmReport");
        List<WarrantyRequestsDTO> warrantyRequestsList = warrantyRequestsService.getAllWarrantyRequests();
        System.out.println("Fetched " + warrantyRequestsList.size() + " items");
        List<CDMReportDTO> list = new ArrayList<>();
        warrantyRequestsList.forEach(request -> list.add(
                CDMReportDTO.builder()
                        .cdmNumber("")
                        .customerName(request.getCustomers().getCustomerName())
                        .customerFullAddress(request.getInstallationAddress().toString())
                        .customerDistrict(request.getInstallationAddress().getDistrict())
                        .state(request.getInstallationAddress().getState())
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


}
