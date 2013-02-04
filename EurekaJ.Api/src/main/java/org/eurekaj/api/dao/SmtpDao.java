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
package org.eurekaj.api.dao;

import org.eurekaj.api.datatypes.EmailRecipientGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 5/5/11
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SmtpDao {

    public List<EmailRecipientGroup> getEmailRecipientGroups();

	public EmailRecipientGroup getEmailRecipientGroup(String groupName, String accountName);

	public void persistEmailRecipientGroup(EmailRecipientGroup emailRecipientGroup);

	public void deleteEmailRecipientGroup(EmailRecipientGroup emailRecipientGroup);
	
	public void deleteEmailRecipientGroup(String groupName, String accountName);
}
