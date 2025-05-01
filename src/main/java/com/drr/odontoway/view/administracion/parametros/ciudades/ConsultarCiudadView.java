package com.drr.odontoway.view.administracion.parametros.ciudades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.CiudadDTO;
import com.drr.odontoway.core.dto.PaisDTO;
import com.drr.odontoway.core.service.CiudadService;
import com.drr.odontoway.core.service.PaisService;
import com.drr.odontoway.view.util.base.ModulBaseView;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ConsultarCiudadView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CiudadService ciudadService;
	
	@Inject
	private PaisService paisService;
	
	@Getter @Setter
	private CiudadDTO consultaFiltroCiudad;
	
	@Getter @Setter
	private List<String> lstTiposDefiltro;
	
	@Getter @Setter
	private List<String> filtroSeleccionado;
	
	@Getter @Setter
	private List<PaisDTO> lstPaisesActuales;
	
	@Getter @Setter
	private Boolean verPais;
	
	@Getter @Setter
	private Boolean verNombre;
	
	@Getter @Setter
	private Boolean verEstado;
	
	@Getter @Setter
	private Boolean verFecha;
	
	@Getter @Setter
	private List<CiudadDTO> lstResultadoFiltro;
	
	@Getter @Setter
	private Boolean verResultados;
	
	@Getter @Setter
	private Boolean verBotonConsultarTodo;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("consultar");
		
		this.filtroSeleccionado = new ArrayList<>();
		
		this.verPais = Boolean.FALSE;
		this.verNombre = Boolean.FALSE;
		this.verEstado = Boolean.FALSE;
		this.verFecha = Boolean.FALSE;
		
		this.verResultados = Boolean.FALSE;
		this.verBotonConsultarTodo = Boolean.FALSE;
		
		this.limpiarFiltro();
		
		this.consultarTodosLosPaises();
		this.cargarTiposDeFiltro();
		this.verBtnConsultarTodo();
		
	}
	
	private void consultarTodosLosPaises() {
		try {
			
			this.lstPaisesActuales = this.paisService.consultarTodosLosPaises();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.actualizarPanelConsultarCiudad();
			this.jsfUtils.updateMsg();
		}
	}
	
	private void cargarTiposDeFiltro() {
		this.lstTiposDefiltro = Arrays.asList("Pais","Nombre", "Estado", "Fecha");
	}
	
	private void verBtnConsultarTodo() {
		
		if ( this.verBotonConsultarTodo )
			this.verBotonConsultarTodo = Boolean.FALSE;
		
		if ( this.filtroSeleccionado == null || this.filtroSeleccionado.isEmpty() )
			this.verBotonConsultarTodo = Boolean.TRUE;
		
		this.jsfUtils.actualizarComponentePorId("flConsultarCiudad");
		
	}
	
	public void onToggleSelect() {
		
		if ( this.verResultados ) {
			this.verResultados = Boolean.FALSE;
			this.lstResultadoFiltro.clear();
			this.jsfUtils.actualizarComponentePorId("flConsultarCiudad");
		}
		
		this.limpiarFiltro();
		this.asignarFiltro();
		
    }
	
	public void asignarFiltro() {
		
		this.verPais = this.filtroSeleccionado.contains("Pais");
		this.verNombre = this.filtroSeleccionado.contains("Nombre");
		this.verEstado = this.filtroSeleccionado.contains("Estado");
		this.verFecha = this.filtroSeleccionado.contains("Fecha");
		
		this.verBtnConsultarTodo();
		this.actualizarPanelConsultarCiudad();
		
	}
	
	private void actualizarPanelConsultarCiudad() {
		this.jsfUtils.actualizarComponentePorId("pnlConsultarCiudad");
	}
	
	public void consultarTodasLasCiudades() {
		try {
			
			this.lstResultadoFiltro = this.ciudadService.consultarTodasLasCiudades();
			this.verResultados = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("flConsultarCiudad");
			this.jsfUtils.actualizarComponentePorId("dtCiudadXFiltro");
			
			this.jsfUtils.addMsgInfo("Se han encontrado coincidencias de busqueda");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	public void consultarCiudadXFiltro() {
		try {
			
			if ( this.verResultados ) {
				this.verResultados = Boolean.FALSE;
				this.jsfUtils.actualizarComponentePorId("flConsultarCiudad");
			}
			
			if ( !this.validacionConsultarPais() ) {
				this.jsfUtils.addMsgError("No se ha detectado ningun valor de busqueda, porfavor ingrese los campos de busqueda");
				return ;
			}
			
			this.lstResultadoFiltro = this.ciudadService.consultarCiudadXFiltros(this.consultaFiltroCiudad);
			
			if ( this.lstResultadoFiltro == null || this.lstResultadoFiltro.isEmpty() ) {
				this.jsfUtils.addMsgError("No se encontro registros con la busqueda ingresada, intente otra combinacion");
				return ;
			}
			
			this.limpiarFiltro();
 			this.verResultados = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("flConsultarCiudad");
			this.jsfUtils.actualizarComponentePorId("dtCiudadXFiltro");
			
			this.jsfUtils.addMsgInfo("Se han encontrado coincidencias de busqueda");
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	private Boolean validacionConsultarPais() {
		
		Integer filtrosIdentificados = 0; 
		
		if ( this.consultaFiltroCiudad != null && !Objects.isNull(this.consultaFiltroCiudad) ) {
			
			if ( this.consultaFiltroCiudad.getPaisAsignado().getIdPais() != null )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroCiudad.getNombreCiudad() != null && !this.consultaFiltroCiudad.getNombreCiudad().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroCiudad.getIdEstado() != null && !this.consultaFiltroCiudad.getIdEstado().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroCiudad.getLstFechasRango() != null && !this.consultaFiltroCiudad.getLstFechasRango().isEmpty() )
				filtrosIdentificados++;
				
		}
		
		if ( filtrosIdentificados >= 1 )
			return Boolean.TRUE;
		
		return Boolean.FALSE;
		
	}
	
	private void limpiarFiltro() {
		this.consultaFiltroCiudad = new CiudadDTO();
		this.consultaFiltroCiudad.setPaisAsignado(new PaisDTO());
	}

}
