package co.bharat.sudarshansaur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.WarrantyRequests;

public interface WarrantyRequestsRepository extends JpaRepository<WarrantyRequests,Long>{
	
	List<WarrantyRequests> findAllByOrderByCreatedOnDesc();
	
	Optional<List<WarrantyRequests>> findByCustomersCustomerId(Long customerId);
	
	Optional<List<WarrantyRequests>> findByDealersDealerId(Long dealerId);

}
