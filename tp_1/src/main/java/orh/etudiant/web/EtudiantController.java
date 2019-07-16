package orh.etudiant.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.imageio.stream.FileImageInputStream;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import orh.etudiant.entity.Etudiant;
import orh.etudiant.repository.EtudiantRepository;
/* au debut on utilise @controller 
 * puis on defint la page a partir de request maping 
 * puis on vas faire l'injection des methodes
 * puis on va crée le controller de la page d'accueil
 * @requestMaping (value="/index")
 * public String index ( Model model ,
 * @requestParam) */
@Controller
@RequestMapping(value ="/etudiant")
public class EtudiantController {
	@Autowired
private  EtudiantRepository etudiantRepository;
	@Value("${images.dir}")
private String dirImages;	
	@RequestMapping(value="/index")
	public String Index (Model model ,
			@RequestParam (name="page",defaultValue="0")  int p ,
			@RequestParam (name="motCle",defaultValue="") String mc){
		Page<Etudiant> pageEtdudiants =etudiantRepository.
				chercherNom("%"+mc+"%",new PageRequest(p , 5)); 
		int pageCount=pageEtdudiants.getTotalPages();
		int[] pages =new int [pageCount];
		for (int i=0 ;i<pageCount;i++) pages[i]=i ;
		model.addAttribute("pages",pages);
		model.addAttribute("pageEtdudiants",pageEtdudiants);
		model.addAttribute("pageCourante",p);
		model.addAttribute("motCle",mc);
		return "etudiants";
	}
	@RequestMapping(value=("/form"), method=RequestMethod.GET)
	public String formEtudiant( Model model ,@RequestParam(required=false ) Long idEtudiant) {
		if (idEtudiant!=null) {
		Optional<Etudiant> etudiant=	 this.etudiantRepository.findById(idEtudiant);
		model.addAttribute("etudiant" , etudiant);
		model.addAttribute("modif" , Boolean.TRUE);
		
		}
		else {
		model.addAttribute("etudiant",new Etudiant());
		model.addAttribute("modif", Boolean.FALSE);
		}
		return "formEtudiant";
	}
	/* pour ajouter un etudiant en utilise @valid pour les champs va etre obligatoire 
	 BindingResult en l'utilise pour tester en cas d'error
	 MultipartFile pour definir et simplifer l'upload d'un fichier 
	 *   */
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String ajoutEtudiant (@Valid Etudiant et ,
			BindingResult bindingResult ,
			@RequestParam(name="picture") MultipartFile file) throws Exception {
		if(bindingResult.hasErrors()) {
			return "formEtudiant"; 
			}
		if (!file.isEmpty()) {et.setPhoto(file.getOriginalFilename()); }
		etudiantRepository.save(et);

		if (!(file.isEmpty())) {
			
			System.out.println(file.getOriginalFilename());
			et.setPhoto(file.getOriginalFilename());
			file.transferTo(new File(System.getProperty("user.home")+"/tp1/"+file.getOriginalFilename()+ et.getId()));
		}
			
		return "redirect:/etudiant/index";
		
		
		
	}
	@RequestMapping(value="/getphoto" ,produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
public byte[] getphoto(@RequestParam(name="picture") MultipartFile file ,Long id) throws Exception {
		File f =new File(System.getProperty("user.home")+"/tp1/"+file.getOriginalFilename()+id);
	    return org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(f));
}
	/* pour upprimer un etudiant il faut premierement definir la request maping par exmeple on utilise "/supprimer"
	 * puis on vas crée une class public String Supprimer (Long id) 
	 * etudiantRepositoy.deletebyid(id
	 * return ""rederiect)*/
	@RequestMapping(value="/supprimer")
	public String Supprimer(Long id) {
		etudiantRepository.deleteById(id);
		return "redirect:/etudiant/index";
 	}
}
