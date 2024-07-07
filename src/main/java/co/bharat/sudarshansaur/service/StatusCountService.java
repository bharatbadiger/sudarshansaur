package co.bharat.sudarshansaur.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.sudarshansaur.dto.MergedStatus;
import co.bharat.sudarshansaur.dto.MergedStatusForEntity;
import co.bharat.sudarshansaur.dto.StatusCountDTO;

@Service
public class StatusCountService {

	@Autowired
	private EntityManager entityManager;

	public List<Object[]> getStatusCounts() {
		/*
		 * String sqlQuery =
		 * "SELECT 'customers', c.status, COUNT(1) FROM icrmdev_sudarshansaur.customers c GROUP BY c.status "
		 * +
		 * "UNION SELECT 'dealers', d.status, COUNT(1) FROM icrmdev_sudarshansaur.dealers d GROUP BY d.status "
		 * +
		 * "UNION SELECT 'stockists', s.status, COUNT(1) FROM icrmdev_sudarshansaur.stockists s GROUP BY s.status "
		 * +
		 * "UNION SELECT 'warrantyDetails', w.allocation_status as status, COUNT(1) FROM icrmdev_sudarshansaur.warranty_details w GROUP BY w.allocation_status"
		 * ;
		 */
        String sqlQuery = "SELECT 'customers' AS entity, c.status, COUNT(1) AS count FROM customers c GROUP BY c.status\r\n" + 
        		"UNION ALL\r\n" + 
        		"SELECT 'dealers' AS entity, d.status, COUNT(1) AS count FROM dealers d GROUP BY d.status\r\n" + 
        		"UNION ALL\r\n" + 
        		"SELECT 'stockists' AS entity, s.status, COUNT(1) AS count FROM stockists s GROUP BY s.status\r\n" + 
        		"UNION ALL\r\n" + 
        		"SELECT 'warrantyDetails' AS entity, w.allocation_status AS status, COUNT(1) AS count FROM warranty_details w GROUP BY w.allocation_status\r\n"+
                "UNION ALL\r\n" +
                "SELECT 'warrantyRequests' AS entity, wr.status AS status, COUNT(1) AS count FROM warranty_requests wr GROUP BY wr.status";
        return entityManager.createNativeQuery(sqlQuery).getResultList();
        /*        List<StatusCountDTO> statusCounts = new ArrayList<>();

        for (Object[] result : resultList) {
            String entity = String.valueOf(result[0]);
            String status = String.valueOf(result[1]);
			
			 * if("warrantyDetails".equalsIgnoreCase(entity)) { status =
			 * AllocationStatus.valueOf(status).name(); } else { status =
			 * UserType.valueOf(UserStatus.class,status).name(); }
			 
            Long count = ((Number) result[2]).longValue();
            statusCounts.add(new StatusCountDTO(entity, status, count));
        }

        return statusCounts;*/
    }
	
	public StatusCountDTO getMergedStatusCounts() {
        List<Object[]> statusCounts = getStatusCounts();

        Map<String, MergedStatusForEntity> mergedData = new HashMap<>();

        for (Object[] row : statusCounts) {
            String entityName = (String) row[0];
            String status = (String) row[1];
            BigInteger count = (BigInteger) row[2];

            MergedStatus mergedStatus = new MergedStatus();
            mergedStatus.setStatus(status);
            mergedStatus.setCount(count);

            MergedStatusForEntity mergedStatusForEntity = mergedData.get(entityName);
            if (mergedStatusForEntity == null) {
                mergedStatusForEntity = new MergedStatusForEntity();
                mergedStatusForEntity.setStatuses(new ArrayList<>());
                mergedData.put(entityName, mergedStatusForEntity);
            }

            mergedStatusForEntity.getStatuses().add(mergedStatus);
        }

        StatusCountDTO response = new StatusCountDTO();
        response.setData(mergedData);

        return response;
    }

    /**
     * SELECT
     * COUNT(CASE WHEN status = 0 THEN 1 END) AS count_status_0,
     * COUNT(CASE WHEN status = 4 THEN 1 END) AS count_status_4,
     * COUNT(CASE WHEN status = 5 THEN 1 END) AS count_status_5,
     * COUNT(CASE WHEN status = 0 AND img_live_system IS NOT NULL AND img_system_serial_no IS NOT NULL THEN 1 END) AS count_status_0_with_images,
     * COUNT(CASE WHEN status = 0 AND img_live_system IS NULL AND img_system_serial_no IS NULL THEN 1 END) AS count_status_0_without_images
     * FROM warranty_requests;
     *
     * @return
     */

    public List<BigInteger[]> getWarrantyRequestCount(){
        String sqlQuery = "SELECT\n" +
                "  COUNT(CASE WHEN status = 0 THEN 1 END) AS count_status_0,\n" +
                "  COUNT(CASE WHEN status = 4 THEN 1 END) AS count_status_4,\n" +
                "  COUNT(CASE WHEN status = 5 THEN 1 END) AS count_status_5,\n" +
                "  COUNT(CASE WHEN status = 0 AND img_live_system IS NOT NULL AND img_system_serial_no IS NOT NULL THEN 1 END) AS count_status_0_with_images,\n" +
                "  COUNT(CASE WHEN status = 0 AND img_live_system IS NULL AND img_system_serial_no IS NULL THEN 1 END) AS count_status_0_without_images\n" +
                "FROM warranty_requests;\n";
        List<BigInteger[]> res = entityManager.createNativeQuery(sqlQuery).getResultList();
        return res;
    }
}
