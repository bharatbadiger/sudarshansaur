package co.bharat.sudarshansaur.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answers {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
	private Customers customers;
	
	@ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "questionId")
	private Questions questions;
	
	/*
	 * @JsonBackReference
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "request_Id", referencedColumnName = "requestId") private
	 * WarrantyRequests warrantyRequests;
	 */	
	private String answerText; 

}
