package org.eurekaj.manager.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

import org.apache.log4j.Logger;
import org.eurekaj.api.datatypes.Alert;
import org.eurekaj.api.enumtypes.AlertStatus;
import org.eurekaj.manager.util.ClassPathUtil;
import org.eurekaj.spi.alert.EurekaJAlertPluginService;

public class ManagerAlertPluginService {
	private static final Logger log = Logger.getLogger(ManagerAlertPluginService.class);
	private static ManagerAlertPluginService pluginService = null;
    private ServiceLoader<EurekaJAlertPluginService> loader;
    
	public ManagerAlertPluginService() {
        ClassPathUtil.addPluginDirectory();
		loader = ServiceLoader.load(EurekaJAlertPluginService.class);
		for (EurekaJAlertPluginService alertPlugin : loader) {
			alertPlugin.setApplicationServices(EurekaJManagerApplicationServices.getInstance());
		}
	}
	
	public static ManagerAlertPluginService getInstance() {
		if (pluginService == null) {
			pluginService = new ManagerAlertPluginService();
		}
		
		return pluginService;
	}
	
	public void sendAlert(String pluginName, List<String> recipients, Alert alert, AlertStatus oldStatus, double currValue, String timeString) {
		log.debug("Finding an alert plugin to send the alert");
		
		for (EurekaJAlertPluginService pluginService : loader) {
			if (pluginService.getAlertPluginName().equals(pluginName)) {
				log.debug("Sending alert through plugin: " + pluginService.getAlertPluginName());
				pluginService.getAlertService().sendAlert(recipients, alert, oldStatus, currValue, timeString);
			}
		}
	}
	
	public List<String> getLoadedAlertPlugins() {
		log.debug("Returning a list of all loaded alert plugins");
		
		List<String> pluginList = new ArrayList<String>();
		
		for (EurekaJAlertPluginService pluginService : loader) {			
			log.debug("Loaded Plugin: " + pluginService.getAlertPluginName());
			pluginService.getAlertService().configure();
			pluginList.add(pluginService.getAlertPluginName());
		} 
		
		return pluginList;
	}
}
