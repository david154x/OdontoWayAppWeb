package com.drr.odontoway.view.administracion.parametros.paises;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

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
public class ModificarPaisView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private PaisService paisService;
	
	@Getter @Setter
	private List<PaisDTO> lstResultados;
	
	@Getter @Setter
	private PaisDTO paisSeleccionadoParaModificar;
	
	@Getter @Setter
	private Boolean habilitarBtnModificar;
	
	@Inject
	@Getter @Setter
	private PaisDTO paisConNuevosValores;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("modificar");
		
		this.consultarTodosLosPaises();
		this.habilitarBtnModificar = Boolean.TRUE;
		
	}
	
	public void consultarTodosLosPaises() {
		try {
			
			this.lstResultados = this.paisService.consultarTodosLosPaises();
			this.jsfUtils.addMsgInfo("Se ha actualizado la informacion correctamente");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("dtTodosLosPaises");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void abrirDialogoModificarRegistro(PaisDTO paisSeleccionado) {
		
		if ( !this.habilitarBtnModificar ) {
			this.habilitarBtnModificar = Boolean.TRUE;
			actualizarBtnGuardarCambios();
		}
		
		this.limpiarDatos();
		
		if ( this.paisSeleccionadoParaModificar != null && !Objects.isNull(this.paisSeleccionadoParaModificar) )
			this.paisSeleccionadoParaModificar = new PaisDTO();
		
		this.paisSeleccionadoParaModificar = SerializationUtils.clone(paisSeleccionado);
		
		this.jsfUtils.ejecutarScript("PF('wvModificarPais').show();");
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarPais:flDatosActuales");
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarPais:flNuevosValoresPais");
		
	}
	
	public void cambiarEstado(PaisDTO paisParaModificar) {
		this.paisService.activarODesactivarPais(paisParaModificar);
	}
	
	public void validarSiYaExisteNombrePais() {
		try {

			if (!this.habilitarBtnModificar) {
				this.habilitarBtnModificar = Boolean.TRUE;
				actualizarBtnGuardarCambios();
			}

			if ( this.paisConNuevosValores.getNombrePais() == null || this.paisConNuevosValores.getNombrePais().isEmpty() )
				return;

			if ( this.paisService.consultarSiPaisExisteXNombre(this.paisConNuevosValores.getNombrePais()) ) {
				this.jsfUtils.addMsgError("Ya existe un pais con el nombre: " + this.paisConNuevosValores.getNombrePais().toUpperCase()
						+ " intente con otro valor");
				this.limpiarDatos();
				this.jsfUtils.actualizarComponentePorId("formPopUpModificarPais:flNuevosValoresPais");
				return;
			}

			this.jsfUtils.addMsgInfo("Este nombre es valido, se permite continuar con la creacion");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
		public void validarNombreCorto() {
			try {
				
				if ( this.paisConNuevosValores.getNombreCorto() == null || this.paisConNuevosValores.getNombreCorto().isEmpty() )
					return ;
				
				if ( !this.habilitarBtnModificar ) {
					this.habilitarBtnModificar = Boolean.TRUE;
				}
				
				if ( this.paisService.consultarSiPaisExisteXNombreCorto(this.paisConNuevosValores.getNombreCorto()) ) {
					this.jsfUtils.addMsgError("Ya existe un pais con este nombre corto, intente con otro valor");
					return ;
				}
				
				this.jsfUtils.addMsgInfo("Este nombre corto es valido, se permite continuar con la modificacion");
				this.habilitarBtnModificar = Boolean.FALSE;
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.jsfUtils.updateMsg();
				actualizarBtnGuardarCambios();
			}
		}
	
	public void guardarCambios() {
		try {
			if ( !validarDatosActualizacion() )
				return ;
			
			this.paisConNuevosValores.setIdPais((this.paisSeleccionadoParaModificar.getIdPais()));
			
			if ( this.paisService.actualizarPais(this.paisConNuevosValores)) {
				this.consultarTodosLosPaises();
				this.jsfUtils.addMsgInfo("Se ha actualizado el registro correctamente");
				this.limpiarDatos();
				this.ocultarDialogoModificarRegistro();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	private Boolean validarDatosActualizacion() {
		
		if ( this.paisConNuevosValores.getNombrePais() == null || this.paisConNuevosValores.getNombreCorto().isEmpty() ) {
			this.jsfUtils.addMsgError("No se ha registrado ningun nombre, no se permite actualizar");
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	private void actualizarBtnGuardarCambios() {
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarPais:btnGuardarCambios");
	}
	
	private void limpiarDatos() {
		
		if ( this.paisConNuevosValores != null && !Objects.isNull(this.paisConNuevosValores) )
			this.paisConNuevosValores = new PaisDTO();
		
	}
	
	private void ocultarDialogoModificarRegistro() {
		this.jsfUtils.ejecutarScript("PF('wvModificarPais').hide();");
	}
	
}
