package com.drr.odontoway.view.converter;

import com.drr.odontoway.core.dto.PerfilDTO;
import com.drr.odontoway.core.service.PerfilService;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "perfilConverter", managed = true)
public class PerfilConverter implements Converter<PerfilDTO> {
	
	@Inject
	private PerfilService perfilService;

    @Override
    public PerfilDTO getAsObject(FacesContext context, UIComponent component, String value) {
    	if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("null")) {
            return null;
        }
    	
    	Integer idPerfil = Integer.valueOf(value);
        return this.perfilService.consultarPerfilXId(idPerfil);
    	
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, PerfilDTO value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getIdPerfil());
    }
    
}
