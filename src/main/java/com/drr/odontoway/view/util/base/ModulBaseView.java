package com.drr.odontoway.view.util.base;

import java.io.Serializable;

import com.drr.odontoway.view.util.JsfUtils;

import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;

public abstract class ModulBaseView implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @Getter @Setter
    protected String colorTipoModulo;

    @Getter @Setter
    protected String estadoEnSistema;

    @Getter @Setter
    protected Integer idMenu;

    @Getter @Setter
    protected String nombreModulo;
    
    @Getter @Setter
    protected String nombreClaseView;

    @Getter @Setter
    protected String colorFondoSegunEstadoSistema;

    @Getter @Setter
    protected String colorTextoSegunEstadoSistema;

    @Getter @Setter
    protected Boolean permitirFuncionalidad;

    @Inject
    protected JsfUtils jsfUtils;

    public void initModulBase(String nombreModuloInterno) {
    	
        this.colorTipoModulo = this.jsfUtils.colorTipoModulo(nombreModuloInterno);
        this.estadoEnSistema = this.jsfUtils.estadoEnSistema();
        this.idMenu = this.jsfUtils.idMenu();
        this.nombreModulo = this.jsfUtils.nombreDeModulo();
        this.nombreClaseView = this.jsfUtils.nombreDeClaseView();
        this.colorFondoSegunEstadoSistema = this.jsfUtils.colorFondoSegunEstadoEnSistema();
        this.colorTextoSegunEstadoSistema = this.jsfUtils.colorTextoSegunEstadoEnSistema();
        this.permitirFuncionalidad = this.jsfUtils.permitirFuncionalidad();
        
    }

}
