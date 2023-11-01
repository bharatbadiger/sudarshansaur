package co.bharat.sudarshansaur.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "StockistDealerWarranty")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockistDealerWarranty {

	@Id
	@GeneratedValue
	private long id;

	private long stockistId;

	private long dealerId;
	
	private String warrantySerialNo;
	
	private Date createdOn;
    @PrePersist
    protected void onCreate() {
    	createdOn = new Date();
    	updatedOn = createdOn;
    }

	private Date updatedOn;
    @PreUpdate
    protected void onUpdate() {
    	updatedOn = new Date();
    }

}
