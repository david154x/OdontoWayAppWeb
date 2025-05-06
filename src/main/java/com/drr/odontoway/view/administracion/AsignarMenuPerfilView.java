package com.drr.odontoway.view.administracion;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DualListModel;

import com.drr.odontoway.core.dto.MenuDTO;
import com.drr.odontoway.core.dto.PerfilDTO;
import com.drr.odontoway.core.service.MenuService;
import com.drr.odontoway.core.service.PerfilService;
import com.drr.odontoway.view.util.base.ModulBaseView;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class AsignarMenuPerfilView extends ModulBaseView {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private PerfilService perfilService;
	
	@Inject
	private MenuService menuService;
	
	@Getter @Setter
	private PerfilDTO perfilBusqueda;
	
	@Getter @Setter
    private List<MenuDTO> lstMenuXPerfil;
	
	@Getter @Setter
    private List<MenuDTO> listaMenusDisponibles;
	
	@Getter @Setter
    private List<MenuDTO> menusSeleccionados;
	
	@Getter @Setter
	private DualListModel<MenuDTO> menusDualList;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("modificar");
		this.inicializarDualList();
		this.inicializarPerfilBusqueda();
		
	}
	
	public List<PerfilDTO> autocompletarPerfiles(String busquedaPerfil) {
        return this.perfilService.consultarPerfilBusqueda(busquedaPerfil);
    }
	
	public void cargarMenuPerfil() {
        try {
        	
            if (this.menusDualList != null)
                this.menusDualList = new DualListModel<>(new ArrayList<>(), new ArrayList<>());
            
            this.listaMenusDisponibles = this.menuService.consultarTodosLosMenu();
            List<MenuDTO> disponibles = new ArrayList<>(this.listaMenusDisponibles);

            this.lstMenuXPerfil = this.menuService.consultarMenuXPerfil(this.perfilBusqueda.getIdPerfil());

            if (this.lstMenuXPerfil == null || this.lstMenuXPerfil.isEmpty()) {
                this.jsfUtils.addMsgWarning("Este perfil no tiene menus asignados actualmente");
                this.lstMenuXPerfil = new ArrayList<>();
            } else {
            	this.jsfUtils.addMsgInfo("Se han encontrado menus asignados al perfil buscado");
                disponibles.removeAll(this.lstMenuXPerfil);
            }

            this.menusDualList = new DualListModel<>(disponibles, this.lstMenuXPerfil);

        } catch (Exception e) {
            e.printStackTrace();
            this.jsfUtils.addMsgError("Error al cargar menus del perfil");
        } finally {
            this.jsfUtils.actualizarComponentePorId("pickListMenu");
            this.jsfUtils.updateMsg();
        }
	}
	
	private void inicializarPerfilBusqueda() {
    	this.perfilBusqueda = new PerfilDTO();
    }

	private void inicializarDualList() {
		this.listaMenusDisponibles = new ArrayList<>();
		this.menusSeleccionados = new ArrayList<>();
		this.menusDualList = new DualListModel<>(this.listaMenusDisponibles, this.menusSeleccionados);
	}
	
	public void guardarMenusAsignadosPerfil() {
        try {
        	
            if ( this.perfilBusqueda == null || this.perfilBusqueda.getIdPerfil() == null ) {
                this.jsfUtils.addMsgError("Debe seleccionar un perfil primero");
                return;
            }

            List<MenuDTO> menusAsignados = this.menusDualList.getTarget();
            
            this.menuService.eliminarTodosLosMenusAsociadosAPerfil(this.perfilBusqueda.getIdPerfil());

            this.menuService.actualizarMenusXPerfil(
            		this.perfilBusqueda.getIdPerfil(), menusAsignados, this.jsfUtils.nombreUsuarioLogeado());

            this.jsfUtils.addMsgInfo("Menus asignados correctamente al perfil");
            
            this.cargarMenuPerfil();

        } catch (Exception e) {
            e.printStackTrace();
            this.jsfUtils.addMsgError("Ocurrió un error al guardar los perfiles");
        } finally {
            this.jsfUtils.updateMsg();
        }
    }
	
}
