package com.drr.odontoway.view.converter;

import com.drr.odontoway.core.dto.MenuDTO;
import com.drr.odontoway.core.service.MenuService;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "menuConverter", managed = true)
public class MenuConverter implements Converter<MenuDTO> {
	
	@Inject
	private MenuService menuService;

    @Override
    public MenuDTO getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        Integer idMenu = Integer.valueOf(value);

        return this.menuService.consultarMenuXId(idMenu);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, MenuDTO value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getIdMenu());
    }

}
