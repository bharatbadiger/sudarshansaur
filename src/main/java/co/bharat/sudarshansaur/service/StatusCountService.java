package co.bharat.sudarshansaur.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.sudarshansaur.dto.StatusCountDTO;
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.enums.UserType;

@Service
public class StatusCountService {

	@Autowired
	private EntityManager entityManager;

	public List<StatusCountDTO> getStatusCounts() {
        String sqlQuery = "SELECT 'customers', c.status, COUNT(1) FROM icrmdev_sudarshansaur.customers c GROUP BY c.status " +
                          "UNION SELECT 'dealers', d.status, COUNT(1) FROM icrmdev_sudarshansaur.dealers d GROUP BY d.status " +
                          "UNION SELECT 'stockists', s.status, COUNT(1) FROM icrmdev_sudarshansaur.stockists s GROUP BY s.status " +
                          "UNION SELECT 'warrantyDetails', w.allocation_status as status, COUNT(1) FROM icrmdev_sudarshansaur.warranty_details w GROUP BY w.allocation_status";

        List<Object[]> resultList = entityManager.createNativeQuery(sqlQuery).getResultList();
        List<StatusCountDTO> statusCounts = new ArrayList<>();

        for (Object[] result : resultList) {
            String entity = String.valueOf(result[0]);
            String status = String.valueOf(result[1]);
			/*
			 * if("warrantyDetails".equalsIgnoreCase(entity)) { status =
			 * AllocationStatus.valueOf(status).name(); } else { status =
			 * UserType.valueOf(UserStatus.class,status).name(); }
			 */
            Long count = ((Number) result[2]).longValue();
            statusCounts.add(new StatusCountDTO(entity, status, count));
        }

        return statusCounts;
    }
}
