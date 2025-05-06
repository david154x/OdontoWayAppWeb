package com.drr.odontoway.view.converter;

import com.drr.odontoway.core.dto.UsuarioDTO;
import com.drr.odontoway.core.service.UsuarioService;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "usuarioConverter", managed = true)
public class UsuarioConverter implements Converter<UsuarioDTO> {
	
	@Inject
	private UsuarioService usuarioService;
	
	@Override
	public UsuarioDTO getAsObject(FacesContext context, UIComponent component, String value) {
	    
		if (value == null || value.isEmpty() || "null".equalsIgnoreCase(value)) {
	        return null;
	    }
		
	    Integer idUsuario = Integer.valueOf(value);
	    return this.usuarioService.consultarUsuarioXId(idUsuario);
	    
	}

    @Override
    public String getAsString(FacesContext context, UIComponent component, UsuarioDTO value) {
        
    	if (value == null)
            return "";
    	
        return String.valueOf(value.getIdUsuario());
        
    }
    
}
