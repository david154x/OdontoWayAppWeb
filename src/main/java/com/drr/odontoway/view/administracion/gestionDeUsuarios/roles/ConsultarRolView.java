package com.drr.odontoway.view.administracion.gestionDeUsuarios.roles;

import java.io.Serializable;

import com.drr.odontoway.view.util.JsfUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ConsultarRolView implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private String colorTipoModulo;
	
	@Getter @Setter
	private String estadoEnSistema;
	
	@Getter @Setter
	private String nombreModulo;
	
	@Getter @Setter
	private String colorFondoSegunEstadoSistema;
	
	@Getter @Setter
	private String colorTextoSegunEstadoSistema;
	
	@Getter @Setter
	private Boolean permitirFuncionalidad;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@PostConstruct
	public void init() {
		
		this.colorTipoModulo = this.jsfUtils.colorTipoModulo("consultar");
		this.estadoEnSistema = this.jsfUtils.estadoEnSistema();
		this.nombreModulo = this.jsfUtils.nombreDeModulo();
		this.colorFondoSegunEstadoSistema = this.jsfUtils.colorFondoSegunEstadoEnSistema();
		this.colorTextoSegunEstadoSistema = this.jsfUtils.colorTextoSegunEstadoEnSistema();
		this.permitirFuncionalidad = this.jsfUtils.permitirFuncionalidad();
		
	}

}
