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

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.jboss.reflect.spi.TypeInfo;

/**
 * String value.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 59990 $
 */
@XmlType
//@JBossXmlNoElements
public class StringValueMetaData extends AbstractTypeMetaData
{
   private static final long serialVersionUID = 1L;

   /**
    * Do we replace String with System property,
    * by default it is true.
    */
   private boolean replace = true;

   /**
    * Create a new string value
    */
   public StringValueMetaData()
   {
   }

   /**
    * Create a new string value
    * 
    * @param value the value
    */
   public StringValueMetaData(String value)
   {
      super(value);
   }

   @Override
   public String getValue()
   {
      return (String) super.getValue();
   }

   /**
    * Set the value
    * 
    * @param value the value
    */
   @XmlValue
   public void setValue(String value)
   {
      super.setValue(value);
   }

   public void setValue(Object value)
   {
      if (value != null && value instanceof String == false)
         throw new ClassCastException("value is not a String: " + value);
      super.setValue(value);
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      return null;
   }

   public boolean isReplace()
   {
      return replace;
   }

   @XmlTransient
   public void setReplace(boolean replace)
   {
      this.replace = replace;
   }
}
