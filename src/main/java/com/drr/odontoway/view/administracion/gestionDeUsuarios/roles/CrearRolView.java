package com.drr.odontoway.view.administracion.gestionDeUsuarios.roles;

import com.drr.odontoway.core.service.RolService;
import com.drr.odontoway.view.util.JsfUtils;
import com.drr.odontoway.view.util.base.ModulBaseView;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class CrearRolView extends ModulBaseView {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private RolService rolService;
	
	@Getter @Setter
	private String nombreRol;
	
	@Getter @Setter
	private String descripcionRol;
	
	@Getter @Setter
	private Boolean deshabilitarBtnCrear; 
	
	@PostConstruct
	public void init() {
		super.initModulBase("crear");
		this.deshabilitarBtnCrear = Boolean.TRUE;
	}
	
	@PreDestroy
	private void destroy() {
		this.limpiarDatos();
	}
	
	public void validarSiYaExisteRolXNombre() {
		try {
			
			if ( !this.deshabilitarBtnCrear )
				this.deshabilitarBtnCrear = Boolean.TRUE;
			
			if ( this.rolService.consultarSiRolExisteXNombre(this.nombreRol.trim()) ) {
				this.jsfUtils.addMsgError("Ya existe un Rol con el nombre: "+this.nombreRol+" intente con otro valor");
				
				this.limpiarDatos();
				return ;
			}
			
			this.jsfUtils.addMsgInfo("Este nombre es valido, se permite continuar con la creacion");
			this.deshabilitarBtnCrear = Boolean.FALSE;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
			this.jsfUtils.actualizarComponentePorId("btnCrearRol");	
		}
	}
	
	public void guardarRol() {
		
		if ( !this.validarDatosIngresados() ) {
			this.jsfUtils.updateMsg();
			return ;
		}
		
		if ( this.rolService.crearNuevoRol(this.nombreRol, this.descripcionRol, this.jsfUtils.nombreUsuarioLogeado()) ) {
			this.jsfUtils.addMsgInfo("Se ha creado el Rol: "+this.nombreRol+" satisfactoriamente");
			this.jsfUtils.updateMsg();
			this.limpiarDatos();
		}
		
	}
	
	private Boolean validarDatosIngresados() {
		
		if ( this.nombreRol == null || this.nombreRol.isEmpty() ) {
			this.jsfUtils.addMsgError("El campo nombre de rol no puede estar vacio");
			return Boolean.FALSE;
		}
		
		if ( this.nombreRol.length() < 5 ) {
			this.jsfUtils.addMsgError("El nombre del rol no pueden tener menos de 5 caracteres");
			return Boolean.FALSE;
		}
		
		if ( this.descripcionRol == null ) {
			this.jsfUtils.addMsgError("El campo descripcion rol no puede estar vacio");
			return Boolean.FALSE;
		}
		
		if ( this.descripcionRol.length() < 15 ) {
			this.jsfUtils.addMsgError("La descripcion del rol, no puede tener menos de 15 caracteres");
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	private void limpiarDatos() {
		
		if ( this.nombreRol != null && !this.nombreRol.isEmpty() )
			this.nombreRol = null;
		
		if ( this.descripcionRol != null && !this.descripcionRol.isEmpty() )
			this.descripcionRol = null;
		
		this.jsfUtils.actualizarComponentePorId("flCrearRol");
		
	}
	
}
