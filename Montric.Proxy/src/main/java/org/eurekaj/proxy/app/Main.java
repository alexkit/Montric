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
package org.eurekaj.proxy.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.apache.http.examples.client.ClientGZipContentCompression;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eurekaj.proxy.FileMatcher;
import org.eurekaj.proxy.http.handlers.PlainTextProtocolHandler;
import org.eurekaj.proxy.http.handlers.PlainTextThread;
import org.eurekaj.proxy.parser.ParseStatistics;

public class Main {
	private static final Logger log = Logger.getLogger(Main.class);
	
    private String scriptPath;
    private String endpointUrl;
    private String username;
    private String accessToken;
    private ClientGZipContentCompression gzipClient;

    public static void main(String[] args) {
        Main main = new Main();
    }

    public Main() {
        try {
            readProperties();
        } catch (IOException e) {
            log.error("Unable to read required properties file 'config.properties'.");
            throw new RuntimeException("Required properties file missing: 'config.properties'");
        }

        gzipClient = new ClientGZipContentCompression(endpointUrl, username, accessToken);
     
        String enablePTP = System.getProperty("org.eurekaj.proxy.enablePTP", "true");
        log.info("org.eurekaj.proxy.enablePTP: " + enablePTP);
        if (enablePTP.equalsIgnoreCase("true")) {
        	setupHttpServer();
        }
        
    	parseAndSendBtraceScripts(scriptPath);
    }
    
    private void setupHttpServer() {
    	PlainTextThread ptt = new PlainTextThread(gzipClient);
    	new Thread(ptt).start();
    	
    }

    private void parseAndSendBtraceScripts(String scriptPath) {
        while (true) {
            try {
                List<File> scriptOutputfileList = FileMatcher.getScriptOutputFilesInDirectory(scriptPath);

                for (File scriptOutputfile : scriptOutputfileList) {
                	long kilobytes = scriptOutputfile.length() / 1024;
                	
                	if (kilobytes > 256) {
                		log.info("Montric Proxy cannot read files larger than 256 KiB. Skipping: " + scriptOutputfile.getAbsolutePath());
                		continue;
                	}
                	
                    String json = ParseStatistics.parseBtraceFile(scriptOutputfile, accessToken);
                    log.debug("Attempting to send JSON contents of: " + scriptOutputfile.getName() + " length: " + json.length());

                    int statusCode = gzipClient.sendGzipOverHttp(json);
                    if (statusCode != 200) {
                    	log.debug("Unable to send JSON data. Server returned status code: " + statusCode + ". Please verify your endpoint, username and password in your 'config.properties' file");
                    } else {
                        scriptOutputfile.delete();
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readProperties() throws IOException {
        Properties properties = new Properties();
		File configFile = new File("config.properties");
		if (!configFile.exists()) {
			log.debug("config.properties not found at : " + configFile.getAbsolutePath() + " trying one level up.");
			configFile = new File("../config.properties");
		}
		if (!configFile.exists()) {
			log.debug("config.properties not found at : " + configFile.getAbsolutePath() + " trying one level up.");
			configFile = new File("../../config.properties");
		}
		if (configFile.exists()) {
			FileInputStream configStream = new FileInputStream(configFile);
			properties.load(configStream);
			configStream.close();
			log.debug("Server properties loaded from " + configFile.getAbsolutePath());
			for (Enumeration<Object> e = properties.keys(); e.hasMoreElements();) {
				Object property = (String) e.nextElement();
				log.debug("\t\t* " + property + "=" + properties.get(property));
			}
		} else {
			String message = "Could not find " + configFile.getAbsolutePath() + ". Unable to start.";
			log.error(message);
			throw new RuntimeException(message);
		}

		setProperties(properties);
    }

    private void setProperties(Properties properties) {
        scriptPath = (String)properties.get("montric.proxy.scriptpath");
        endpointUrl = (String)properties.get("montric.proxy.endpoint");
        accessToken = (String)properties.get("montric.proxy.accessToken");

        if (scriptPath == null || scriptPath.length() == 1) {
            throw new RuntimeException("The properties file 'config.properties' requires that the property 'montric.proxy.scriptpath' is defined. Example: montric.proxy.scriptpath=/path/to/btrace/scripts");
        }

        if (endpointUrl == null || endpointUrl.length() == 1) {
            throw new RuntimeException("The properties file 'config.properties' requires that the property 'montric.proxy.endpoint' is defined. Example: montric.proxy.endpoint=http://hostname:port");
        }

        if (accessToken == null || accessToken.length() == 1) {
            throw new RuntimeException("The properties file 'config.properties' requires that the property 'montric.proxy.accessToken' is defined. Example: montric.proxy.accessToken=password");
        }

	}
}
