package com.drr.odontoway.view.administracion.gestionDeUsuarios.procedimientos;

import java.util.ArrayList;
import java.util.List;

import com.drr.odontoway.core.dto.ProcedimientoDTO;
import com.drr.odontoway.core.service.ProcedimientoService;
import com.drr.odontoway.view.util.JsfUtils;
import com.drr.odontoway.view.util.base.ModulBaseView;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class CrearProcedimientoView extends ModulBaseView {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private JsfUtils jsfUtils;
	
	@Inject
	private ProcedimientoService procedimientoService;
	
	@Getter @Setter
	private ProcedimientoDTO procedimientoParaCrear;
	
	@Getter @Setter
	private List<SelectItem> lstCantidadesDisponibles;
	
	@Getter @Setter
	private List<SelectItem> lstTiempoDuracionDisponibles;
	
	@Getter @Setter
	private Boolean deshabilitarBtnCrear;
	
	@PostConstruct
	public void init() {
		
		super.initModulBase("crear");
		
		
		this.inicializarProcedimiento();
		this.deshabilitarBtnCrear = Boolean.TRUE;
		this.cargarCantidadesDisponibles();
		this.cargarTiempoDuracionDisponibles();
	}
	
	private void inicializarProcedimiento() {
		this.procedimientoParaCrear = new ProcedimientoDTO();
	}
	
	public void cargarCantidadesDisponibles() {
	    this.lstCantidadesDisponibles = new ArrayList<>();

	    for (int i = 1; i <= 36; i++) {
	    	this.lstCantidadesDisponibles.add(new SelectItem(i, String.valueOf(i)));
	    }
	    
	}
	
	public void cargarTiempoDuracionDisponibles() {
	    this.lstTiempoDuracionDisponibles = new ArrayList<>();
	    this.lstTiempoDuracionDisponibles.add(new SelectItem("minutos", "Minutos"));
	    this.lstTiempoDuracionDisponibles.add(new SelectItem("horas", "Horas"));
	    this.lstTiempoDuracionDisponibles.add(new SelectItem("dias", "Días"));
	    this.lstTiempoDuracionDisponibles.add(new SelectItem("semanas", "Semanas"));
	    this.lstTiempoDuracionDisponibles.add(new SelectItem("meses", "Meses"));
	}
	
	public void crearProcedimiento() {
		try {
			
			if ( !validarDatosObligatorios() )
				return ;
			
			if ( this.procedimientoService.crearProcedimiento(this.procedimientoParaCrear, this.jsfUtils.nombreUsuarioLogeado())) {
				this.jsfUtils.addMsgInfo("Se ha creado el procedimiento correctamente");
				this.inicializarProcedimiento();
				this.actualizarPnlCrearProcedimiento();
			} else {
				this.jsfUtils.addMsgError("Ha ocurrido un error guardando el procedimiento");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			this.jsfUtils.updateMsg();
		}
	}
	
	private Boolean validarDatosObligatorios() {
		try {
			
			if ( this.procedimientoParaCrear.getTipoProcedimiento() == null
					|| this.procedimientoParaCrear.getTipoProcedimiento().isEmpty() ) {
				this.jsfUtils.addMsgError("No se ha seleccionado ningun tipo de procedimiento");
				return Boolean.FALSE;
			}
			
			if ( this.procedimientoParaCrear.getNombreProcedimiento() == null
					|| this.procedimientoParaCrear.getNombreProcedimiento().isEmpty()) {
				this.jsfUtils.addMsgError("No se definido ningun nombre para el procedimiento");
				return Boolean.FALSE;
			}
			
			if ( procedimientoParaCrear.getDescripcionProcedimiento() == null
					|| this.procedimientoParaCrear.getDescripcionProcedimiento().isEmpty() ) {
				this.jsfUtils.addMsgError("No se ha definido ninguna descripcion para el procedimiento");
				return Boolean.FALSE;
			}
			
			if ( this.procedimientoParaCrear.getCantidad() == null ) {
				this.jsfUtils.addMsgError("No se ha seleccionado una cantidad");
				return Boolean.FALSE;
			}
			
			if ( this.procedimientoParaCrear.getTiempoDuracion() == null
					|| this.procedimientoParaCrear.getTiempoDuracion().isEmpty() ) {
				this.jsfUtils.addMsgError("No se ha seleccionado ningun tipo de duracion");
				return Boolean.FALSE;
			}
			
			if ( this.procedimientoParaCrear.getValorCosto() == null ) {
				this.jsfUtils.addMsgError("No se ha dado ningun valor de costo");
				return Boolean.FALSE;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.TRUE;
	}
	
	private void actualizarPnlCrearProcedimiento() {
		this.jsfUtils.actualizarComponentePorId("flCrearProcedimiento");
	}
	
}
