package com.drr.odontoway.view.administracion.parametros.ciudades;

import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.CiudadDTO;
import com.drr.odontoway.core.dto.PaisDTO;
import com.drr.odontoway.core.service.CiudadService;
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
public class CrearCiudadView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private PaisService paisService;
	
	@Inject
	private CiudadService ciudadService;
	
	@Getter @Setter
	private CiudadDTO ciudadParaCrear; 
	
	@Getter @Setter
	private Boolean deshabilitarBtnCrear;
	
	@Getter @Setter
	private List<PaisDTO> lstPaisesActuales;
	
	@Getter @Setter
	private Boolean deshabilitarInputNombreCiudad;
	
	@Getter @Setter
	private Boolean deshabilitarInputNombreCorto;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("crear");
		
		this.consultarTodosLosPaises();
		
		this.deshabilitarBtnCrear = Boolean.TRUE;
		this.deshabilitarInputNombreCiudad = Boolean.TRUE;
		this.deshabilitarInputNombreCorto = Boolean.TRUE;
		
		this.ciudadParaCrear = new CiudadDTO();
		this.ciudadParaCrear.setPaisAsignado(new PaisDTO());
		
	}
	
	private void consultarTodosLosPaises() {
		try {
			
			this.lstPaisesActuales = this.paisService.consultarTodosLosPaises();
			this.jsfUtils.addMsgInfo("Se ha actualizado la informacion correctamente");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.actualizarComponentePorId("pnlCrearCiudad");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void confirmarSeleccionPais() {
		
		if ( !this.deshabilitarInputNombreCiudad )
			this.deshabilitarInputNombreCiudad = Boolean.TRUE;
		
		if ( !this.deshabilitarInputNombreCorto )
			this.deshabilitarInputNombreCorto = Boolean.TRUE;
		
		if ( this.ciudadParaCrear.getPaisAsignado().getIdPais() != null )
			this.deshabilitarInputNombreCiudad = Boolean.FALSE;
		
		actualizarPanelCrearCiudad();
	}
	
	private void actualizarPanelCrearCiudad() {
		this.jsfUtils.actualizarComponentePorId("pnlCrearCiudad");
	}
	
	private void actualizarBotonCrearCiudad() {
		this.jsfUtils.actualizarComponentePorId("btnCrearCiudad");
	}
	
	public void validarSiYaExisteCiudadXPais() {
		try {
			
			if ( !this.deshabilitarInputNombreCorto )
				this.deshabilitarInputNombreCorto = Boolean.TRUE;
			
			if ( this.ciudadParaCrear.getNombreCiudad() == null || this.ciudadParaCrear.getNombreCiudad().isEmpty() )
				return ;
			
			if ( this.ciudadService.consultarSiExisteCiudadXPais(this.ciudadParaCrear.getPaisAsignado().getIdPais(), this.ciudadParaCrear.getNombreCiudad())) {
				this.jsfUtils.addMsgError("En el pais seleccionado, ya existe una ciudad con el nombre: "+this.ciudadParaCrear.getNombreCiudad().toUpperCase()+" intente con otro valor");
				this.ciudadParaCrear.setNombreCiudad(null);
			} else {
				this.jsfUtils.addMsgInfo("El nombre ingresado es valido, porfavor asignar un nombre corto");
				this.deshabilitarInputNombreCorto = Boolean.FALSE;
			}
			
		} finally {
			this.actualizarPanelCrearCiudad();
			this.jsfUtils.updateMsg();
		}
	}
	
	public void confirmarValoresAsignado() {
		try {
			
			if ( !this.deshabilitarBtnCrear )
				this.deshabilitarBtnCrear = Boolean.TRUE;
			
			if (  this.ciudadParaCrear.getNombreCorto() == null && this.ciudadParaCrear.getNombreCorto().isEmpty() )
				return ;
			
			if ( this.ciudadParaCrear.getNombreCorto().length() < 3 ) {
				this.jsfUtils.addMsgError("El nombre corto no puede ser menor a 3 caracteres");
				return ;
			}
			
			this.deshabilitarBtnCrear = Boolean.FALSE;
			this.jsfUtils.addMsgInfo("Todos los valores son correctos, se permite continuar con la creacion");
			
		} finally {
			this.actualizarBotonCrearCiudad();
			this.jsfUtils.updateMsg();
		}
	}
	
	public void crearCiudad() {
		try {
			
			if ( this.ciudadParaCrear != null && !Objects.isNull(this.ciudadParaCrear) ) {
				
				if ( this.ciudadService.crearCiudad(this.ciudadParaCrear, this.jsfUtils.nombreUsuarioLogeado()) )
					this.jsfUtils.addMsgInfo("Se ha creado la ciudad: "+this.ciudadParaCrear.getNombreCiudad().toUpperCase()+" correctamente");
				
				this.deshabilitarBtnCrear = Boolean.TRUE;
				
			}
			
		} catch (Exception e) {
			this.jsfUtils.addMsgError("No se pudo guardar la ciudad: "+e.getMessage());
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
			this.limpiarDatos();
			this.actualizarBotonCrearCiudad();
		}
	}
	
	private void limpiarDatos() {
		
		this.ciudadParaCrear = new CiudadDTO();
		this.ciudadParaCrear.setPaisAsignado(new PaisDTO());
		this.actualizarPanelCrearCiudad();
		
	}

}
