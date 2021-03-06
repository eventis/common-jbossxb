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

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;
import org.jboss.xb.annotations.JBossXmlAttribute;

/**
 * Dependency value.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 62474 $
 */
@XmlType
public class AbstractDependencyValueMetaData extends AbstractValueMetaData
{
   private static final long serialVersionUID = 2L;
   
   /** The property name */
   protected String property;

   /** The when required state of the dependency or null to use current context state */
   protected ControllerState whenRequiredState;

   /** The required state of the dependency or null to look in the registry */
   protected ControllerState dependentState = ControllerState.INSTALLED;

   /**
    * Create a new dependency value
    */
   public AbstractDependencyValueMetaData()
   {
   }

   /**
    * Create a new dependency value
    * 
    * @param value the value
    */
   public AbstractDependencyValueMetaData(Object value)
   {
      super(value);
   }

   /**
    * Create a new dependency value
    * 
    * @param value the value
    * @param property the property
    */
   public AbstractDependencyValueMetaData(Object value, String property)
   {
      super(value);
      this.property = property;
   }

   /**
    * Get the property
    * 
    * @return the property
    */
   public String getProperty()
   {
      return property;
   }
   
   /**
    * Set the property
    * 
    * @param property the property name
    */
   @XmlAttribute
   public void setProperty(String property)
   {
      this.property = property;
   }
   
   /**
    * Set the when required state of the dependency
    * 
    * @param whenRequiredState the when required state or null if it uses current context state
    */
   public void setWhenRequiredState(ControllerState whenRequiredState)
   {
      this.whenRequiredState = whenRequiredState;
      flushJBossObjectCache();
   }

   public ControllerState getWhenRequiredState()
   {
      return whenRequiredState;
   }
   
   /**
    * Set the required state of the dependency
    *
    * @param dependentState the required state or null if it must be in the registry
    */
   @XmlAttribute(name="state")
   public void setDependentState(ControllerState dependentState)
   {
      this.dependentState = dependentState;
      flushJBossObjectCache();
   }

   public ControllerState getDependentState()
   {
      return dependentState;
   }

   @XmlAttribute(name="bean")
   @JBossXmlAttribute(type=String.class)
   public void setValue(Object value)
   {
      super.setValue(value);
   }

   @XmlAnyElement
   public void setValueObject(Object value)
   {
      if (value == null)
         setValue(null);
      else if (value instanceof ValueMetaData)
         setValue(value);
      else
         setValue(new AbstractValueMetaData(value));
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      return null;
   }

   protected boolean addDependencyItem()
   {
      return true;
   }
      
   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (property != null)
         buffer.append(" property=").append(property);
      if (whenRequiredState != null)
         buffer.append(" whenRequiredState=").append(whenRequiredState.getStateString());
      if (dependentState != null)
         buffer.append(" dependentState=").append(dependentState.getStateString());
   }
}
