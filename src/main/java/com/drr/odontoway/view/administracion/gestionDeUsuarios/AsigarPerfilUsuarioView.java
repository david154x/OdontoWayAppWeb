package com.drr.odontoway.view.administracion.gestionDeUsuarios;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DualListModel;

import com.drr.odontoway.core.dto.PerfilDTO;
import com.drr.odontoway.core.dto.UsuarioDTO;
import com.drr.odontoway.core.service.PerfilService;
import com.drr.odontoway.core.service.UsuarioService;
import com.drr.odontoway.view.util.base.ModulBaseView;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class AsigarPerfilUsuarioView extends ModulBaseView {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private PerfilService perfilService;
	
	@Getter @Setter
	private UsuarioDTO usuarioBusqueda;
	
	@Getter @Setter
    private List<PerfilDTO> perfilesUsuario;
	
	@Getter @Setter
    private List<PerfilDTO> listaPerfilesDisponibles;
	
	@Getter @Setter
    private List<PerfilDTO> perfilesSeleccionados;
	
	@Getter @Setter
	private DualListModel<PerfilDTO> perfilesDualList;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("consultar");
		this.inicializarUsuarioBusqueda();
		this.inicializarDualList();
		
	}
    
    public List<UsuarioDTO> autocompletarUsuarios(String busquedaUsuario) {
        return this.usuarioService.consultarUsuarioBusqueda(busquedaUsuario);
    }

    public void cargarPerfilesUsuario() {
        try {
            if (this.perfilesDualList != null) {
                this.perfilesDualList = new DualListModel<>(new ArrayList<>(), new ArrayList<>());
            }

            this.listaPerfilesDisponibles = this.perfilService.consultarTodosLosPerfiles();
            List<PerfilDTO> disponibles = new ArrayList<>(this.listaPerfilesDisponibles);

            this.perfilesUsuario = this.usuarioService.obtenerPerfilesUsuario(this.usuarioBusqueda);

            if (this.perfilesUsuario == null || this.perfilesUsuario.isEmpty()) {
                this.jsfUtils.addMsgWarning("Este usuario no tiene perfiles asignados actualmente");

                this.perfilesUsuario = new ArrayList<>();
            } else {
            	this.jsfUtils.addMsgInfo("Se han encontrado perfiles asignados a este usuario");
                disponibles.removeAll(this.perfilesUsuario);
            }

            this.perfilesDualList = new DualListModel<>(disponibles, this.perfilesUsuario);

            this.jsfUtils.actualizarComponentePorId("pickListPerfiles");

        } catch (Exception e) {
            e.printStackTrace();
            this.jsfUtils.addMsgError("Error al cargar perfiles del usuario");
        } finally {
            this.jsfUtils.actualizarComponentePorId("pickListPerfiles");
            this.jsfUtils.updateMsg();
        }
    }

    
    private void inicializarUsuarioBusqueda() {
    	this.usuarioBusqueda = new UsuarioDTO();
    }
    
    public UsuarioDTO buscarUsuarioPorId(Integer idUsuario) {
        return usuarioService.consultarUsuarioXId(idUsuario);
    }
    
    public PerfilDTO buscarPerfilPorId(Integer idPerfil) {
        return this.perfilService.consultarPerfilXId(idPerfil);
    }
    
    private void inicializarDualList() {
        this.listaPerfilesDisponibles = new ArrayList<>();
        this.perfilesSeleccionados = new ArrayList<>();
        this.perfilesDualList = new DualListModel<>(this.listaPerfilesDisponibles, this.perfilesSeleccionados);
    }
    
    public void guardarPerfilesAsignadosAlUsuario() {
        try {
            if (this.usuarioBusqueda == null || this.usuarioBusqueda.getIdUsuario() == null) {
                this.jsfUtils.addMsgError("Debe seleccionar un usuario primero");
                return;
            }

            List<PerfilDTO> perfilesAsignados = this.perfilesDualList.getTarget();
            
            this.usuarioService.eliminarTodosLosPerfilesAsignadosUsuario(this.usuarioBusqueda.getIdUsuario());

            this.usuarioService.actualizarPerfilesDelUsuario(
            		this.usuarioBusqueda.getIdUsuario(), perfilesAsignados, this.jsfUtils.nombreUsuarioLogeado());

            this.jsfUtils.addMsgInfo("Perfiles asignados correctamente al usuario");
            
            cargarPerfilesUsuario();

        } catch (Exception e) {
            e.printStackTrace();
            this.jsfUtils.addMsgError("Ocurrió un error al guardar los perfiles");
        } finally {
            this.jsfUtils.updateMsg();
        }
    }
    
}
