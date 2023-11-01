package co.bharat.sudarshansaur.dto;

import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.entity.WarrantyRequests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyStockistMappingDTO {

	private Stockists stockists;
    private String warrantySerialNo;
    private WarrantyRequests warrantyRequests;
}
