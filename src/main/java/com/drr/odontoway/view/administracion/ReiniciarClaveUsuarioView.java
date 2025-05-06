package com.drr.odontoway.view.administracion;

import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.UsuarioDTO;
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
public class ReiniciarClaveUsuarioView extends ModulBaseView {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Getter @Setter
	private UsuarioDTO usuarioBusqueda;
	
	@Getter @Setter
	private Boolean deshabilitarBtnReiniciarClave;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("modificar");
		this.deshabilitarBtnReiniciarClave = Boolean.TRUE;
		
	}
	
	public List<UsuarioDTO> autocompletarUsuarios(String busquedaUsuario) {
        return this.usuarioService.consultarUsuarioBusqueda(busquedaUsuario);
    }
	
	public void identificarUsuario() {
		
		if ( !this.deshabilitarBtnReiniciarClave )
			this.deshabilitarBtnReiniciarClave = Boolean.TRUE;
		
		if ( this.usuarioBusqueda != null && !Objects.isNull(this.usuarioBusqueda) )
			this.deshabilitarBtnReiniciarClave = Boolean.FALSE;
		
		this.jsfUtils.addMsgInfo("Se ha seleccionado al usuario: "+this.usuarioBusqueda.getNombreUsuario());
		this.jsfUtils.updateMsg();
		this.jsfUtils.actualizarComponentePorId("flReiniciarClaveUsuario");
		
	}
	
	public void reiniciarClaveUsuarioSeleccionado() {
		
		if ( this.usuarioService.actualizarClaveUsuario(this.usuarioBusqueda.getNombreUsuario(), "estandar") ) {
			this.jsfUtils.addMsgInfo("Se ha reiniciado la clave del usuario: "+this.usuarioBusqueda.getNombreUsuario()+" correctamente");
			this.deshabilitarBtnReiniciarClave = Boolean.TRUE;
			this.usuarioBusqueda = new UsuarioDTO();
		} else {
			this.jsfUtils.addMsgError("Ha ocurrido un error reiniciando la clave del usuario seleccionado");
		}
		
		this.jsfUtils.updateMsg();
		this.jsfUtils.actualizarComponentePorId("flReiniciarClaveUsuario");
		
	}
	
}
