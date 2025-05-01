package com.drr.odontoway.view.administracion.gestionDeUsuarios;

import com.drr.odontoway.core.dto.PerfilDTO;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "perfilConverter")
public class PerfilConverter implements Converter<PerfilDTO> {

    @Override
    public PerfilDTO getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        Integer idPerfil = Integer.valueOf(value);

        AsigarPerfilUsuarioView view = context.getApplication()
                                          .evaluateExpressionGet(context, "#{perfilesUsuarioView}", AsigarPerfilUsuarioView.class);

        return view.buscarPerfilPorId(idPerfil);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, PerfilDTO value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getIdPerfil());
    }
}
