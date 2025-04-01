package com.drr.odontoway.view.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;

import com.drr.odontoway.view.core.dto.TabItemDTO;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class WelcomeView implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private Integer activeTab;
	
	@Getter @Setter
	private List<TabItemDTO> lstTabs;
	
	@PostConstruct
	private void init() {
		this.activeTab = 0;
		this.lstTabs = new ArrayList<>();
//		crearTabBienvenidaAplicacion();
    }
	
	private void crearTabBienvenidaAplicacion() {
		cargarPaginaSeleccionada("Home");
	}
	
	public void cargarPaginaSeleccionada(String menuSeleccionado) {
		
		if ( menuSeleccionado.equals("HelloPetStore") ) {
			this.agregarTab(menuSeleccionado, menuSeleccionado, null);
			return ;
		}
		
	    String msjUbicacion = "Estas en: " + menuSeleccionado;
	    
		if ( menuSeleccionado.equals("createBook") ) {
			
			msjUbicacion = "Estas en: "+"Crear libro";
		    
		    this.agregarTab("Crear libro", menuSeleccionado, msjUbicacion);
			
		} else if ( menuSeleccionado.equals("userManagement") )  {
			
			msjUbicacion = "Estas en: "+"Gestion de usuarios";

		    this.agregarTab("Gestion de usuarios", menuSeleccionado, msjUbicacion);
		    
		} else {
			this.agregarTab(menuSeleccionado, menuSeleccionado.toLowerCase(), msjUbicacion);
		}
	    
	}
	
	
	
	public void agregarTab(String title, String content, String path) {
		
		TabItemDTO newTab = TabItemDTO.builder()
									  .title(title)
									  .content("/BussinessView/"+content+"/"+content+"View.xhtml")
									  .path(path)
									  .build();
		
		this.lstTabs.add(newTab);
		this.activeTab = this.lstTabs.size() - 1;
		
	}
	
	public Boolean closeTab(TabItemDTO tabSeleccionado) {
	
		if ( this.lstTabs.contains(tabSeleccionado) ) {
			this.lstTabs.remove(tabSeleccionado);
			this.activeTab = this.lstTabs.size() - 1;
			updateTabViews();
		}
		
		return Boolean.TRUE;
		
	}
	
	private void updateTabViews() {
		PrimeFaces.current().ajax().update("formPrincipal:tabViewContent");
	}
	
	public Boolean showButtonCloseAll(TabItemDTO tabSeleccionado) {

		if ( tabSeleccionado.getTitle().equals("Home") ) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}

	}
	
	public void cerrarSesion() {
		System.out.println("Se ha cerrado sesion correctamente");
	}
	
}
