package de.mickare.chatapi.api;

import java.util.List;

public interface IComponentTranslate extends IComponentChat {

	public String getTranslate();
	
	public List<IComponentChat> getWith();
	
}
