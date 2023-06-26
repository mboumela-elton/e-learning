package com.msel.elearning.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MyFile {

	private String name;
	private String path;
	private String path1;
	private String modified;
	private Long size;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static String supprimerChemin(String chaine) {
	    int index = chaine.indexOf("msel_parent");
	    if (index != -1) {
	        return chaine.substring(index);
	    }
	    return chaine;
	}
	
	public static String convertirCheminWindowsEnJava(String chemin) {
	    return chemin.replace("\\", "/");
	}
	
	public MyFile(@Value("${folder.name}") File file) {
		this.name = file.getName();
		this.path =  file.getPath();
		this.path1 = convertirCheminWindowsEnJava(supprimerChemin(path));
		this.modified = (new SimpleDateFormat("yyyy-MM-dd")).format(file.lastModified());
		this.size = file.length()/1024;
	}
}
