package com.msel.elearning.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.msel.elearning.service.FolderService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FolderController {

	@Autowired
    private FolderService folderService;
	
    @Autowired
	private ResourceLoader resourceLoader;


	protected static String ouvrir = new String();
	
	protected static String path = new String();
	
	//pour les actions admin
	
	@GetMapping("/parcourirAdminouvrir")
	public String parcourirAdminOuvrir(@RequestParam("ouvrir") String ouvrir, Model model) {
		path = ouvrir;
		System.out.println(path);
		this.ouvrir = path;

		return "redirect:/parcourirAdmin";
	}
	
	
    @GetMapping("/parcourirAdminsupprimer")
    public String deleteFolder( @RequestParam("dropelement") String dropelement) {
    	try {
			folderService.deleteFolder(path + "/" + dropelement);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.ouvrir = path;
    	return "redirect:/parcourirAdmin";
    }
    
    @GetMapping("/parcourirAdminrename")
    public String renameFolderOrFile(@RequestParam("renamedelement") String renamedelement, @RequestParam("newName") String newName) {
    	folderService.renameFolder(path + File.separator + renamedelement, newName);
    	this.ouvrir = path;
    	return "redirect:/parcourirAdmin";
    }

    @GetMapping("/parcourirAdmincreate")
    public String createFolder(@RequestParam("nouveauFichier") String nouveauFichier) {
    	try {
			folderService.createFolder(path + "/" + nouveauFichier);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.ouvrir = path;
    	return "redirect:/parcourirAdmin";
    }
    
	//pour les actions enseignant
	@GetMapping("/parcourirEnsouvrir")
	public String parcourirEnsOuvrir(@RequestParam("ouvrir") String ouvrir, Model model) {
		path = ouvrir;
		
		this.ouvrir = path;

		return "redirect:/parcourirEns";
	}
	
    @GetMapping("/parcourirEnsrename")
    public String renameFile(@RequestParam("renamedelement") String renamedelement, @RequestParam("newName") String newName) {
    	folderService.renameFolder(path + File.separator + renamedelement, newName);
    	this.ouvrir = path;
    	return "redirect:/parcourirEns";
    }

    @GetMapping("/parcourirEnssupprimer")
    public String deleteFile( @RequestParam("dropelement") String dropelement) {
    	try {
			folderService.deleteFolder(path + "/" + dropelement);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.ouvrir = path;
    	return "redirect:/parcourirEns";
    }

    //pour les actions abonne
	@GetMapping("/parcourirAbonneouvrir")
	public String parcourirAbonneOuvrir(@RequestParam("ouvrir") String ouvrir, Model model) {
		path = ouvrir;
		
		this.ouvrir = path;

		return "redirect:/parcourirAbonne";
	}
	
	//pour les actions communes
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file) {
		folderService.FileUploader(file, path);
		this.ouvrir = path;
		return "redirect:/parcourirAdmin";
	}
	
//    @GetMapping("/openFile")
//    public ResponseEntity<Resource> openFile(@RequestParam("filename") String filename, @RequestParam("contentType") String contentType, Model model) throws MalformedURLException {
//        Path file = Paths.get(FolderService.rootFolderPath + path + File.separator + filename);
//        Resource resource = new UrlResource(file.toUri());
//
//        model.addAttribute("filenom", filename);
//        if (resource.exists()) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);
//            headers.setContentType(MediaType.parseMediaType(contentType));
//
//            try {
//                return ResponseEntity.ok()
//                        .headers(headers)
//                        .contentLength(resource.contentLength())
//                        .body(resource);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }
    
	 @GetMapping("/pdf/{relativePath:.+}")
	 public void getPdf(@PathVariable String relativePath, HttpServletResponse response) throws IOException {
	     Resource pdfResource = resourceLoader.getResource("classpath:static/msel_parent"+ path + File.separator + relativePath);
	     response.setContentType(MediaType.APPLICATION_PDF_VALUE);
	     try (InputStream inputStream = pdfResource.getInputStream();
	          OutputStream outputStream = response.getOutputStream()) {
	         byte[] buffer = new byte[4096];
	         int bytesRead = -1;
	         while ((bytesRead = inputStream.read(buffer)) != -1) {
	             outputStream.write(buffer, 0, bytesRead);
	         }
	     }
	 }
	
}
