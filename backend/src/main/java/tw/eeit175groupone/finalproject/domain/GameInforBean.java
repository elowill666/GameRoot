package tw.eeit175groupone.finalproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gameinfor")
@NoArgsConstructor
@Data
public class GameInforBean {

	@Id
	@Column(name = "gameinfor_id")
	private Integer gameInforId;
	@Column(name = "rating")
	private Double rating;
	@Column(name = "os")
	private String os;
	@Column(name = "processor")
	private String processor;
	@Column(name = "memory")
	private String memory;
	@Column(name = "graphics")
	private String graphics;
	@Column(name = "storage")
	private String storage;



}
