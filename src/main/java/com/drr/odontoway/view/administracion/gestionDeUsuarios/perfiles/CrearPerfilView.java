package com.drr.odontoway.view.administracion.gestionDeUsuarios.perfiles;

import com.drr.odontoway.core.dto.PerfilDTO;
import com.drr.odontoway.core.service.PerfilService;
import com.drr.odontoway.view.util.base.ModulBaseView;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class CrearPerfilView extends ModulBaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PerfilService perfilService;
	
	@Getter @Setter
	private PerfilDTO perfilParaCrear;
	
	@Getter @Setter
	private Boolean deshabilitarBtnCrear;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("crear");
		this.deshabilitarBtnCrear = Boolean.TRUE;
		this.inicializarPerfilParaCrear();
		
	}
	
	private void inicializarPerfilParaCrear() {
		this.perfilParaCrear = new PerfilDTO();
	}
	
	public void crearPerfil() {
		try {
			
			if ( this.perfilService.crearPerfil(perfilParaCrear, this.jsfUtils.nombreUsuarioLogeado()))
				this.jsfUtils.addMsgInfo("Se ha creado el perfil correctamente");
			
			this.inicializarPerfilParaCrear();
			this.deshabilitarBtnCrear = Boolean.TRUE;
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.actualizarBtnCrearPerfil();
			this.jsfUtils.actualizarComponentePorId("flCrearPerfil");
			this.jsfUtils.updateMsg();
		}
	}
	
	public void validarSiYaExisteNombrePerfil() {
		try {
			
			if ( !this.deshabilitarBtnCrear )
				this.deshabilitarBtnCrear = Boolean.TRUE;
			
			if ( this.perfilParaCrear.getNombrePerfil() == null || this.perfilParaCrear.getNombrePerfil().isEmpty() ) {
				return ;
			}
			
			if ( this.perfilParaCrear.getNombrePerfil().length() < 5  ) {
				this.jsfUtils.addMsgError("El nombre del perfil no puede tener menos de 5 caracteres");
				return ;
			}
			
			if ( this.perfilParaCrear.getDescripcionPerfil() != null
					&& !this.perfilParaCrear.getDescripcionPerfil().isEmpty() ) {
				this.deshabilitarBtnCrear = Boolean.FALSE;
				return;
			}
			
			if ( this.perfilService.consultarSiPerfilXNombreExiste(this.perfilParaCrear.getNombrePerfil()) ) {
				this.jsfUtils.addMsgError("Ya existe un perfil con el nombre ingresado, intente con otro valor");
				return ;
			}
			
			this.jsfUtils.addMsgInfo("El nombre ingresado es valido, se permite continuar");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.actualizarBtnCrearPerfil();
			this.jsfUtils.updateMsg();
		}
		
	}
	
	public void validarDatosCompletos() {
		try {
			
			if ( !this.deshabilitarBtnCrear )
				this.deshabilitarBtnCrear = Boolean.TRUE;
			
			if ( this.perfilParaCrear.getDescripcionPerfil() == null ) {
				return ;
			}
			
			if ( this.perfilParaCrear.getDescripcionPerfil().length() < 10  ) {
				this.jsfUtils.addMsgError("La descripcion no puede tener menos de 10 caracteres");
				return ;
			}
			
			this.jsfUtils.addMsgInfo("Todos los datos estan completos, se permite la creacion del perfil");
			this.deshabilitarBtnCrear = Boolean.FALSE;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.actualizarBtnCrearPerfil();
			this.jsfUtils.updateMsg();
		}
	}
	
	private void actualizarBtnCrearPerfil() {
		this.jsfUtils.actualizarComponentePorId("btnCrearPerfil");
	}

}
