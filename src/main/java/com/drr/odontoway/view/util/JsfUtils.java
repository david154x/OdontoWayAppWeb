package com.drr.odontoway.view.util;

import org.mindrot.jbcrypt.BCrypt;
import org.primefaces.PrimeFaces;

import com.drr.odontoway.view.core.SessionDataView;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@ApplicationScoped
public class JsfUtils {
	
	@Inject
	private FacesContext fc;
	
	@Inject
	private SessionDataView sessionDataView;
	
	public void addMsgInfo(String msg) {
		this.fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", msg) );
	}
	
	public void addMsgWarning(String msg) {
		this.fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", msg) );
	}
	
	public void addMsgError(String msg) {
		this.fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg) );
	}
	
	public void updateMsg() {
		PrimeFaces.current().ajax().update("formPrincipal:msgGeneric");
	}
	
	public void actualizarComponentePorId(String id) {
		PrimeFaces.current().ajax().update("formPrincipal:tabViewContent:"+id);
	}
	
	public void ejecutarScript(String script) {
		PrimeFaces.current().executeScript(script);
	}
	
	public String colorTipoModulo(String tipoFuncionalidad) {
		try {
			
			if ( tipoFuncionalidad.equals("consultar") )
				return "color: #17a2b8;";
			
			if ( tipoFuncionalidad.equals("crear") )
				return "color: #00bfff;";
			
			if ( tipoFuncionalidad.equals("modificar") )
				return "color: #ffc107;";
			
			if ( tipoFuncionalidad.equals("eliminar") )
				return "color: #dc3545;";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String estadoEnSistema() {
		try {
			
			if ( this.sessionDataView.getEstadoSistema().equals("A") ) {
				return "Autorizado";
			}
			
			if ( this.sessionDataView.getEstadoSistema().equals("X") ) {
				return "Denegado";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer idMenu() {
		try {
			
			if ( this.sessionDataView.getIdMenu() != null )
				return this.sessionDataView.getIdMenu();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String nombreDeModulo() {
		try {
			
			if ( this.sessionDataView.getNombreModulo() != null && !this.sessionDataView.getNombreModulo().isBlank() )
				return this.sessionDataView.getNombreModulo();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String nombreDeClaseView() {
		try {
			
			if ( this.sessionDataView.getNombreClaseView() != null && !this.sessionDataView.getNombreClaseView().isBlank() )
				return this.sessionDataView.getNombreClaseView();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String colorTextoSegunEstadoEnSistema() {
		try {
			
			if ( this.sessionDataView.getEstadoSistema().equals("A") ) {
				return "tx-ok";
			}
			
			if ( this.sessionDataView.getEstadoSistema().equals("X") ) {
				return "tx-no";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String colorFondoSegunEstadoEnSistema() {
		try {
			
			if ( this.sessionDataView.getEstadoSistema().equals("A") ) {
				return "bg-ok";
			}
			
			if ( this.sessionDataView.getEstadoSistema().equals("X") ) {
				return "bg-no";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Boolean permitirFuncionalidad() {
		try {
			
			if ( this.sessionDataView.getEstadoSistema().equals("A") )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
	
	public String nombreUsuarioLogeado() {
		return this.sessionDataView.getNombreUsuario();
	}
	
	public String claveUsuarioGenerica() {
		String valorEncriptado = null;
		try {
			
			valorEncriptado = BCrypt.hashpw("estandar", BCrypt.gensalt());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valorEncriptado;
	}
	
}
