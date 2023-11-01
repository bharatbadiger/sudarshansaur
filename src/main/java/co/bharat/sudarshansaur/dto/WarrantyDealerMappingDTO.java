package co.bharat.sudarshansaur.dto;

import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.entity.WarrantyRequests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyDealerMappingDTO {

	private Dealers dealer;
    private String warrantySerialNo;
    private WarrantyRequests warrantyRequests;
}
