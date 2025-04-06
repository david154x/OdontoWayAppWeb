package com.drr.odontoway.view.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TabItemDTO {
	
	private String title;
	private String content;
	private String path;

}
