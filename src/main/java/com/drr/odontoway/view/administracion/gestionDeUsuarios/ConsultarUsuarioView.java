package com.drr.odontoway.view.administracion.gestionDeUsuarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;

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
public class ConsultarUsuarioView extends ModulBaseView {
	
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
	private UsuarioDTO consultaFiltroUsuario;
	
	@Getter @Setter
	private List<String> lstTiposDefiltro;
	
	@Getter @Setter
	private List<String> filtroSeleccionado;
	
	@Getter @Setter
	private List<RolDTO> lstRolesActuales;
	
	@Getter @Setter
	private List<PaisDTO> lstPaisesActuales;
	
	@Getter @Setter
	private List<CiudadDTO> lstCiudadesActuales;
	
	@Getter @Setter
	private Boolean verRol;
	
	@Getter @Setter
	private Boolean verPais;
	
	@Getter @Setter
	private Boolean verCiudad;
	
	@Getter @Setter
	private Boolean verNombre;
	
	@Getter @Setter
	private Boolean verEstado;
	
	@Getter @Setter
	private Boolean verFecha;
	
	@Getter @Setter
	private List<UsuarioDTO> lstResultadoFiltro;
	
	@Getter @Setter
	private Boolean verResultados;
	
	@Getter @Setter
	private Boolean verBotonConsultarTodo;
	
	@Getter @Setter
	private Boolean deshabilitarSeleccionCiudad;
	
	@Getter @Setter
	private UsuarioDTO usuarioSeleccionado;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("consultar");
		
		this.verRol = Boolean.FALSE;
		this.verPais = Boolean.FALSE;
		this.verCiudad = Boolean.FALSE;
		this.verNombre = Boolean.FALSE;
		this.verEstado = Boolean.FALSE;
		this.verFecha = Boolean.FALSE;
		this.verResultados = Boolean.FALSE;
		this.verBotonConsultarTodo = Boolean.FALSE;
		this.deshabilitarSeleccionCiudad = Boolean.TRUE;
		
		this.inicializarFiltrUsuario();
		
		this.cargarTiposDeFiltro();
		this.cargarRoles();
		this.cargarPaies();
		this.cargarCiudades();
		
		this.verBtnConsultarTodo();
		
		this.filtroSeleccionado = new ArrayList<>();
		
	}
	
	private void cargarTiposDeFiltro() {
		this.lstTiposDefiltro = Arrays.asList("Rol", "Pais", "Ciudad","Nombre", "Estado", "Fecha");
	}
	
	private void cargarRoles() {
		this.lstRolesActuales = this.rolService.consultarTodosLosRoles();
	}
	
	private void cargarPaies() {
		this.lstPaisesActuales = this.paisService.consultarTodosLosPaises();
	}
	
	private void cargarCiudades() {
		this.lstCiudadesActuales = this.ciudadService.consultarTodasLasCiudades();
	}
	
	public void onToggleSelect() {
		
		if ( this.verResultados ) {
			this.verResultados = Boolean.FALSE;
			this.lstResultadoFiltro.clear();
			this.jsfUtils.actualizarComponentePorId("flConsultarUsuario");
		}
		
		this.cargarRoles();
		this.cargarPaies();
		this.cargarCiudades();
		
		this.inicializarFiltrUsuario();
		this.asignarFiltro();
		
    }
	
	public void asignarFiltro() {
		
		if ( !this.deshabilitarSeleccionCiudad )
			this.deshabilitarSeleccionCiudad = Boolean.TRUE;
		
		this.verRol = this.filtroSeleccionado.contains("Rol");
		
		if ( this.filtroSeleccionado.contains("Pais") ) {
			
			if ( this.consultaFiltroUsuario.getPaisAsignado().getIdPais() != null )
				this.consultaFiltroUsuario.getPaisAsignado().setIdPais(null);
			
			if ( this.verCiudad )
				this.verCiudad = Boolean.FALSE;
			
			this.verPais = Boolean.TRUE;
			
		} else {
			this.verPais = Boolean.FALSE;
		}
		
		if ( this.filtroSeleccionado.contains("Ciudad") ) {
			
			if ( !this.verPais )
				this.verPais = Boolean.TRUE;
			
			this.verCiudad = Boolean.TRUE;
			
		} else {
			this.verCiudad = Boolean.FALSE;
		}
		
		this.verNombre = this.filtroSeleccionado.contains("Nombre");
		this.verEstado = this.filtroSeleccionado.contains("Estado");
		this.verFecha = this.filtroSeleccionado.contains("Fecha");
		
		this.verBtnConsultarTodo();
		this.actualizarPanelConsultarUsuario();
		
	}
	
	private void verBtnConsultarTodo() {
		
		if ( this.verBotonConsultarTodo )
			this.verBotonConsultarTodo = Boolean.FALSE;
		
		if ( this.filtroSeleccionado == null || this.filtroSeleccionado.isEmpty() )
			this.verBotonConsultarTodo = Boolean.TRUE;
		
		this.jsfUtils.actualizarComponentePorId("flConsultarUsuario");
		
	}
	
	private void inicializarFiltrUsuario() {
		
		this.consultaFiltroUsuario = new UsuarioDTO();
		this.consultaFiltroUsuario.setRolAsignado(new RolDTO());
		this.consultaFiltroUsuario.setPaisAsignado(new PaisDTO());
		this.consultaFiltroUsuario.setCiudadAsignada(new CiudadDTO());
		
	}
	
	private void actualizarPanelConsultarUsuario() {
		this.jsfUtils.actualizarComponentePorId("pnlConsultarUsuario");
	}
	
	public void consultarCiudadesXPaises() {
		try {
			
			if ( this.consultaFiltroUsuario.getPaisAsignado().getIdPais() == null ) {
				this.consultaFiltroUsuario.getCiudadAsignada().setIdCiudad(null);
				this.deshabilitarSeleccionCiudad = Boolean.TRUE;
				return ;
			}
			
			if ( this.verCiudad && this.consultaFiltroUsuario.getPaisAsignado().getIdPais() != null ) {
				this.lstCiudadesActuales = this.ciudadService.consultarCiudadesXPais(this.consultaFiltroUsuario.getPaisAsignado().getIdPais());
				this.deshabilitarSeleccionCiudad = Boolean.FALSE;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.actualizarPanelConsultarUsuario();
		}
	}
	
	public void consultarUsuarioXFiltro() {
		try {
			
			if ( this.verResultados ) {
				this.verResultados = Boolean.FALSE;
				this.jsfUtils.actualizarComponentePorId("flConsultarUsuario");
			}
			
			if ( !this.validacionConsultarUsuario() ) {
				this.jsfUtils.addMsgError("No se ha detectado ningun valor de busqueda, porfavor ingrese los campos de busqueda");
				return ;
			}
			
			this.lstResultadoFiltro = this.usuarioService.consultarUsuarioXFiltro(this.consultaFiltroUsuario);
			
			if ( this.lstResultadoFiltro == null || this.lstResultadoFiltro.isEmpty() ) {
				this.jsfUtils.addMsgError("No se encontro registros con la busqueda ingresada, intente otra combinacion");
				return ;
			}
			
			this.inicializarFiltrUsuario();
 			this.verResultados = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("flConsultarUsuario");
			this.jsfUtils.actualizarComponentePorId("dtUsuarioXFiltro");
			
			this.jsfUtils.addMsgInfo("Se han encontrado coincidencias de busqueda");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	private Boolean validacionConsultarUsuario() {
		
		Integer filtrosIdentificados = 0; 
		
		if ( this.consultaFiltroUsuario != null && !Objects.isNull(this.consultaFiltroUsuario) ) {
			
			if ( this.consultaFiltroUsuario.getRolAsignado() != null )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroUsuario.getPaisAsignado() != null )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroUsuario.getCiudadAsignada() != null )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroUsuario.getNombreUsuario() != null && !this.consultaFiltroUsuario.getNombreUsuario().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroUsuario.getIdEstado() != null && !this.consultaFiltroUsuario.getIdEstado().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroUsuario.getLstFechasRango() != null && !this.consultaFiltroUsuario.getLstFechasRango().isEmpty() )
				filtrosIdentificados++;
				
		}
		
		if ( filtrosIdentificados >= 1 )
			return Boolean.TRUE;
		
		return Boolean.FALSE;
		
	}
	
	public void consultarTodosLosUsuario() {
		
		this.lstResultadoFiltro = this.usuarioService.consultarTodosLosUsuario();
		this.verResultados = Boolean.TRUE;
		this.jsfUtils.actualizarComponentePorId("flConsultarUsuario");
		this.jsfUtils.actualizarComponentePorId("dtUsuarioXFiltro");
		this.jsfUtils.addMsgInfo("Se han encontrado coincidencias de busqueda");
		this.jsfUtils.updateMsg();
		
	}
	
	public void abrirDlgInformacionUsuarioSeleccionado(UsuarioDTO usuarioSeleccionado) {
		this.usuarioSeleccionado = SerializationUtils.clone(usuarioSeleccionado);
		this.jsfUtils.actualizarComponentePorId("InformacionUsuarioSeleccionado");
		PrimeFaces.current().executeScript("PF('wvInfoUsuario').show();");
	}
	
}
