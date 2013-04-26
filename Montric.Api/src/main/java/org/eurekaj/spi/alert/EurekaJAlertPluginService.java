/**
    EurekaJ Profiler - http://eurekaj.haagen.name
    
    Copyright (C) 2010-2011 Joachim Haagen Skeie

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package org.eurekaj.spi.alert;

import org.eurekaj.api.service.AlertService;
import org.eurekaj.api.service.EurekaJApplicationServices;

public abstract class EurekaJAlertPluginService {

	public abstract String getAlertPluginName();
	
	public abstract AlertService getAlertService();
	
	public void setApplicationServices(EurekaJApplicationServices applicationServices) {
    	//Each plugin must choose to implement this method in order to gain access to the EurekaJ Application Services 
    	return;
    }
}
