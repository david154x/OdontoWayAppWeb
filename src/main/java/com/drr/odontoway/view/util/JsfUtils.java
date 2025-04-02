package com.drr.odontoway.view.util;

import org.primefaces.PrimeFaces;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@ApplicationScoped
public class JsfUtils {
	
	@Inject
	private FacesContext fc;
	
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
	
}
