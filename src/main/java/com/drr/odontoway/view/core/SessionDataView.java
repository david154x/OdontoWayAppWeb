package com.drr.odontoway.view.core;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
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
	private String estadoSistema;
	
	@Getter @Setter
	private String nombreModulo;
	
	@PostConstruct
	private void init() {
		this.nombreUsuario = "David Rojas Rodriguez";
		this.rolDeUsuario = "Administrador";
    }

}
