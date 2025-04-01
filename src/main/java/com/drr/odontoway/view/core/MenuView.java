package com.drr.odontoway.view.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.drr.odontoway.core.dto.MenuDTO;
import com.drr.odontoway.core.service.MenuService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class MenuView implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private MenuModel menuModel;
	
	@Inject
	private MenuService menuService;
	
	@PostConstruct
	private void init() {
		this.menuModel = new DefaultMenuModel();
		this.crearMenuXUsuario();
	}
	
	private void crearMenuXUsuario() {
	    Integer idUsuario = 1;
	    try {
	        List<MenuDTO> lstMenuUsuario = this.menuService.consultarMenuXUsuario(idUsuario);
	        Map<Integer, DefaultSubMenu> subMenus = new HashMap<>();
	        Set<Integer> subMenusAnidados = new HashSet<>();

	        if ( lstMenuUsuario != null && !lstMenuUsuario.isEmpty() ) {

	            lstMenuUsuario.forEach(x -> {

	                if ( x.getMenuPadre() == null ) {
	                    
	                	DefaultSubMenu subMenu = DefaultSubMenu.builder()
	                    									   .label(x.getNombreMenu())
	                    									   .build();
	                    
	                    subMenus.put(x.getIdMenu(), subMenu);

	                } else {
	                	
	                    if ( x.getMenuPadre() != null && (x.getRutaUrl().isEmpty() || x.getRutaUrl().isBlank()) ) {
	                        
	                    	DefaultSubMenu parentSubMenu = subMenus.get(x.getMenuPadre());

	                        if ( parentSubMenu != null ) {
	                        	
	                            DefaultSubMenu subMenu = DefaultSubMenu.builder()
	                            									   .label(x.getNombreMenu())
	                            									   .build();

	                            parentSubMenu.getElements().add(subMenu);
	                            subMenus.put(x.getIdMenu(), subMenu);
	                            subMenusAnidados.add(x.getIdMenu());
	                            
	                        }

	                    } else {
	                    	
	                        DefaultMenuItem item = DefaultMenuItem.builder()
	                        									  .value(x.getNombreMenu())
	                        									  .icon(x.getIconoMenu())
	                        									  .url(x.getRutaUrl())
	                        									  .build();

	                        DefaultSubMenu parentSubMenu = subMenus.get(x.getMenuPadre());

	                        if (parentSubMenu != null)
	                            parentSubMenu.getElements().add(item);
	                    }
	                }
	                
	            });

	            for ( Map.Entry<Integer, DefaultSubMenu> entry : subMenus.entrySet() ) {
	            	
	                if ( !subMenusAnidados.contains(entry.getKey()) ) 
	                    this.menuModel.getElements().add(entry.getValue());
	                
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
//	private DefaultMenuItem crearMenuItem(String nombreItem) {
//		
//		DefaultMenuItem item = DefaultMenuItem.builder()
//							                  .value(nombreItem)
//							                  .ajax(Boolean.TRUE)
//							                  .command("#{welcomeView.cargarPaginaSeleccionada('" + nombreItem + "')}")
//							                  .update("formPrincipal:tabViewContent")
//							                  .build();
//		
//		if ( nombreItem.equals("Crear libro") ) {
//			nombreItem = "createBook";
//			item.setCommand("#{welcomeView.cargarPaginaSeleccionada('" + nombreItem + "')}");
//		}
//		
//		if ( nombreItem.equals("Gestion de usuarios") ) {
//			nombreItem = "userManagement";
//			item.setCommand("#{welcomeView.cargarPaginaSeleccionada('" + nombreItem + "')}");
//		}
//		
//		return item;
//		
//	}
	
}
