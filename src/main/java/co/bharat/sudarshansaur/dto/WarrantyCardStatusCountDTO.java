package co.bharat.sudarshansaur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarrantyCardStatusCountDTO {
    private String approved;
    private String  denied;
    private String pendingImageUploaded;
    private String pendingImageNotUploaded;

    @Override
    public String toString() {
        return  approved + "," +
                denied + "," +
                pendingImageUploaded + "," +
                pendingImageNotUploaded;
    }
}
