package com.drr.odontoway.view.administracion.gestionDeUsuarios;

import com.drr.odontoway.core.dto.UsuarioDTO;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "usuarioConverter")
public class UsuarioConverter implements Converter<UsuarioDTO> {
	
	@Override
	public UsuarioDTO getAsObject(FacesContext context, UIComponent component, String value) {
	    if (value == null || value.isEmpty() || "null".equalsIgnoreCase(value)) {
	        return null;
	    }
	    Integer idUsuario = Integer.valueOf(value);
	    
	    AsigarPerfilUsuarioView view = context.getApplication()
	            .evaluateExpressionGet(context, "#{perfilesUsuarioView}", AsigarPerfilUsuarioView.class);
	    
	    return view.buscarUsuarioPorId(idUsuario);
	}

    @Override
    public String getAsString(FacesContext context, UIComponent component, UsuarioDTO value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getIdUsuario());
    }
    
}
