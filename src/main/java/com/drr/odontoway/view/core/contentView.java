package com.drr.odontoway.view.core;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;

import com.drr.odontoway.core.service.MenuService;
import com.drr.odontoway.view.core.dto.TabItemDTO;
import com.drr.odontoway.view.util.JsfUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class contentView implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private Integer activeTab;
	
	@Getter @Setter
	private List<TabItemDTO> lstTabs;
	
	@Inject
	private MenuService menuService;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@PostConstruct
	private void init() {
		this.activeTab = 0;
		this.lstTabs = new ArrayList<>();
    }
	
	public void cargarPaginaSeleccionada(Integer idMenu, String nombreMenu, String rutaUrl, String nombreDocumento) {
		
		if ( this.lstTabs != null && lstTabs.size() >= 5 ) {
			this.jsfUtils.addMsgError("No se permiten cargar mas de 5 pestañas, porfavor cierre alguna que no este utilizando");
			this.jsfUtils.updateMsg();
			return ;
		}
		
		String msjUbicacion = this.menuService.ubicacionMenu(idMenu);
		
		TabItemDTO newTab = TabItemDTO.builder()
									  .title(nombreMenu)
									  .content(rutaUrl+File.separator+nombreDocumento+".xhtml")
									  .path(msjUbicacion)
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
