package com.drr.odontoway.view.administracion.gestionDeUsuarios.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
public class ConsultarRolView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private RolService rolService;
	
	@Inject
	@Getter @Setter
	private RolDTO consultaFiltroRol;
	
	@Getter @Setter
	private List<String> lstTiposDefiltro;
	
	@Getter @Setter
	private List<String> filtroSeleccionado;
	
	@Getter @Setter
	private List<Date> lstFechasRango;
	
	@Getter @Setter
	private Boolean verDescripcion;
	
	@Getter @Setter
	private Boolean verNombre;
	
	@Getter @Setter
	private Boolean verEstado;
	
	@Getter @Setter
	private Boolean verFecha;
	
	@Getter @Setter
	private List<RolDTO> lstResultadoFiltro;
	
	@Getter @Setter
	private Boolean verResultados;
	
	@Getter @Setter
	private Boolean verBotonConsultarTodo;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("consultar");
		
		this.filtroSeleccionado = new ArrayList<>();
		
		this.verNombre = Boolean.FALSE;
		this.verDescripcion = Boolean.FALSE;
		this.verEstado = Boolean.FALSE;
		this.verFecha = Boolean.FALSE;
		
		this.verResultados = Boolean.FALSE;
		this.verBotonConsultarTodo = Boolean.FALSE;
		
		this.cargarTiposDeFiltro();
		this.verBtnConsultarTodo();
		
	}
	
	private void cargarTiposDeFiltro() {
		this.lstTiposDefiltro = Arrays.asList("Nombre", "Descripcion", "Estado", "Fecha");
	}
	
	public void asignarFiltro() {
		
		this.verNombre = this.filtroSeleccionado.contains("Nombre");
		this.verDescripcion = this.filtroSeleccionado.contains("Descripcion");
		this.verEstado = this.filtroSeleccionado.contains("Estado");
		this.verFecha = this.filtroSeleccionado.contains("Fecha");
		
		this.verBtnConsultarTodo();

		this.jsfUtils.actualizarComponentePorId("pnlConsultarRol");
		
	}
	
	public void limpiarFiltros() {
		
		if ( this.verNombre )
			this.verNombre = Boolean.FALSE;
		
		if ( this.verDescripcion)
			this.verDescripcion = Boolean.FALSE;
		
		if ( this.verEstado )
			this.verEstado = Boolean.FALSE;
		
		if ( this.verFecha)
			this.verFecha = Boolean.FALSE;
		
		this.filtroSeleccionado = null;
		this.consultaFiltroRol = new RolDTO(); 
		
	}
	
	public void consultarTodosLosRoles() {
		try {
			
			this.lstResultadoFiltro = this.rolService.consultarTodosLosRoles();
			this.verResultados = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("flRolXFiltro");
			this.jsfUtils.actualizarComponentePorId("dtRolXFiltro");
			
			this.jsfUtils.addMsgInfo("Se han encontrado coincidencias de busqueda");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	public void consultarRolXFiltro() {
		try {
			
			if ( this.verResultados ) {
				this.verResultados = Boolean.FALSE;
				this.jsfUtils.actualizarComponentePorId("flRolXFiltro");
			}
				
			
			if ( !this.validacionConsultarRol() ) {
				this.jsfUtils.addMsgError("No se ha detectado ningun valor de busqueda, porfavor ingrese los campos de busqueda");
				return ;
			}
			
			this.lstResultadoFiltro = this.rolService.consultarRolXFiltros(this.consultaFiltroRol);
			
			if ( this.lstResultadoFiltro == null || this.lstResultadoFiltro.isEmpty() ) {
				this.jsfUtils.addMsgError("No se encontro registros con la busqueda ingresada, intente otra combinacion");
				return ;
			}
			
//			this.limpiarFiltros();
			this.consultaFiltroRol = new RolDTO();
 			this.verResultados = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("flRolXFiltro");
			this.jsfUtils.actualizarComponentePorId("dtRolXFiltro");
			
			this.jsfUtils.addMsgInfo("Se han encontrado coincidencias de busqueda");
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	private Boolean validacionConsultarRol() {
		
		Integer filtrosIdentificados = 0; 
		
		if ( this.consultaFiltroRol != null && !Objects.isNull(this.consultaFiltroRol) ) {
			
			if ( this.consultaFiltroRol.getNombreRol() != null && !this.consultaFiltroRol.getNombreRol().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroRol.getDescripcionRol() != null && !this.consultaFiltroRol.getDescripcionRol().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroRol.getIdEstado() != null && !this.consultaFiltroRol.getIdEstado().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroRol.getLstFechasRango() != null && !this.consultaFiltroRol.getLstFechasRango().isEmpty() )
				filtrosIdentificados++;
				
		}
		
		if ( filtrosIdentificados >= 1 )
			return Boolean.TRUE;
		
		return Boolean.FALSE;
		
	}
	
	public void onToggleSelect() {
		
		if ( this.verResultados ) {
			this.verResultados = Boolean.FALSE;
			this.lstResultadoFiltro.clear();
			this.jsfUtils.actualizarComponentePorId("flRolXFiltro");
		}
		
		this.verBtnConsultarTodo();
		this.consultaFiltroRol = new RolDTO();
		
		this.asignarFiltro();
		
    }
	
	public void verBtnConsultarTodo() {
		
		if ( this.verBotonConsultarTodo )
			this.verBotonConsultarTodo = Boolean.FALSE;
		
		if ( this.filtroSeleccionado == null || this.filtroSeleccionado.isEmpty() )
			this.verBotonConsultarTodo = Boolean.TRUE;
		
		this.jsfUtils.actualizarComponentePorId("flRolXFiltro");
		
	}
	
}
