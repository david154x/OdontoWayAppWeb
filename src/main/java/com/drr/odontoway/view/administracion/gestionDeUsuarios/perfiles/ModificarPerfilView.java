package com.drr.odontoway.view.administracion.gestionDeUsuarios.perfiles;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

import com.drr.odontoway.core.dto.PerfilDTO;
import com.drr.odontoway.core.service.PerfilService;
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
public class ModificarPerfilView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PerfilService perfilService;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Getter @Setter
	private List<PerfilDTO> lstResultados;
	
	@Getter @Setter
	private PerfilDTO perfilSeleccionadoParaModificar;
	
	@Inject
	@Getter @Setter
	private PerfilDTO perfilConNuevosValores; 
	
	@Getter @Setter
	private Boolean habilitarBtnModificar;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("modificar");
		
		this.consultarTodosLosPerfiles();
		this.habilitarBtnModificar = Boolean.TRUE;
		
	}
	
	public void consultarTodosLosPerfiles() {
		try {
			
			this.lstResultados = this.perfilService.consultarTodosLosPerfiles();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("dtTodosLosPerfiles");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void refrescarTodosLosPerfiles() {
		try {
			
			this.lstResultados = this.perfilService.consultarTodosLosPerfiles();
			this.jsfUtils.addMsgInfo("Se ha actualizado la informacion correctamente");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("dtTodosLosPerfiles");
			this.jsfUtils.updateMsg();
		}
	}
	
	public Boolean idenfiticarEstado(String idEstado) {
		
		if ( idEstado.equals("Activo") )
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
	
	public void cambiarEstado(PerfilDTO perfilParaModificar) {
		this.perfilService.activarODesactivarPerfil(perfilParaModificar);
	}
	
	public void abrirDialogoModificarRegistro(PerfilDTO perfilSeleccionado) {
		
		if ( !this.habilitarBtnModificar ) {
			this.habilitarBtnModificar = Boolean.TRUE;
			this.actualizarBtnModificarPerfil();
		}
		
		this.perfilConNuevosValores = new PerfilDTO();
		
		if ( this.perfilSeleccionadoParaModificar != null && !Objects.isNull(this.perfilSeleccionadoParaModificar) )
			this.perfilSeleccionadoParaModificar = new PerfilDTO();
		
		this.perfilSeleccionadoParaModificar = SerializationUtils.clone(perfilSeleccionado);
		
		this.jsfUtils.ejecutarScript("PF('wvModificarPerfil').show();");
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarPerfil:pnlDatosActuales");
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarPerfil:pnlNuevosValores");
		
	}
	
	private void actualizarBtnModificarPerfil() {
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarPerfil:btnGuardarCambios");
	}
	
	public void ocultarDialogoModificarRegistro() {
		this.jsfUtils.ejecutarScript("PF('wvModificarPerfil').hide();");
	}
	
	public void validarSiNombrePerfilModificadoYaExiste() {
		
		if ( !this.habilitarBtnModificar ) {
			this.habilitarBtnModificar = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("formPopUpModificarPerfil:pnlNuevosValores");
		}
		
		if ( this.perfilConNuevosValores.getNombrePerfil() != null && !this.perfilConNuevosValores.getNombrePerfil().isEmpty() ) {
			
			if ( this.perfilService.consultarSiPerfilXNombreExiste(this.perfilConNuevosValores.getNombrePerfil()) ) {

				this.jsfUtils.addMsgError("Ya existe un perfil con este nombre, intente con otro valor");
				this.jsfUtils.updateMsg();
				this.actualizarBtnModificarPerfil();
				return ;
				
			}
			
		}
		
		this.habilitarBtnModificar = Boolean.FALSE;
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarPerfil:btnGuardarCambios");
		this.jsfUtils.addMsgInfo("El nombre ingresado es valido, se permite continuar");
		this.jsfUtils.updateMsg();
		
	}
	
	public void guardarCambios() {
		try {
			this.perfilConNuevosValores.setIdPerfil(this.perfilSeleccionadoParaModificar.getIdPerfil());
			
			if ( this.perfilService.actualizarPerfil(this.perfilConNuevosValores) ) {
				this.consultarTodosLosPerfiles();
				this.jsfUtils.addMsgInfo("Se ha actualizado el registro correctamente");
				this.ocultarDialogoModificarRegistro();
			} else {
				this.jsfUtils.addMsgInfo("No se detectaron cambios");
				this.ocultarDialogoModificarRegistro();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	public void validarDescripcionModificada() {
		try {
			
			if ( !this.habilitarBtnModificar )
				this.habilitarBtnModificar = Boolean.TRUE;
			
			if ( this.perfilConNuevosValores.getDescripcionPerfil() == null || this.perfilConNuevosValores.getDescripcionPerfil().isEmpty() )
				return ;
			
			if ( this.perfilConNuevosValores.getDescripcionPerfil().length() < 10 ) {
				this.jsfUtils.addMsgError("La descripcion no puede ser menor a 10 caracteres");
				return ;
			}
			
			this.habilitarBtnModificar = Boolean.FALSE;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.actualizarBtnModificarPerfil();
			this.jsfUtils.updateMsg();
		}
		
	}
	
}
