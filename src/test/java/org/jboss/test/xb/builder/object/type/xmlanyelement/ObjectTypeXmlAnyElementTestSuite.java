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
package org.jboss.test.xb.builder.object.type.xmlanyelement;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.xb.builder.object.type.xmlanyelement.test.ArrayElementWildcardUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.ArrayObjectWildcardUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.ElementPropertiesAndWildcardUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.ElementWildcardUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.ElementWildcardWithCollectionPropertyUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.ListElementWildcardUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.ListObjectWildcardUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.NotAnnotatedElementWildcardUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.SimpleObjectWildcardUnitTestCase;
import org.jboss.test.xb.builder.object.type.xmlanyelement.test.XmlElementsAndXmlAnyElementUnitTestCase;

/**
 * ObjectTypeXmlAnyElementTestSuite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ObjectTypeXmlAnyElementTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Object Type XmlAnyElement Tests");

      suite.addTest(SimpleObjectWildcardUnitTestCase.suite());
      suite.addTest(ArrayObjectWildcardUnitTestCase.suite());
      suite.addTest(ListObjectWildcardUnitTestCase.suite());
      suite.addTest(ElementWildcardUnitTestCase.suite());
      suite.addTest(ArrayElementWildcardUnitTestCase.suite());
      suite.addTest(ListElementWildcardUnitTestCase.suite());
      suite.addTest(ElementPropertiesAndWildcardUnitTestCase.suite());
      suite.addTest(ElementWildcardWithCollectionPropertyUnitTestCase.suite());
      suite.addTest(NotAnnotatedElementWildcardUnitTestCase.suite());
      suite.addTest(XmlElementsAndXmlAnyElementUnitTestCase.suite());
      
      return suite;
   }
}
