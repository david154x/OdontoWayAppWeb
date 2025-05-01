package com.drr.odontoway.view.administracion.parametros.ciudades;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

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
public class ModificarCiudadView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CiudadService ciudadService;
	
	@Inject
	private PaisService paisService;
	
	@Getter @Setter
	private List<CiudadDTO> lstCiudadesActuales;
	
	@Getter @Setter
	private CiudadDTO ciudadSeleccionadaParaModificar;
	
	@Getter @Setter
	private Boolean deshabilitarBtnModificar;
	
	@Getter @Setter
	private CiudadDTO ciudadConNuevosValores;
	
	@Getter @Setter
	private List<PaisDTO> lstPaisesDisponibles;
	
	@Getter @Setter
	private Boolean deshabilitarInputNombreCorto;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("modificar");
		this.consultarTodasLasCiudades();
		
		this.deshabilitarBtnModificar = Boolean.TRUE;
		this.deshabilitarInputNombreCorto = Boolean.TRUE;
		
		this.ciudadConNuevosValores = new CiudadDTO();
		this.ciudadConNuevosValores.setPaisAsignado(new PaisDTO());
		
	}
	
	private void consultarTodasLasCiudades() {
		try {
			
			this.lstCiudadesActuales = this.ciudadService.consultarTodasLasCiudades();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("dtTodasLasCiudades");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void refrescarTodasLasCiudades() {
		try {
			
			this.lstCiudadesActuales = this.ciudadService.consultarTodasLasCiudades();
			this.jsfUtils.addMsgInfo("Se ha actualizado la informacion correctamente");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("dtTodasLasCiudades");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void cambiarEstado(CiudadDTO ciudadSeleccionada) {
		this.ciudadService.activarODesactivarCiudad(ciudadSeleccionada);
	}
	
	public void abrirDialogoModificarRegistro(CiudadDTO ciudadSeleccionado) {
		
		if ( !this.deshabilitarBtnModificar ) {
			this.deshabilitarBtnModificar = Boolean.TRUE;
			actualizarBtnGuardarCambios();
		}
		
		this.limpiarDatos();
		
		if ( this.ciudadSeleccionadaParaModificar != null && !Objects.isNull(this.ciudadSeleccionadaParaModificar) )
			this.ciudadSeleccionadaParaModificar = new CiudadDTO();
		
		this.ciudadSeleccionadaParaModificar = SerializationUtils.clone(ciudadSeleccionado);
		
		this.lstPaisesDisponibles = this.paisService.consultarTodosLosPaises();
		
		this.jsfUtils.ejecutarScript("PF('wvModificarCiudad').show();");
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarCiudad:flDatosActuales");
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarCiudad:flNuevosValoresCiudad");
		
	}
	
	private void actualizarBtnGuardarCambios() {
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarCiudad:btnGuardarCambios");
	}
	
	private void limpiarDatos() {
		
		this.ciudadConNuevosValores = new CiudadDTO();
		this.ciudadConNuevosValores.setPaisAsignado(new PaisDTO());
		
	}
	
	private void ocultarDialogoModificarRegistro() {
		this.jsfUtils.ejecutarScript("PF('wvModificarCiudad').hide();");
	}
	
	public void validarSiYaExisteCiudadXPais() {
		try {
			
			Integer paisModificado = null;
			
			if ( !this.deshabilitarInputNombreCorto )
				this.deshabilitarInputNombreCorto = Boolean.TRUE;
			
			if ( this.ciudadConNuevosValores.getNombreCiudad() == null || this.ciudadConNuevosValores.getNombreCiudad().isEmpty() )
				return ;
			
			// SI CAMBIAN EL PAIS, REALIZAMOS LA CONSULTA CON ESE PAIS MODIFICADO, CASO CONTRARIO TOMAMOS EL ANTERIOR
			if ( this.ciudadConNuevosValores.getPaisAsignado().getIdPais() != null ) {
				
				if ( !this.ciudadConNuevosValores.getPaisAsignado().getIdPais().equals(this.ciudadSeleccionadaParaModificar.getPaisAsignado().getIdPais()) )
					paisModificado = this.ciudadConNuevosValores.getPaisAsignado().getIdPais();
				
			} else {
				paisModificado = this.ciudadSeleccionadaParaModificar.getPaisAsignado().getIdPais();
			}
			
			if ( this.ciudadService.consultarSiExisteCiudadXPais(paisModificado, this.ciudadConNuevosValores.getNombreCiudad())) {
				this.jsfUtils.addMsgError("El pais seleccionado ya tiene una ciudad con el nombre: "+this.ciudadConNuevosValores.getNombreCiudad().toUpperCase()+" intente con otro valor");
				this.ciudadConNuevosValores.setNombreCiudad(null);
			} else {
				this.jsfUtils.addMsgInfo("El nombre ingresado es valido, porfavor asignar un nombre corto");
				this.deshabilitarInputNombreCorto = Boolean.FALSE;
			}
			
		} finally {
			this.jsfUtils.actualizarComponentePorId("formPopUpModificarCiudad:flNuevosValoresCiudad");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void confirmarValoresAsignado() {
		try {
			
			if ( !this.deshabilitarInputNombreCorto )
				this.deshabilitarInputNombreCorto = Boolean.TRUE;
			
			if ( this.ciudadConNuevosValores.getNombreCorto() == null || this.ciudadConNuevosValores.getNombreCorto().isEmpty() )
				return ;
			
			if ( this.ciudadConNuevosValores.getNombreCorto().length() < 3 ) {
				this.jsfUtils.addMsgError("El nombre corto no puede ser menor a 3 caracteres");
				return ;
			}
			
			this.deshabilitarBtnModificar = Boolean.FALSE;
			this.jsfUtils.addMsgInfo("Todos los valores son correctos, se permite continuar con la modificacion");
			
		} finally {
			this.actualizarBtnGuardarCambios();
			this.jsfUtils.updateMsg();
		}
	}
	
	public void guardarCambios() {
		try {
			
			this.ciudadConNuevosValores.setIdCiudad(this.ciudadSeleccionadaParaModificar.getIdCiudad());
			
			if ( this.ciudadService.modificarCiudad(this.ciudadConNuevosValores) ) {
				this.jsfUtils.addMsgInfo("Se ha modificado la ciudad correctamente");
				this.consultarTodasLasCiudades();
				ocultarDialogoModificarRegistro();
			}
			
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	public void seleccionPais() {
		
		if ( !this.deshabilitarBtnModificar ) {
			this.deshabilitarBtnModificar = Boolean.TRUE;
			actualizarBtnGuardarCambios();
		}
		
		if ( this.ciudadConNuevosValores.getNombreCiudad() != null && !this.ciudadConNuevosValores.getNombreCiudad().isEmpty() )
			this.ciudadConNuevosValores.setNombreCiudad(null);
		
		if ( this.ciudadConNuevosValores.getNombreCorto() != null && !this.ciudadConNuevosValores.getNombreCorto().isEmpty() )
			this.ciudadConNuevosValores.setNombreCorto(null);
		
		this.jsfUtils.actualizarComponentePorId("formPopUpModificarCiudad:flNuevosValoresCiudad");
		
	}

}
