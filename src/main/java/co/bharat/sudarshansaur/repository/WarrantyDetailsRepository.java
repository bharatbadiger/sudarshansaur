package co.bharat.sudarshansaur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.enums.AllocationStatus;

public interface WarrantyDetailsRepository extends JpaRepository<WarrantyDetails,String>{

	List<WarrantyDetails> findByInvoiceNoAndAllocationStatus(String invoiceNo, AllocationStatus allocationStatus);

	List<WarrantyDetails> findByInvoiceNo(String invoiceNo);

	List<WarrantyDetails> findByAllocationStatus(AllocationStatus allocationStatus);

}
