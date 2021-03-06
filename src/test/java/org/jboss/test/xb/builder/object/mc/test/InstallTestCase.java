/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.xb.builder.object.mc.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.Test;

import org.jboss.test.xb.builder.object.mc.support.Annotation1;
import org.jboss.test.xb.builder.object.mc.support.Annotation2;
import org.jboss.test.xb.builder.object.mc.support.Annotation3;
import org.jboss.test.xb.builder.object.mc.support.model.AbstractBeanMetaData;
import org.jboss.test.xb.builder.object.mc.support.model.ControllerState;
import org.jboss.test.xb.builder.object.mc.support.model.InstallMetaData;

/**
 * InstallTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 46386 $
 */
public class InstallTestCase extends AbstractMCTest
{
   protected InstallMetaData getInstall() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      List<?> installs = bean.getInstalls();
      assertNotNull(installs);
      assertEquals(1, installs.size());
      InstallMetaData install = (InstallMetaData) installs.get(0);
      assertNotNull(install);
      return install;
   }

   public void testInstallWithBean() throws Exception
   {
      InstallMetaData install = getInstall();
      assertEquals("Bean", install.getBean());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithState() throws Exception
   {
      InstallMetaData install = getInstall();
      assertNull(install.getBean());
      assertEquals(ControllerState.CONFIGURED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithMethod() throws Exception
   {
      InstallMetaData install = getInstall();
      assertNull(install.getBean());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Method", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithAnnotation() throws Exception
   {
      InstallMetaData install = getInstall();
      assertNull(install.getBean());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add("@" + Annotation1.class.getName());
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithAnnotations() throws Exception
   {
      InstallMetaData install = getInstall();
      assertNull(install.getBean());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add("@" + Annotation1.class.getName());
      expected.add("@" + Annotation2.class.getName());
      expected.add("@" + Annotation3.class.getName());
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithParameter() throws Exception
   {
      InstallMetaData install = getInstall();
      assertNull(install.getBean());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Parameter1");
      assertParameters(expected, install.getParameters());
   }

   public void testInstallWithParameters() throws Exception
   {
      InstallMetaData install = getInstall();
      assertNull(install.getBean());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Parameter1");
      expected.add("Parameter2");
      expected.add("Parameter3");
      assertParameters(expected, install.getParameters());
   }

   /* TODO
   public void testInstallBadNoMethod() throws Exception
   {
      try
      {
         unmarshalBean("InstallBadNoMethod.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   public static Test suite()
   {
      return suite(InstallTestCase.class);
   }

   public InstallTestCase(String name)
   {
      super(name);
   }
}
