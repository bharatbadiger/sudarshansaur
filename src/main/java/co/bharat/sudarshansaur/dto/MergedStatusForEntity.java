package co.bharat.sudarshansaur.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MergedStatusForEntity {

	private List<MergedStatus> statuses;

}
