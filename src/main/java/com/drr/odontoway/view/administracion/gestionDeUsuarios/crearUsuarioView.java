package com.drr.odontoway.view.administracion.gestionDeUsuarios;

import java.io.Serializable;

import com.drr.odontoway.view.util.JsfUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class crearUsuarioView implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@PostConstruct
	public void init() {
		System.out.println("Vista de usuario activa");
	}
	
	public void mensajeFijo() {
		this.jsfUtils.addMsgInfo("MENSAJE FIJO");
		jsfUtils.updateMsg();
		
	}
	
	public void mensajeTemporal() {
		this.jsfUtils.addMsgInfo("MENSAJE TEMPORAL");
		this.jsfUtils.actualizarComponentePorId("mensajeUsuario");
	}

}
