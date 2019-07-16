package orh.etudiant.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import orh.etudiant.entity.Etudiant;

public interface EtudiantRepository  extends JpaRepository<Etudiant, Long>{
	public List<Etudiant> findByNom(String n);
	public Page<Etudiant> findByNom(String n ,Pageable pageable);
	@Query ("select e from Etudiant e where e.nom like:x")
	public Page<Etudiant> chercherNom (@Param("x")String mc ,Pageable pageable);

}
