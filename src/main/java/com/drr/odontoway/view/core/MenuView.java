package com.drr.odontoway.view.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.drr.odontoway.core.dto.MenuDTO;
import com.drr.odontoway.core.service.MenuService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class MenuView {

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
	        Map<Integer, DefaultSubMenu> modulos = new HashMap<>();

	        if (lstMenuUsuario != null && !lstMenuUsuario.isEmpty()) {

	            lstMenuUsuario.forEach(x -> {

	                // Si no tiene modulo (ModuloMenu es null), es un módulo principal
	                if ( x.getModuloMenu() == null && x.getSubMenu() == null ) {
	                	
	                    DefaultSubMenu modulo = DefaultSubMenu.builder()
	                                                          .label(x.getNombreMenu())
	                                                          .build();
	                    
	                    modulos.put(x.getIdMenu(), modulo);
	                    
	                }

	                // Si tiene modulo y no tiene un submenu
	                if ( x.getModuloMenu() != null && x.getSubMenu() == null ) {

	                    if ( x.getRutaUrl() != null && !x.getRutaUrl().isBlank() ) {
	                    	
	                        DefaultMenuItem item = DefaultMenuItem.builder()
	                                                              .value(x.getNombreMenu())
	                                                              .icon(x.getIconoMenu())
	                                                              .url(x.getRutaUrl())
	                                                              .build();
	                        
	                        DefaultSubMenu parentSubMenu = modulos.get(x.getModuloMenu());
	                        
	                        if (parentSubMenu != null)
	                            parentSubMenu.getElements().add(item);
	                        
	                    } else {
	                        
	                    	// Buscar el submenú en los modulos
	                        DefaultSubMenu parentSubMenu = modulos.get(x.getModuloMenu());

	                        if ( parentSubMenu == null )
	                            parentSubMenu = subMenus.get(x.getModuloMenu());

	                        // Si existe el submenú en modulos o subMenus, crear y agregar un submenú
	                        if ( parentSubMenu != null && !subMenus.containsKey(x.getIdMenu()) ) {
	                        	
	                            DefaultSubMenu subMenu = DefaultSubMenu.builder()
	                                                                    .label(x.getNombreMenu())
	                                                                    .build();
	                            
	                            parentSubMenu.getElements().add(subMenu);
	                            subMenus.put(x.getIdMenu(), subMenu);
	                            
	                        }
	                        
	                    }
	                }

	                // Si tiene id_sub_menu, pero tiene URL, debe ir al módulo
	                if ( x.getSubMenu() != null && (!x.getRutaUrl().isBlank() || !x.getRutaUrl().isEmpty()) ) {
	                    
	                	DefaultMenuItem item = DefaultMenuItem.builder()
	                                                          .value(x.getNombreMenu())
	                                                          .icon(x.getIconoMenu())
	                                                          .command("#{contentView.cargarPaginaSeleccionada('" + x.getIdMenu() + "','" + x.getNombreMenu() + "', '" + x.getRutaUrl() + "', '" + x.getNombreDocumento() + "')}")
	                                                          .update("formPrincipal:tabViewContent")
	                                                          .build();

	                    // Aquí verificamos si es un submenú dentro de otro submenú
	                    DefaultSubMenu parentSubMenu = subMenus.get(x.getSubMenu());
	                    
	                    if ( parentSubMenu != null ) {
	                        // Si es un submenú dentro de otro submenú, agregamos el ítem al submenú
	                        parentSubMenu.getElements().add(item);
	                    } else {
	                        // Si no es un submenú dentro de otro submenú, buscamos en los módulos
	                        parentSubMenu = modulos.get(x.getSubMenu());
	                        if (parentSubMenu != null) {
	                            // Agregar el ítem al módulo
	                            parentSubMenu.getElements().add(item);
	                        }
	                    }
	                }

	            });
	            
				List<DefaultSubMenu> lstMenuOrdenado = new ArrayList<>(modulos.values());

				lstMenuOrdenado.sort(Comparator.comparing(DefaultSubMenu::getLabel));

				for (DefaultSubMenu modulo : lstMenuOrdenado) {
					this.menuModel.getElements().add(modulo);
				}
	            
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
