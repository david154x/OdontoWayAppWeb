package com.drr.odontoway.view.core;

import java.io.Serializable;

import org.primefaces.PrimeFaces;

import com.drr.odontoway.core.dto.UsuarioDTO;
import com.drr.odontoway.core.service.UsuarioService;
import com.drr.odontoway.view.util.JsfUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class LoginView implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private SessionDataView sessionDataView;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private FacesContext fc;
	
	@Getter @Setter
	private String nombreUsuario;
	
	@Getter @Setter
	private String passwordUsuario;
	
	private UsuarioDTO usuarioAutenticado;
	
	@PostConstruct
	private void init() {
		
	}
	
	public void iniciarSesion() {
		try {
			
			this.usuarioAutenticado = this.usuarioService.iniciarSesion(this.nombreUsuario, this.passwordUsuario);
			
			if ( this.usuarioAutenticado == null ) {
				this.jsfUtils.addMsgError("Usuario o contraseña invalido");
				return ;
			}
			
			this.sessionDataView.setNombreUsuario(this.usuarioAutenticado.getNombreUsuario());
			this.sessionDataView.setNombreModulo(this.usuarioAutenticado.getRolAsignado().getNombreRol());
			this.sessionDataView.setGeneroUsuario(this.usuarioAutenticado.getGenero());
			this.sessionDataView.setSaludarUsuario(Boolean.TRUE);
			
			if ( this.usuarioAutenticado.getCambiarClaveGenerica() ) {
				this.sessionDataView.setCambiarCredenciales(Boolean.TRUE);
			} else {
				this.sessionDataView.setCambiarCredenciales(Boolean.FALSE);
			}
			
			this.fc.getExternalContext().getSessionMap().put("sessionDataView", this.sessionDataView);
			
			String contextPath = this.fc.getExternalContext().getRequestContextPath();
			this.jsfUtils.ejecutarScript("setTimeout(function() { window.location.href = '" + contextPath + "/index.xhtml'; }, 1000);");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PrimeFaces.current().ajax().update("loginForm:msgGeneric");
		}
	}
	
}
