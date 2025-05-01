package com.drr.odontoway.view.core;

import java.io.Serializable;

import com.drr.odontoway.core.service.UsuarioService;
import com.drr.odontoway.view.util.JsfUtils;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class CambioClaveView implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private SessionDataView sessionDataView;
	
	@Getter @Setter
	private String primerClave;
	
	@Getter @Setter
	private String segundaClave;
	
	private void cerrarDlgCambiarCredenciales() {
		this.jsfUtils.ejecutarScript("PF('wvCambioCredencial').hide();");
	}
	
	public void actualizarCredencialesUsuario() {
		try {
			
			if ( this.primerClave == null ) {
				this.jsfUtils.addMsgError("No se ha ingresado ninguna clave");
				return ;
			}
			
			if ( this.segundaClave == null ) {
				this.jsfUtils.addMsgError("No se ha ingresado ningun valor en repetir contraseña");
				return ;
			}
			
			if ( !this.primerClave.equals(this.segundaClave) ) {
				this.jsfUtils.addMsgError("Las contraseñas no coinciden, porfavor validar");
				return ;
			}
			
			if ( !this.usuarioService.actualizarClaveUsuario(this.jsfUtils.nombreUsuarioLogeado(), this.primerClave) ) {
				this.jsfUtils.addMsgError("No se logro actualizar la contraseña");
				return ;
			}
			
			this.sessionDataView.setCambiarCredenciales(Boolean.FALSE);
			this.jsfUtils.addMsgInfo("Se ha actualizado la contraseña correctamente");
			this.cerrarDlgCambiarCredenciales();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	
	
}
