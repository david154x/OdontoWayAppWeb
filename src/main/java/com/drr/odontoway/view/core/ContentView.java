package com.drr.odontoway.view.core;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.PrimeFaces;

import com.drr.odontoway.core.service.MenuService;
import com.drr.odontoway.view.core.dto.TabItemDTO;
import com.drr.odontoway.view.util.JsfUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ContentView implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private FacesContext fc;
	
	@Getter @Setter
	private Integer activeTab;
	
	@Getter @Setter
	private List<TabItemDTO> lstTabs;
	
	@Inject
	private MenuService menuService;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private SessionDataView sessionDataView;
	
	@PostConstruct
	private void init() {
		this.activeTab = 0;
		this.lstTabs = new ArrayList<>();
		this.saludarUsuario();
		this.validarSiDebeCambiarCredenciales();
    }
	
	public void cargarPaginaSeleccionada(Integer idMenu, String nombreMenu, String rutaUrl, String nombreDocumento, String nombreClaseView) {
		
		String msjUbicacion = this.menuService.ubicacionMenu(idMenu);
		
		TabItemDTO newTab = TabItemDTO.builder()
									  .title(nombreMenu)
									  .content(rutaUrl+"/"+nombreDocumento+".xhtml")
									  .path(msjUbicacion)
									  .nombreView(nombreClaseView)
									  .build();
		
		definirAlcanceDeUsuario(idMenu, newTab);
		
		// SE VALIDA EL TAB Y SE VUELVE DEJAR EN SU POSCISION
		int index = this.lstTabs.indexOf(newTab);
		
		if ( index != -1 ) {
			
			this.activeTab = index;
			return;
			
		}
		
		// SI EL TAB NO EXISTE PERO SON MAS DE 5
		if ( this.lstTabs != null && lstTabs.size() >= 5 ) {
			this.jsfUtils.addMsgError("No se permiten cargar mas de 5 pestañas, porfavor cierre alguna que no este utilizando");
			this.jsfUtils.updateMsg();
			return ;
		}
		
		this.lstTabs.add(newTab);
		this.activeTab = this.lstTabs.size() - 1;
		
	}
	
	public Boolean closeTab(TabItemDTO tabSeleccionado) {
	
		if ( this.lstTabs.contains(tabSeleccionado) ) {
			
			FacesContext context = FacesContext.getCurrentInstance();
			Map<String, Object> viewMap = context.getViewRoot().getViewMap(false);

		    if ( viewMap != null )
		        viewMap.remove(tabSeleccionado.getNombreView());
		    
		    this.lstTabs.remove(tabSeleccionado);
			this.activeTab = this.lstTabs.size() - 1;
			
			updateTabViews();
			
		}
		
		return Boolean.TRUE;
		
	}
	
	private void definirAlcanceDeUsuario(Integer idMenu, TabItemDTO menuSeleccionado) {
		
//		if ( idMenu.equals(4) ) {
//			this.sessionDataView.setEstadoSistema("A");
//		}
//		
//		if ( idMenu.equals(5) ) {
//			this.sessionDataView.setEstadoSistema("A");
//		}
//		
//		if ( idMenu.equals(6) ) {
//			this.sessionDataView.setEstadoSistema("A");
//		}
//		
//		if ( idMenu.equals(23) ) {
//			this.sessionDataView.setEstadoSistema("A");
//		}
//		
//		if ( idMenu.equals(24) ) {
//			this.sessionDataView.setEstadoSistema("A");
//		}
//		
//		if ( idMenu.equals(25) ) {
//			this.sessionDataView.setEstadoSistema("A");
//		}
		
		this.sessionDataView.setEstadoSistema("A");
		
		this.sessionDataView.setIdMenu(idMenu);
		this.sessionDataView.setNombreModulo(menuSeleccionado.getTitle());
		this.sessionDataView.setNombreClaseView(menuSeleccionado.getNombreView());
		
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
	
	private void saludarUsuario() {
		
		if ( this.sessionDataView.getSaludarUsuario() != null && this.sessionDataView.getSaludarUsuario() ) {
			
			if ( this.sessionDataView.getGeneroUsuario().equals("M") )
				this.jsfUtils.addMsgInfo("Bienvenido "+this.sessionDataView.getNombreUsuario());
			
			if ( this.sessionDataView.getGeneroUsuario().equals("F") )
				this.jsfUtils.addMsgInfo("Bienvenida "+this.sessionDataView.getNombreUsuario());
			
			this.jsfUtils.updateMsg();
			this.sessionDataView.setSaludarUsuario(Boolean.FALSE);
		}
		
	}
	
	public void abrirDlgCerrarSesion() {
		this.jsfUtils.ejecutarScript("PF('wvLogout').show();");
	}
	
	public void cerrarSesion() {
		this.fc.getExternalContext().invalidateSession();
		String contextPath = this.fc.getExternalContext().getRequestContextPath();
		this.jsfUtils.ejecutarScript("PF('wvLogout').hide();");
		this.jsfUtils.ejecutarScript("setTimeout(function() { window.location.href = '" + contextPath + "/login.xhtml'; }, 1000);");
	}
	
	private void validarSiDebeCambiarCredenciales() {
		
		if ( this.sessionDataView.getCambiarCredenciales() )
			this.jsfUtils.ejecutarScript("PF('wvCambioCredencial').show();");
		
	}

}
