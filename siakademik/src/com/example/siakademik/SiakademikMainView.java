package com.example.siakademik;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;

public class SiakademikMainView extends CustomComponent {
	private static final long serialVersionUID = 820767829921737101L;
	public SiakademikMainView() {
		GridLayout gl = new GridLayout(2, 2);
		
		setCompositionRoot(gl);
	}

}
