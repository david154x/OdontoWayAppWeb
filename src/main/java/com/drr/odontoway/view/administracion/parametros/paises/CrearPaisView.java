package com.drr.odontoway.view.administracion.parametros.paises;

import java.util.Objects;

import com.drr.odontoway.core.dto.PaisDTO;
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
public class CrearPaisView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PaisService paisService;
	
	@Getter @Setter
	private PaisDTO paisParaCrear;
	
	@Getter @Setter
	private Boolean deshabilitarBtnCrear;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("crear");
		this.deshabilitarBtnCrear = Boolean.TRUE;
		this.paisParaCrear = new PaisDTO();
		
	}
	
	public void validarSiYaExisteNombrePais() {
		try {
			
			if ( !this.deshabilitarBtnCrear )
				this.deshabilitarBtnCrear = Boolean.TRUE;
			
			if ( this.paisParaCrear.getNombrePais() == null || this.paisParaCrear.getNombrePais().isEmpty() )
				return ;
			
			if ( this.paisService.consultarSiPaisExisteXNombre(this.paisParaCrear.getNombrePais()) ) {
				this.jsfUtils.addMsgError("Ya existe un pais con el nombre: "+this.paisParaCrear.getNombrePais()+", intente con otro valor");
				
				this.limpiarDatos();
				return ;
			}
			
			this.jsfUtils.addMsgInfo("Este nombre es valido, se permite continuar con la creacion");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
			this.actualizarBotonCreacion();
		}
	}
	
	public void validarNombreCorto() {
		try {
			
			if ( !this.deshabilitarBtnCrear )
				this.deshabilitarBtnCrear = Boolean.TRUE;
			
			if ( this.paisParaCrear.getNombreCorto() == null || this.paisParaCrear.getNombreCorto().isEmpty() )
				return ;
			
			if ( this.paisService.consultarSiPaisExisteXNombreCorto(this.paisParaCrear.getNombreCorto()) ) {
				this.jsfUtils.addMsgError("Ya existe un pais con este nombre corto, intente con otro valor");
				
				this.limpiarDatos();
				return ;
			}
			
			this.jsfUtils.addMsgInfo("Este nombre corto es valido, se permite continuar con la creacion");
			this.deshabilitarBtnCrear = Boolean.FALSE;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
			this.actualizarBotonCreacion();
		}
	}
	
	private void limpiarDatos() {
		
		this.paisParaCrear = new PaisDTO();
		this.jsfUtils.actualizarComponentePorId("pnlCrearPais");
		
	}
	
	public void crearPais() {
		try {
			
			if ( this.paisParaCrear != null && !Objects.isNull(this.paisParaCrear) ) {
				
				if ( this.paisService.crearPais(this.paisParaCrear, this.jsfUtils.nombreUsuarioLogeado()) )
					this.jsfUtils.addMsgInfo("Se ha creado el pais: "+paisParaCrear.getNombrePais().toUpperCase()+" correctamente");
				
				this.deshabilitarBtnCrear = Boolean.TRUE;
				
			}
			
		} catch (Exception e) {
			this.jsfUtils.addMsgError("No se pudo guardar el pais: "+e.getMessage());
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
			this.limpiarDatos();
			this.actualizarBotonCreacion();
		}
	}
	
	private void actualizarBotonCreacion() {
		this.jsfUtils.actualizarComponentePorId("btnCrearPais");
	}

}
