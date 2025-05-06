package com.drr.odontoway.view.administracion.gestionDeUsuarios;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
public class CrearUsuarioView extends ModulBaseView {

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
	private UsuarioDTO usuarioParaCrear;
	
	@Getter @Setter
	private List<PaisDTO> lstPaisesDisponibles;
	
	@Getter @Setter
	private List<CiudadDTO> lstCiudadesDisponibles;
	
	@Getter @Setter
	private List<RolDTO> lstRolesDisponibles;
	
	@Getter @Setter
	private Boolean deshabilitarSeleccionCiudad;
	
	@Getter @Setter
	private Boolean verDatosUsuario;
	
	@Getter @Setter
	private Boolean deshabilitarBtnCrear;
	
	@Getter @Setter
	private Boolean deshabilitarNombresYApellidos;
	
	@Getter @Setter
	private Boolean numeroDocumentoPermitido;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("crear");
		
		this.inicializarUsuarioParaCrear();
		
		this.cargarPaises();
		this.cargarRoles();
		
		this.numeroDocumentoPermitido = Boolean.TRUE;
		
		this.deshabilitarSeleccionCiudad = Boolean.TRUE;
		this.verDatosUsuario = Boolean.FALSE;
		this.deshabilitarBtnCrear = Boolean.TRUE;
		this.deshabilitarNombresYApellidos = Boolean.FALSE;
		
		
	}
	
	private void inicializarUsuarioParaCrear() {
		
		this.usuarioParaCrear = new UsuarioDTO();
		this.usuarioParaCrear.setRolAsignado(new RolDTO());
		this.usuarioParaCrear.setPaisAsignado(new PaisDTO());
		this.usuarioParaCrear.setCiudadAsignada(new CiudadDTO());
		
	}
	
	private void cargarPaises() {
		this.lstPaisesDisponibles = this.paisService.consultarTodosLosPaises();
	}
	
	private void cargarRoles() {
		this.lstRolesDisponibles = this.rolService.consultarTodosLosRoles();
	}
	
	public void cargarCiudadesXPais() {
		try {
			
			if ( this.usuarioParaCrear.getCiudadAsignada().getIdCiudad() != null )
				this.usuarioParaCrear.getCiudadAsignada().setIdCiudad(null);
				
			if ( !this.deshabilitarSeleccionCiudad )
				this.deshabilitarSeleccionCiudad = Boolean.TRUE;
			
			if ( this.usuarioParaCrear.getPaisAsignado().getIdPais() == null)
				return ;
			
			this.deshabilitarSeleccionCiudad = Boolean.FALSE;
			this.lstCiudadesDisponibles = this.ciudadService.consultarCiudadesXPais(this.usuarioParaCrear.getPaisAsignado().getIdPais());
			
		} finally {
			this.jsfUtils.actualizarComponentePorId("ciudadNacimiento");
		}
	}
	
	public Date fechaMaximaNacimiento() {
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.YEAR, -18);
	    return cal.getTime();
	}
	
	public void confirmarDatosCompletos() {
		try {
			
			if ( this.verDatosUsuario )
				this.verDatosUsuario = Boolean.FALSE;
			
			if ( this.usuarioParaCrear.getGenero() == null || this.usuarioParaCrear.getGenero().isEmpty() ) {
				this.deshabilitarNombresYApellidos = Boolean.FALSE;
				this.jsfUtils.actualizarComponentePorId("pnlDatosPersonales");
				return ;
			}
			
			if ( !this.validarDatosObligatorios() ) {
				this.usuarioParaCrear.setGenero(null);
				this.jsfUtils.actualizarComponentePorId("generoUsuario");
				return ;
			}
			
			this.usuarioParaCrear.setNombreUsuario(this.usuarioService.nombreUsuarioAsignado(
					this.usuarioParaCrear.getPrimerNombre(), this.usuarioParaCrear.getSegundoNombre(),
					this.usuarioParaCrear.getPrimerApellido(), this.usuarioParaCrear.getSegundoApellido()));
			
			this.usuarioParaCrear.setPassUsuario(this.jsfUtils.claveUsuarioGenerica());
			
			this.deshabilitarNombresYApellidos = Boolean.TRUE;
			
			this.verDatosUsuario = Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("pnlDatosPersonales");
			this.jsfUtils.actualizarComponentePorId("flCrearUsuario");
			this.jsfUtils.updateMsg();
		}
	}
	
	private Boolean validarDatosObligatorios() {
		try {
			
			if ( !StringUtils.isNotBlank(this.usuarioParaCrear.getPrimerNombre()) ) {
				this.jsfUtils.addMsgError("El primer nombre no puede estar vacio");
				return Boolean.FALSE;
			}
			
			if ( !StringUtils.isNotBlank(this.usuarioParaCrear.getPrimerApellido()) ) {
				this.jsfUtils.addMsgError("El primer apellido no puede estar vacio");
				return Boolean.FALSE;
			}
			
			if ( this.usuarioParaCrear.getPaisAsignado().getIdPais() == null ) {
				this.jsfUtils.addMsgError("No se ha seleccionado ningun pais");
				return Boolean.FALSE;
			}
			
			if ( this.usuarioParaCrear.getCiudadAsignada().getIdCiudad() == null ) {
				this.jsfUtils.addMsgError("No se ha seleccionado ninguna ciudad");
				return Boolean.FALSE;
			}
			
			if ( !StringUtils.isNotBlank(this.usuarioParaCrear.getDireccion()) ) {
				this.jsfUtils.addMsgError("La direccion no tiene ningun valor asignado");
				return Boolean.FALSE;
			}
			
			if ( !StringUtils.isNotBlank(this.usuarioParaCrear.getTelefono()) ) {
				this.jsfUtils.addMsgError("El telefono no tiene ningun valor asignado");
				return Boolean.FALSE;
			}
			
			if ( !StringUtils.isNotBlank(this.usuarioParaCrear.getDireccion()) ) {
				this.jsfUtils.addMsgError("El email no tiene ningun valor asignado");
				return Boolean.FALSE;
			}
			
			if ( !StringUtils.isNotBlank(String.valueOf(this.usuarioParaCrear.getFechaNacimiento())) ) {
				this.jsfUtils.addMsgError("No se ha seleccionado ninguna fecha de nacimiento");
				return Boolean.FALSE;
			}
			
			if ( !StringUtils.isNotBlank(this.usuarioParaCrear.getGenero()) ) {
				this.jsfUtils.addMsgError("No se ha seleccionado ningun genero");
				return Boolean.FALSE;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.TRUE;
	}
	
	@SuppressWarnings("deprecation")
	public void validarEmailValido() {
		
		if ( !EmailValidator.getInstance().isValid(this.usuarioParaCrear.getEmail())) {
			jsfUtils.addMsgError("El email registrado no es valido, intente otra combinacion");
			this.usuarioParaCrear.setEmail(null);
			this.jsfUtils.actualizarComponentePorId("emailUsuarioNuevo");
		} else {
			jsfUtils.addMsgInfo("El email registrado es valido");
		}
		
		this.jsfUtils.updateMsg();
		
	}
	
	public void rolSeleccionado() {
		try {
			
			if ( !this.deshabilitarBtnCrear )
				this.deshabilitarBtnCrear = Boolean.TRUE;
			
			if ( this.usuarioParaCrear.getRolAsignado().getIdRol() == null )
				return ;
			
			this.jsfUtils.addMsgInfo("Todos los datos son correctos, se permite la creacion del usuario");
			this.deshabilitarBtnCrear = Boolean.FALSE;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("btnCrearUsuario");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void crearUsuario() {
		try {
			
			if ( this.usuarioService.crearUsuario(this.usuarioParaCrear, this.jsfUtils.nombreUsuarioLogeado()) ) {
				this.jsfUtils.addMsgInfo("Se ha creado el usuario correctamente");
				this.inicializarUsuarioParaCrear();
				this.verDatosUsuario = Boolean.FALSE;
				this.deshabilitarBtnCrear = Boolean.TRUE;
				this.jsfUtils.actualizarComponentePorId("flCrearUsuario");
			}
			
		} catch (Exception e) {
			this.jsfUtils.addMsgError("Ha ocurrido un erro en la creacion del usuario: "+e.getMessage());
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	public void validarNumeroDocumento() {
		try {
			
			if ( this.usuarioParaCrear.getNumeroDocumentoUsuario() == null
					|| this.usuarioParaCrear.getNumeroDocumentoUsuario().isEmpty() )
				return ;
			
			if ( !this.numeroDocumentoPermitido )
				this.numeroDocumentoPermitido = Boolean.TRUE;
			
			if ( this.usuarioService.validarNumeroDocumentoExistente(this.usuarioParaCrear.getNumeroDocumentoUsuario()) ) {
				this.jsfUtils.addMsgError("El numero de documento ingresado ya existe, intente con otro valor");
				this.inicializarUsuarioParaCrear();
				return ;
			}
			
			this.jsfUtils.addMsgInfo("El numero de documento es valido, se permite continuar con la creacion");
			this.numeroDocumentoPermitido = Boolean.FALSE;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("pnlDatosPersonales");
			this.jsfUtils.updateMsg();
		}
	}
	
	public Boolean deshabilitarNombresYApellidosUsuario() {
		
		if ( this.numeroDocumentoPermitido || this.deshabilitarNombresYApellidos )
			return Boolean.TRUE;
		
		return Boolean.FALSE;
		
	}
	
}
