package com.drr.odontoway.view.administracion.gestionDeUsuarios.procedimientos;

import com.drr.odontoway.view.util.base.ModulBaseView;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ModificarProcedimientoView extends ModulBaseView {

	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("modificar");
		
	}
	
}
