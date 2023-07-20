package co.bharat.sudarshansaur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.enums.AllocationStatus;

@RepositoryRestResource(collectionResourceRel = "warrantyDetails", path = "warrantyDetails")
public interface WarrantyDetailsRepository extends JpaRepository<WarrantyDetails,String>{

	List<WarrantyDetails> findByInvoiceNoAndAllocationStatus(String invoiceNo, AllocationStatus allocationStatus);

	List<WarrantyDetails> findByInvoiceNo(String invoiceNo);

	List<WarrantyDetails> findByAllocationStatus(AllocationStatus allocationStatus);

}
