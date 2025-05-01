package com.drr.odontoway.view.core;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class SessionDataView implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private String nombreUsuario;
	
	@Getter @Setter
	private String rolDeUsuario;
	
	@Getter @Setter
	private String generoUsuario;
	
	@Getter @Setter
	private String estadoSistema;
	
	@Getter @Setter
	private Integer idMenu;
	
	@Getter @Setter
	private String nombreModulo;
	
	@Getter @Setter
	private String nombreClaseView;
	
	@Getter @Setter
	private Boolean saludarUsuario;
	
	@Getter @Setter
	private Boolean cambiarCredenciales;
	
}
