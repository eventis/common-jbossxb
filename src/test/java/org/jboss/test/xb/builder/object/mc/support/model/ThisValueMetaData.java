/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.test.xb.builder.object.mc.support.model;

import javax.xml.bind.annotation.XmlType;

import org.jboss.reflect.spi.TypeInfo;
import org.jboss.xb.annotations.JBossXmlNoElements;

/**
 * Dependency value.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 60562 $
 */
@XmlType
@JBossXmlNoElements
public class ThisValueMetaData extends AbstractValueMetaData
{
   private static final long serialVersionUID = 2L;

   /**
    * Create a new dependency value
    */
   public ThisValueMetaData()
   {
   }
   
   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      return null;
   }
}
