package co.bharat.sudarshansaur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.enums.AllocationStatus;

public interface WarrantyDetailsRepository extends JpaRepository<WarrantyDetails, String> {

	List<WarrantyDetails> findByInvoiceNoAndAllocationStatus(String invoiceNo, AllocationStatus allocationStatus);

	List<WarrantyDetails> findByInvoiceNo(String invoiceNo);

	List<WarrantyDetails> findByAllocationStatus(AllocationStatus allocationStatus);

	Optional<WarrantyDetails> findByWarrantySerialNo(String warrantySerialNo);

//	Optional<List<WarrantyDetails>> findByCustomerCustomerId(Long id);

//	Optional<List<WarrantyDetails>> findByDealersDealerId(Long id);
	
//	Page<WarrantyDetails> findByStockistsMobileNo(String mobileNo, Pageable pageable);
	
//	Optional<List<WarrantyDetails>> findByStockistsStockistId(Long id);

}
