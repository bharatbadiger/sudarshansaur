package co.bharat.sudarshansaur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.StockistDealerWarranty;

public interface StockistDealerWarrantyRepository extends JpaRepository<StockistDealerWarranty,Long>{
	
	List<StockistDealerWarranty> findAllByOrderByCreatedOnDesc();
	
	Optional<List<StockistDealerWarranty>> findByWarrantySerialNo(String warrantySerialNo);
	
	Optional<List<StockistDealerWarranty>> findByStockistId(long stockistId);
	
	Optional<List<StockistDealerWarranty>> findByDealerId(long dealerId);
	
	boolean existsByWarrantySerialNo(String warrantySerialNo);
	
}
