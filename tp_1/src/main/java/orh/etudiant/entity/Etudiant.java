package orh.etudiant.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="etudiant")
public class Etudiant implements Serializable{
	@Id
	@GeneratedValue
	private Long id;
	@NotEmpty
	private String nom;
	@NotEmpty
	@Email
	private String email ;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateNaissance ;
	
	private String photo ;

	public Etudiant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Etudiant(String nom, String email, Date dateNaissance, String photo) {
		super();
		this.nom = nom;
		this.email = email;
		this.dateNaissance = dateNaissance;
		this.photo = photo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	

}
