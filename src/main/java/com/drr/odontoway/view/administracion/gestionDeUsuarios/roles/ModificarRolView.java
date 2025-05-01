package com.drr.odontoway.view.administracion.gestionDeUsuarios.roles;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

import com.drr.odontoway.core.dto.RolDTO;
import com.drr.odontoway.core.service.RolService;
import com.drr.odontoway.view.util.JsfUtils;
import com.drr.odontoway.view.util.base.ModulBaseView;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ModificarRolView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private RolService rolService;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Getter @Setter
	private List<RolDTO> lstResultados;
	
	@Getter @Setter
	private RolDTO rolSeleccionadoParaModificar;
	
	@Inject
	@Getter @Setter
	private RolDTO rolConNuevosValores; 
	
	@Getter @Setter
	private Boolean habilitarBtnModificar;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("modificar");
		
		this.consultarTodosLosRoles();
		this.habilitarBtnModificar = Boolean.TRUE;
		
	}
	
	public void consultarTodosLosRoles() {
		try {
			
			this.lstResultados = this.rolService.consultarTodosLosRoles();
			this.jsfUtils.addMsgInfo("Se ha actualizado la informacion correctamente");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("flModificarRol");
			this.jsfUtils.actualizarComponentePorId("dtTodosLosRoles");
			this.jsfUtils.updateMsg();
		}
	}
	
	public Boolean idenfiticarEstado(String idEstado) {
		
		if ( idEstado.equals("Activo") )
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
	
	public void cambiarEstado(RolDTO rolParaModificar) {
		this.rolService.modificarEstadoRol(rolParaModificar);
	}
	
	public void abrirDialogoModificarRegistro(RolDTO rolSeleccionado) {
		
		if ( !this.habilitarBtnModificar ) {
			this.habilitarBtnModificar = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("btnGuardarCambios");
		}
		
		this.rolConNuevosValores = new RolDTO();
		
		if ( this.rolSeleccionadoParaModificar != null && !Objects.isNull(this.rolSeleccionadoParaModificar) )
			this.rolSeleccionadoParaModificar = new RolDTO();
		
		this.rolSeleccionadoParaModificar = SerializationUtils.clone(rolSeleccionado);
		
		this.jsfUtils.ejecutarScript("PF('wvModificarRol').show();");
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarRol:pnlDatosActuales");
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarRol:pnlNuevosValoresRol");
		
	}
	
	public void ocultarDialogoModificarRegistro() {
		this.jsfUtils.ejecutarScript("PF('wvModificarRol').hide();");
	}
	
	public void validarSiNombreRolModificadoYaExiste() {
		
		if ( !this.habilitarBtnModificar ) {
			this.habilitarBtnModificar = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("formPopUpModificarRol:pnlNuevosValoresRol");
		}
		
		if ( this.rolConNuevosValores.getNombreRol() != null && !this.rolConNuevosValores.getNombreRol().isEmpty() ) {
			
			if ( this.rolService.consultarSiRolExisteXNombre(this.rolConNuevosValores.getNombreRol()) ) {

				this.jsfUtils.addMsgError("Ya existe un rol con este nombre, intente con otro valor");
				this.jsfUtils.updateMsg();
				this.jsfUtils.actualizarComponentePorId("formPopUpModificarRol:btnGuardarCambios");
				return ;
				
			}
			
		}
		
		this.habilitarBtnModificar = Boolean.FALSE;
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarRol:btnGuardarCambios");
		this.jsfUtils.addMsgInfo("El nombre ingresado es valido, se permite continuar");
		this.jsfUtils.updateMsg();
		
	}
	
	public void guardarCambios() {
		try {
			if ( !validarDatosActualizacion() )
				return ;
			
			this.rolConNuevosValores.setIdRol(this.rolSeleccionadoParaModificar.getIdRol());
			
			if ( this.rolService.actualizarRol(this.rolConNuevosValores) ) {
				this.consultarTodosLosRoles();
				this.jsfUtils.addMsgInfo("Se ha actualizado el registro correctamente");
				this.ocultarDialogoModificarRegistro();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	private Boolean validarDatosActualizacion() {
		
		if ( this.rolConNuevosValores.getNombreRol() == null || this.rolConNuevosValores.getNombreRol().isEmpty() ) {
			this.jsfUtils.addMsgError("No se ha registrado ningun nombre, no se permite actualizar");
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
}
