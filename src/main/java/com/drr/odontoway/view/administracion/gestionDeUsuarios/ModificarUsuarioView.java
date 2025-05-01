package com.drr.odontoway.view.administracion.gestionDeUsuarios;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.validator.EmailValidator;

import com.drr.odontoway.core.dto.CiudadDTO;
import com.drr.odontoway.core.dto.PaisDTO;
import com.drr.odontoway.core.dto.RolDTO;
import com.drr.odontoway.core.dto.UsuarioDTO;
import com.drr.odontoway.core.service.CiudadService;
import com.drr.odontoway.core.service.PaisService;
import com.drr.odontoway.core.service.RolService;
import com.drr.odontoway.core.service.UsuarioService;
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
public class ModificarUsuarioView extends ModulBaseView {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private PaisService paisService;
	
	@Inject
	private CiudadService ciudadService;
	
	@Inject
	private RolService rolService;
	
	@Getter @Setter
	private UsuarioDTO usuarioSeleccionadoParaModificar;
	
	@Getter @Setter
	private UsuarioDTO usuarioParaModificar;
	
	@Getter @Setter
	private List<UsuarioDTO> lstUsuariosActuales;
	
	@Getter @Setter
	private List<PaisDTO> lstPaisesDisponibles;
	
	@Getter @Setter
	private List<CiudadDTO> lstCiudadesDisponibles;
	
	@Getter @Setter
	private List<RolDTO> lstRolesDisponibles;
	
	private Boolean sinDetectarCambios;
	
	@Getter @Setter
	private Boolean deshabilitarBtnModificar;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("modificar");
		
		this.inicializarUsuarioParaModificar();
		this.consultarTodasLosUsuarios();
		this.cargarPaises();
		this.cargarRoles();
		
		this.usuarioSeleccionadoParaModificar = new UsuarioDTO();
		this.usuarioSeleccionadoParaModificar.setRolAsignado(new RolDTO());
		this.usuarioSeleccionadoParaModificar.setPaisAsignado(new PaisDTO());
		this.usuarioSeleccionadoParaModificar.setCiudadAsignada(new CiudadDTO());
		this.usuarioSeleccionadoParaModificar.setGenero("O");
		
		this.sinDetectarCambios = Boolean.FALSE;
		this.deshabilitarBtnModificar = Boolean.FALSE;
		
	}
	
	public void consultarTodasLosUsuarios() {
		try {
			
			this.lstUsuariosActuales = this.usuarioService.consultarTodosLosUsuario();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("dtTodosLosUsuarios");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void refrescarTodasLosUsuarios() {
		try {
			
			this.lstUsuariosActuales = this.usuarioService.consultarTodosLosUsuario();
			
			this.jsfUtils.addMsgInfo("Se ha refrescado la informacion correctamente");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("dtTodosLosUsuarios");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void cambiarEstado(UsuarioDTO usuarioDTO) {
		this.usuarioService.activarODesactivarUsuario(usuarioDTO);
	}
	
	public void abrirDlgModificarUsuario(UsuarioDTO usuarioSeleccionado) {
		try {
			
			this.sinDetectarCambios = Boolean.FALSE;
			// Este sera el DTO que se utilizara para mostrar lo actual y se podra modificar sobre el mismo
			this.usuarioSeleccionadoParaModificar = SerializationUtils.clone(usuarioSeleccionado);
			
			// Este nos permitira saber su informacion anterior al cambio
			this.usuarioParaModificar = SerializationUtils.clone(this.usuarioService.consultarUsuarioXId(
					this.usuarioSeleccionadoParaModificar.getIdUsuario()));
			
			this.cargarCiudadesXPais();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("formPopUpModificarUsuario:flDatosActuales");
			this.jsfUtils.ejecutarScript("PF('wvModificarUsuario').show();");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void validarEmailValido() {
		
		if ( !EmailValidator.getInstance().isValid(this.usuarioSeleccionadoParaModificar.getEmail())) {
			jsfUtils.addMsgError("El email registrado no es valido, intente otra combinacion");
			this.deshabilitarBtnModificar = Boolean.TRUE;
		} else {
			jsfUtils.addMsgInfo("El email registrado es valido");
			if ( !this.deshabilitarBtnModificar )
				this.deshabilitarBtnModificar = Boolean.FALSE;
		}

		this.jsfUtils.actualizarComponentePorId("emailUsuarioNuevo");
		this.actualizarBotonModificarUsuario();
		this.jsfUtils.updateMsg();
		
	}
	
	private void inicializarUsuarioParaModificar() {
		
		this.usuarioParaModificar = new UsuarioDTO();
		this.usuarioParaModificar.setRolAsignado(new RolDTO());
		this.usuarioParaModificar.setPaisAsignado(new PaisDTO());
		this.usuarioParaModificar.setCiudadAsignada(new CiudadDTO());
		
	}
	
	private void cargarPaises() {
		this.lstPaisesDisponibles = this.paisService.consultarTodosLosPaises();
	}
	
	public void cargarCiudadesXPais() {
		try {
			
			if ( this.deshabilitarBtnModificar )
				this.deshabilitarBtnModificar = Boolean.FALSE;
			
			this.lstCiudadesDisponibles = this.ciudadService.consultarCiudadesXPais(this.usuarioSeleccionadoParaModificar.getPaisAsignado().getIdPais());
			
			if ( this.lstCiudadesDisponibles == null || this.lstCiudadesDisponibles.isEmpty() ) {
				this.jsfUtils.addMsgError("No existen ciudades parametrizadas para el pais seleccionado, porfavor intente con un pais diferente");
				this.deshabilitarBtnModificar = Boolean.TRUE;
				this.jsfUtils.updateMsg();
			}
			
		} finally {
			this.actualizarBotonModificarUsuario();
			this.jsfUtils.actualizarComponentePorId("ciudadNacimiento");
		}
	}
	
	private void cargarRoles() {
		this.lstRolesDisponibles = this.rolService.consultarTodosLosRoles();
	}
	
	public Date fechaMaximaNacimiento() {
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.YEAR, -18);
	    return cal.getTime();
	}
	
	public void actualizarInformacionUsuario() {
		try {
			
			if ( this.usuarioParaModificar != null && !Objects.isNull(this.usuarioParaModificar) ) {
				
				this.asignarCambiosAlUsuario();
				
				if ( !this.sinDetectarCambios ) {
					this.jsfUtils.addMsgInfo("No se han detectado cambios, se mantiene la informacion actual");
					this.ocultarDlgModificarUsuario();
					return ;
				}
				
				if ( this.usuarioService.actualizarInformacionUsuario(this.usuarioParaModificar) ) {
					this.jsfUtils.addMsgInfo("Se ha actualizado la informacion correctamente");
					this.consultarTodasLosUsuarios();
					this.ocultarDlgModificarUsuario();
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	private void asignarCambiosAlUsuario() {
		try {
			
			if ( this.usuarioParaModificar.getRolAsignado() != null
					&& !this.usuarioParaModificar.getRolAsignado().getIdRol().equals(
							this.usuarioSeleccionadoParaModificar.getRolAsignado().getIdRol()) ) {
				this.usuarioParaModificar.setRolAsignado(this.usuarioSeleccionadoParaModificar.getRolAsignado());
				this.sinDetectarCambios = Boolean.TRUE;
			}
			
			if ( this.usuarioSeleccionadoParaModificar.getDireccion() != null
					&& !this.usuarioParaModificar.getDireccion().equals(this.usuarioSeleccionadoParaModificar.getDireccion()) ) {
				this.usuarioParaModificar.setDireccion(this.usuarioSeleccionadoParaModificar.getDireccion());
				this.sinDetectarCambios = Boolean.TRUE;
			}
			
			if ( this.usuarioSeleccionadoParaModificar.getTelefono() != null
					&& !this.usuarioParaModificar.getTelefono().equals(
							this.usuarioSeleccionadoParaModificar.getTelefono()) ) {
				this.usuarioParaModificar.setTelefono(this.usuarioSeleccionadoParaModificar.getTelefono());
				this.sinDetectarCambios = Boolean.TRUE;
			}
			
			if ( this.usuarioSeleccionadoParaModificar.getEmail() != null
					&& !this.usuarioParaModificar.getEmail().equals(
							this.usuarioSeleccionadoParaModificar.getEmail()) ) {
				this.usuarioParaModificar.setEmail(this.usuarioSeleccionadoParaModificar.getEmail());
				this.sinDetectarCambios = Boolean.TRUE;
			}
			
			if ( this.usuarioSeleccionadoParaModificar.getPaisAsignado() != null
					&& !this.usuarioSeleccionadoParaModificar.getPaisAsignado().getIdPais().equals(
							this.usuarioParaModificar.getPaisAsignado().getIdPais()) ) {
				this.usuarioParaModificar.setPaisAsignado(this.usuarioSeleccionadoParaModificar.getPaisAsignado());
				this.sinDetectarCambios = Boolean.TRUE;
			}
			
			if ( this.usuarioSeleccionadoParaModificar.getCiudadAsignada() != null ) {
				
				if ( !this.usuarioSeleccionadoParaModificar.getCiudadAsignada().getIdCiudad().equals(
							this.usuarioParaModificar.getCiudadAsignada().getIdCiudad()) ) {
					this.usuarioParaModificar.setCiudadAsignada(this.usuarioSeleccionadoParaModificar.getCiudadAsignada());
					this.sinDetectarCambios = Boolean.TRUE;
				}
				
			}
			
			if ( this.usuarioSeleccionadoParaModificar.getGenero() != null
					&& !this.usuarioParaModificar.getGenero().equals(
							this.usuarioSeleccionadoParaModificar.getGenero()) ) {
				this.usuarioParaModificar.setGenero(this.usuarioSeleccionadoParaModificar.getGenero());
				this.sinDetectarCambios = Boolean.TRUE;
			}
			
			if ( this.usuarioSeleccionadoParaModificar.getFechaNacimiento() != null
					&& !this.usuarioParaModificar.getFechaNacimiento().equals(
							this.usuarioSeleccionadoParaModificar.getFechaNacimiento()) ) {
				this.usuarioParaModificar.setFechaNacimiento(this.usuarioSeleccionadoParaModificar.getFechaNacimiento());
				this.sinDetectarCambios = Boolean.TRUE;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void ocultarDlgModificarUsuario() {
		this.jsfUtils.ejecutarScript("PF('wvModificarUsuario').hide();");
	}
	
	private void actualizarBotonModificarUsuario() {
		this.jsfUtils.actualizarComponentePorId("btnGuardarCambios");
	}
	
}
