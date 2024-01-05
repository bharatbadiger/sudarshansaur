package co.bharat.sudarshansaur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.bharat.sudarshansaur.entity.WarrantyRequests;
import co.bharat.sudarshansaur.enums.AllocationStatus;

public interface WarrantyRequestsRepository extends JpaRepository<WarrantyRequests,Long>{
	
	List<WarrantyRequests> findAllByOrderByCreatedOnDesc();

	@Query("SELECT wr FROM WarrantyRequests wr WHERE wr.status = ':status' order by wr.createdOn desc")
	List<WarrantyRequests> findAllWarrantyRequestsByStatus(String status);


	Optional<WarrantyRequests> findByWarrantyDetailsWarrantySerialNo(String warrantySerialNo);
	
	boolean existsByWarrantyDetailsWarrantySerialNo(String warrantySerialNo);
	
	Optional<List<WarrantyRequests>> findByCustomersCustomerId(Long customerId);
	
	Optional<List<WarrantyRequests>> findByInitiatedBy(String initiatedBy);
	
	Optional<List<WarrantyRequests>> findByCustomersCustomerIdAndStatus(long customerId, AllocationStatus status);
	
	@Query("SELECT wr FROM WarrantyRequests wr WHERE wr.customers.customerId = :customerId AND wr.status = 'PENDING'")
    List<WarrantyRequests> findPendingWarrantyRequestsByCustomerId(long customerId);
	
	/*
	 * // Define a custom query to select warrantySerialNo values
	 * 
	 * @Query("SELECT wr.warrantySerialNo FROM WarrantyRequests wr") List<String>
	 * findWarrantySerialNumbers();
	 */
	
}
