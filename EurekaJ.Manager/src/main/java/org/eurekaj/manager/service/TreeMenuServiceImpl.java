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
package org.eurekaj.manager.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eurekaj.api.datatypes.*;
import org.eurekaj.api.enumtypes.UnitType;
import org.eurekaj.api.enumtypes.ValueType;
import org.eurekaj.manager.plugin.ManagerDbPluginService;
import org.eurekaj.manager.util.DatabasePluginUtil;
import org.eurekaj.spi.db.EurekaJDBPluginService;

public class TreeMenuServiceImpl implements TreeMenuService {
	private static final Logger log = Logger.getLogger(TreeMenuServiceImpl.class);
    EurekaJDBPluginService dbPlugin = null;

    public TreeMenuServiceImpl() {

    }

    private EurekaJDBPluginService getDbPlugin() {

        if (dbPlugin == null) {
            dbPlugin = ManagerDbPluginService.getInstance().getPluginServiceWithName(DatabasePluginUtil.getDatabasePluginName());
        }

        return dbPlugin;
    }

    public void storeIncomingStatistics(String guiPath, Long timeperiod, String value, ValueType valueType, UnitType unitType) {
		getDbPlugin().getLiveStatissticsDao().storeIncomingStatistics(guiPath, timeperiod, value, valueType, unitType);
	}
	
	public List<TreeMenuNode> getTreeMenu() {
		return getDbPlugin().getTreeMenuDao().getTreeMenu();
	}

	public TreeMenuNode getTreeMenu(String guiPath) {
		return getDbPlugin().getTreeMenuDao().getTreeMenu(guiPath);
	}

	public List<LiveStatistics> getLiveStatistics(String guiPath,
                                                  Long minTimeperiod, Long maxTimeperiod) {
		return getDbPlugin().getLiveStatissticsDao().getLiveStatistics(guiPath, minTimeperiod, maxTimeperiod);
	}

	public void persistGroupInstrumentation(GroupedStatistics groupedStatistics) {
		getDbPlugin().getGroupedStatisticsDao().persistGroupInstrumentation(groupedStatistics);
	}
	
	public GroupedStatistics getGroupedStatistics(String name) {
		return getDbPlugin().getGroupedStatisticsDao().getGroupedStatistics(name);
	}
	
	public List<GroupedStatistics> getGroupedStatistics() {
		return getDbPlugin().getGroupedStatisticsDao().getGroupedStatistics();
	}
	
	public void deleteChartGroup(String groupName) {
		getDbPlugin().getGroupedStatisticsDao().deleteGroupedChart(groupName);
	}

	public Alert getAlert(String alertName) {
		return getDbPlugin().getAlertDao().getAlert(alertName);
	}

	public void persistAlert(Alert alert) {
		getDbPlugin().getAlertDao().persistAlert(alert);
	}
	
	public List<Alert> getAlerts() {
		return getDbPlugin().getAlertDao().getAlerts();
	}
	
	public void deleteAlert(String alertName) {
		getDbPlugin().getAlertDao().deleteAlert(alertName);
	}

    public void persistTriggeredAlert(TriggeredAlert triggeredAlert) {
        getDbPlugin().getAlertDao().persistTriggeredAlert(triggeredAlert);
    }

    public List<TriggeredAlert> getTriggeredAlerts(Long fromTimeperiod, Long toTimeperiod) {
        return getDbPlugin().getAlertDao().getTriggeredAlerts(fromTimeperiod, toTimeperiod);
    }

    public List<TriggeredAlert> getTriggeredAlerts(String alertname, Long fromTimeperiod, Long toTimeperiod) {
        return getDbPlugin().getAlertDao().getTriggeredAlerts(alertname, fromTimeperiod, toTimeperiod);
    }

    public List<TriggeredAlert> getRecentTriggeredAlerts(int numAlerts) {
        return getDbPlugin().getAlertDao().getRecentTriggeredAlerts(numAlerts);
    }

    public void deleteOldLiveStatistics(Date date) {
        log.debug("dbplugin: " + getDbPlugin());

        getDbPlugin().getLiveStatissticsDao().deleteLiveStatisticsOlderThan(date);
    }
    
    public void deleteTreeMenuNode(String guiPath) {
    	getDbPlugin().getTreeMenuDao().deleteTreeMenu(guiPath);    	
    }
}
