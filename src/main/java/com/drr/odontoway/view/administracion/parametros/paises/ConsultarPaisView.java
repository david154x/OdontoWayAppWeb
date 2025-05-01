package com.drr.odontoway.view.administracion.parametros.paises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.PaisDTO;
import com.drr.odontoway.core.service.PaisService;
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
public class ConsultarPaisView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private PaisService paisService;
	
	@Inject
	@Getter @Setter
	private PaisDTO consultaFiltroPais;
	
	@Getter @Setter
	private List<String> lstTiposDefiltro;
	
	@Getter @Setter
	private List<String> filtroSeleccionado;
	
	@Getter @Setter
	private Boolean verNombre;
	
	@Getter @Setter
	private Boolean verEstado;
	
	@Getter @Setter
	private Boolean verFecha;
	
	@Getter @Setter
	private List<PaisDTO> lstResultadoFiltro;
	
	@Getter @Setter
	private Boolean verResultados;
	
	@Getter @Setter
	private Boolean verBotonConsultarTodo;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("consultar");
		
		this.filtroSeleccionado = new ArrayList<>();
		
		this.verNombre = Boolean.FALSE;
		this.verEstado = Boolean.FALSE;
		this.verFecha = Boolean.FALSE;
		
		this.verResultados = Boolean.FALSE;
		this.verBotonConsultarTodo = Boolean.FALSE;
		
		this.cargarTiposDeFiltro();
		this.verBtnConsultarTodo();
		
	}
	
	private void cargarTiposDeFiltro() {
		this.lstTiposDefiltro = Arrays.asList("Nombre", "Estado", "Fecha");
	}
	
	private void verBtnConsultarTodo() {
		
		if ( this.verBotonConsultarTodo )
			this.verBotonConsultarTodo = Boolean.FALSE;
		
		if ( this.filtroSeleccionado == null || this.filtroSeleccionado.isEmpty() )
			this.verBotonConsultarTodo = Boolean.TRUE;
		
		this.jsfUtils.actualizarComponentePorId("flPaisXFiltro");
		
	}
	
	public void onToggleSelect() {
		
		if ( this.verResultados ) {
			this.verResultados = Boolean.FALSE;
			this.lstResultadoFiltro.clear();
			this.jsfUtils.actualizarComponentePorId("flPaisXFiltro");
		}
		
		this.verBtnConsultarTodo();
		this.consultaFiltroPais = new PaisDTO();
		
		this.asignarFiltro();
		
    }
	
	public void asignarFiltro() {
		
		this.verNombre = this.filtroSeleccionado.contains("Nombre");
		this.verEstado = this.filtroSeleccionado.contains("Estado");
		this.verFecha = this.filtroSeleccionado.contains("Fecha");
		
		this.verBtnConsultarTodo();

		this.jsfUtils.actualizarComponentePorId("pnlConsultarPais");
		
	}
	
	public void limpiarFiltros() {
		
		if ( this.verNombre )
			this.verNombre = Boolean.FALSE;
		
		if ( this.verEstado )
			this.verEstado = Boolean.FALSE;
		
		if ( this.verFecha)
			this.verFecha = Boolean.FALSE;
		
		this.filtroSeleccionado = null;
		this.consultaFiltroPais = new PaisDTO(); 
		
	}
	
	public void consultarTodosLosPaises() {
		try {
			
			this.lstResultadoFiltro = this.paisService.consultarTodosLosPaises();
			this.verResultados = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("flPaisXFiltro");
			this.jsfUtils.actualizarComponentePorId("dtPaisXFiltro");
			
			this.jsfUtils.addMsgInfo("Se han encontrado coincidencias de busqueda");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	public void consultarPaisXFiltro() {
		try {
			
			if ( this.verResultados ) {
				this.verResultados = Boolean.FALSE;
				this.jsfUtils.actualizarComponentePorId("flPaisXFiltro");
			}
				
			
			if ( !this.validacionConsultarPais() ) {
				this.jsfUtils.addMsgError("No se ha detectado ningun valor de busqueda, porfavor ingrese los campos de busqueda");
				return ;
			}
			
			this.lstResultadoFiltro = this.paisService.consultarPaisXFiltros(this.consultaFiltroPais);
			
			if ( this.lstResultadoFiltro == null || this.lstResultadoFiltro.isEmpty() ) {
				this.jsfUtils.addMsgError("No se encontro registros con la busqueda ingresada, intente otra combinacion");
				return ;
			}
			
			this.consultaFiltroPais = new PaisDTO();
 			this.verResultados = Boolean.TRUE;
			this.jsfUtils.actualizarComponentePorId("flPaisXFiltro");
			this.jsfUtils.actualizarComponentePorId("dtPaisXFiltro");
			
			this.jsfUtils.addMsgInfo("Se han encontrado coincidencias de busqueda");
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	private Boolean validacionConsultarPais() {
		
		Integer filtrosIdentificados = 0; 
		
		if ( this.consultaFiltroPais != null && !Objects.isNull(this.consultaFiltroPais) ) {
			
			if ( this.consultaFiltroPais.getNombrePais() != null && !this.consultaFiltroPais.getNombrePais().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroPais.getIdEstado() != null && !this.consultaFiltroPais.getIdEstado().isEmpty() )
				filtrosIdentificados++;
			
			if ( this.consultaFiltroPais.getLstFechasRango() != null && !this.consultaFiltroPais.getLstFechasRango().isEmpty() )
				filtrosIdentificados++;
				
		}
		
		if ( filtrosIdentificados >= 1 )
			return Boolean.TRUE;
		
		return Boolean.FALSE;
		
	}

}
