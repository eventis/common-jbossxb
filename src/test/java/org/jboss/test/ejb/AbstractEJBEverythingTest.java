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
package org.jboss.test.ejb;

import java.util.Iterator;
import java.util.Set;

import junit.framework.AssertionFailedError;

import org.jboss.ejb.metadata.jboss.CacheInvalidationConfigMetaData;
import org.jboss.ejb.metadata.jboss.ClusterConfigMetaData;
import org.jboss.ejb.metadata.jboss.IORASContextMetaData;
import org.jboss.ejb.metadata.jboss.IORSASContextMetaData;
import org.jboss.ejb.metadata.jboss.IORSecurityConfigMetaData;
import org.jboss.ejb.metadata.jboss.IORTransportConfigMetaData;
import org.jboss.ejb.metadata.jboss.MethodAttributeMetaData;
import org.jboss.ejb.metadata.jboss.MethodAttributesMetaData;
import org.jboss.ejb.metadata.jboss.RemoteBindingMetaData;
import org.jboss.ejb.metadata.spec.ActivationConfigMetaData;
import org.jboss.ejb.metadata.spec.ActivationConfigPropertiesMetaData;
import org.jboss.ejb.metadata.spec.ActivationConfigPropertyMetaData;
import org.jboss.ejb.metadata.spec.AssemblyDescriptorMetaData;
import org.jboss.ejb.metadata.spec.CMRFieldMetaData;
import org.jboss.ejb.metadata.spec.ContainerTransactionMetaData;
import org.jboss.ejb.metadata.spec.ContainerTransactionsMetaData;
import org.jboss.ejb.metadata.spec.EjbJarMetaData;
import org.jboss.ejb.metadata.spec.EnterpriseBeanMetaData;
import org.jboss.ejb.metadata.spec.EnterpriseBeansMetaData;
import org.jboss.ejb.metadata.spec.EntityBeanMetaData;
import org.jboss.ejb.metadata.spec.ExcludeListMetaData;
import org.jboss.ejb.metadata.spec.MessageDrivenBeanMetaData;
import org.jboss.ejb.metadata.spec.MethodInterfaceType;
import org.jboss.ejb.metadata.spec.MethodMetaData;
import org.jboss.ejb.metadata.spec.MethodParametersMetaData;
import org.jboss.ejb.metadata.spec.MethodPermissionMetaData;
import org.jboss.ejb.metadata.spec.MethodPermissionsMetaData;
import org.jboss.ejb.metadata.spec.MethodsMetaData;
import org.jboss.ejb.metadata.spec.MultiplicityType;
import org.jboss.ejb.metadata.spec.PersistenceType;
import org.jboss.ejb.metadata.spec.RelationMetaData;
import org.jboss.ejb.metadata.spec.RelationRoleMetaData;
import org.jboss.ejb.metadata.spec.RelationRoleSourceMetaData;
import org.jboss.ejb.metadata.spec.RelationsMetaData;
import org.jboss.ejb.metadata.spec.SecurityIdentityMetaData;
import org.jboss.ejb.metadata.spec.SessionBeanMetaData;
import org.jboss.ejb.metadata.spec.SessionType;
import org.jboss.ejb.metadata.spec.TransAttributeType;
import org.jboss.ejb.metadata.spec.TransactionType;
import org.jboss.javaee.metadata.jboss.IgnoreDependencyMetaData;
import org.jboss.javaee.metadata.spec.RunAsMetaData;
import org.jboss.javaee.metadata.spec.SecurityRoleRefMetaData;
import org.jboss.javaee.metadata.spec.SecurityRoleRefsMetaData;
//import org.jboss.metadata.ApplicationMetaData;
//import org.jboss.metadata.BeanMetaData;
//import org.jboss.metadata.EntityMetaData;
//import org.jboss.metadata.IorSecurityConfigMetaData;
//import org.jboss.metadata.MessageDrivenMetaData;
//import org.jboss.metadata.MetaData;
//import org.jboss.metadata.RelationshipRoleMetaData;
//import org.jboss.metadata.SessionMetaData;
import org.jboss.test.javaee.AbstractJavaEEEverythingTest;
import org.jboss.util.NotImplementedException;

/**
 * AbstractEJBEverythingTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractEJBEverythingTest extends AbstractJavaEEEverythingTest
{
   public AbstractEJBEverythingTest(String name)
   {
      super(name);
   }

   protected void assertEnterpriseBeans(EjbJarMetaData ejbJarMetaData)
   {
      EnterpriseBeansMetaData enterpriseBeansMetaData = ejbJarMetaData.getEnterpriseBeans();
      assertNotNull(enterpriseBeansMetaData);
      assertId("enterprise-beans", enterpriseBeansMetaData);
      assertEquals(15, enterpriseBeansMetaData.size());

      assertNullSession("session0", enterpriseBeansMetaData);
      assertFullSession("session1", enterpriseBeansMetaData);
      assertFullSession("session2", enterpriseBeansMetaData);
      SessionBeanMetaData session = assertSession("session3EjbName", enterpriseBeansMetaData);
      assertEquals(SessionType.Stateful, session.getSessionType());
      session = assertSession("session4EjbName", enterpriseBeansMetaData);
      assertEquals(TransactionType.Bean, session.getTransactionType());
      session = assertSession("session5EjbName", enterpriseBeansMetaData);
      SecurityIdentityMetaData securityIdentityMetaData = session.getSecurityIdentity();
      assertNotNull(securityIdentityMetaData);
      assertFalse(securityIdentityMetaData.isUseCallerId());
      
      assertNullEntity("entity0", enterpriseBeansMetaData);
      assertFullEntity("entity1", enterpriseBeansMetaData);
      assertFullEntity("entity2", enterpriseBeansMetaData);
      EntityBeanMetaData entity = assertEntity("entity3EjbName", enterpriseBeansMetaData);
      assertEquals(PersistenceType.Bean, entity.getPersistenceType());
      entity = assertEntity("entity4EjbName", enterpriseBeansMetaData);
      assertFalse(entity.isReentrant());
      entity = assertEntity("entity5EjbName", enterpriseBeansMetaData);
      assertEquals("1.x", entity.getCmpVersion());
      assertTrue(entity.isCMP1x());
      
      assertNullMDB("mdb0", enterpriseBeansMetaData);
      assertFullMDB("mdb1", enterpriseBeansMetaData);
      assertFullMDB("mdb2", enterpriseBeansMetaData);
   }

/*   protected void assertEnterpriseBeans(ApplicationMetaData applicationMetaData)
   {
      Iterator<BeanMetaData> i = applicationMetaData.getEnterpriseBeans();
      assertNotNull(i);
      int count = 0;
      while (i.hasNext())
      {
         i.next();
         ++count;
      }
      
      assertEquals(15, count);

      assertNullSession("session0", applicationMetaData);
      assertFullSession("session1", applicationMetaData);
      assertFullSession("session2", applicationMetaData);
      SessionMetaData session = assertSession("session3", applicationMetaData);
      assertTrue(session.isStateful());
      assertFalse(session.isStateless());
      session = assertSession("session4", applicationMetaData);
      assertTrue(session.isBeanManagedTx());
      assertFalse(session.isContainerManagedTx());
      session = assertSession("session5", applicationMetaData);
      org.jboss.metadata.SecurityIdentityMetaData securityIdentityMetaData = session.getSecurityIdentityMetaData();
      assertNotNull(securityIdentityMetaData);
      assertFalse(securityIdentityMetaData.getUseCallerIdentity());
      
      assertNullEntity("entity0", applicationMetaData);
      assertFullEntity("entity1", applicationMetaData);
      assertFullEntity("entity2", applicationMetaData);
      EntityMetaData entity = assertEntity("entity3", applicationMetaData);
      assertTrue(entity.isBMP());
      entity = assertEntity("entity4", applicationMetaData);
      assertFalse(entity.isReentrant());
      entity = assertEntity("entity5", applicationMetaData);
      assertTrue(entity.isCMP1x());
      assertTrue(entity.isCMP1x());
      
      assertNullMDB("mdb0", applicationMetaData);
      assertFullMDB("mdb1", applicationMetaData);
      assertFullMDB("mdb2", applicationMetaData);
   }
*/   
   protected SessionBeanMetaData assertFullSession(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      throw new NotImplementedException();
   }
   
   private SessionBeanMetaData assertNullSession(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      SessionBeanMetaData session = assertSession(ejbName + "EjbName", enterpriseBeansMetaData);
      assertNull(session.getId());
      assertNull(session.getMappedName());
      assertNull(session.getHome());
      assertNull(session.getRemote());
      assertNull(session.getLocalHome());
      assertNull(session.getLocal());
      assertNull(session.getBusinessLocals());
      assertNull(session.getBusinessRemotes());
      assertNull(session.getServiceEndpoint());
      assertNull(session.getEjbClass());
      assertNull(session.getSessionType());
      assertNull(session.getTimeoutMethod());
      assertNull(session.getInitMethods());
      assertNull(session.getRemoveMethods());
      assertNull(session.getTransactionType());
      assertNull(session.getAroundInvokes());
      assertNull(session.getPostActivates());
      assertNull(session.getPrePassivates());
      assertNull(session.getJndiEnvironmentRefsGroup());
      assertNull(session.getContainerTransactions());
      assertNull(session.getMethodPermissions());
      assertNull(session.getExcludeList());
      assertNull(session.getSecurityRoleRefs());
      assertNull(session.getSecurityIdentity());
      
      return session;
   }

   protected SessionBeanMetaData assertSession(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      SessionBeanMetaData session = assertEnterpriseBean(ejbName, enterpriseBeansMetaData, SessionBeanMetaData.class);
      assertTrue(session.isSession());
      assertFalse(session.isEntity());
      assertFalse(session.isMessageDriven());
      return session;
   }

/*   private SessionMetaData assertFullSession(String ejbName, ApplicationMetaData applicationMetaData)
   {
      SessionMetaData session = assertSession(ejbName, applicationMetaData);
      assertClass(ejbName, "Home", session.getHome());
      assertClass(ejbName, "Remote", session.getRemote());
      assertClass(ejbName, "LocalHome", session.getLocalHome());
      assertClass(ejbName, "Local", session.getLocal());
      assertClass(ejbName, "ServiceEndpoint", session.getServiceEndpoint());
      assertClass(ejbName, "EjbClass", session.getEjbClass());
      assertTrue(session.isStateless());
      assertTrue(session.isContainerManagedTx());
      assertEnvironment(ejbName, session, true);
      assertMethodAttributes(ejbName, session);
      assertContainerTransactions(session.getTransactionMethods());
      assertMethodPermissions(7, session);
      assertExcludedMethods(ejbName, session.getExcludedMethods());
      assertSecurityRoleRefs(ejbName, 2, session.getSecurityRoleReferences());
      assertSecurityIdentity(ejbName, "SecurityIdentity", session.getSecurityIdentityMetaData(), true);
      
      return session;
   }
*/
/*   private SessionMetaData assertNullSession(String ejbName, ApplicationMetaData applicationMetaData)
   {
      SessionMetaData session = assertSession(ejbName, applicationMetaData);
      assertNull(session.getHome());
      assertNull(session.getRemote());
      assertNull(session.getLocalHome());
      assertNull(session.getLocal());
      assertNull(session.getServiceEndpoint());
      assertNull(session.getEjbClass());
      assertTrue(session.isStateless());
      assertTrue(session.isContainerManagedTx());
      assertNullEnvironment(session);
      assertDefaultMethodAttributes(ejbName, session);
      assertEmptyIterator(session.getTransactionMethods());
      assertEmptyIterator(session.getPermissionMethods());
      assertEmptyIterator(session.getExcludedMethods());
      assertEmptyIterator(session.getSecurityRoleReferences());
      assertNull(session.getSecurityIdentityMetaData());
      
      return session;
   }
*/   
/*   private SessionMetaData assertSession(String ejbName, ApplicationMetaData applicationMetaData)
   {
      SessionMetaData session = assertEnterpriseBean(ejbName, applicationMetaData, SessionMetaData.class);
      assertTrue(session.isSession());
      assertFalse(session.isEntity());
      assertFalse(session.isMessageDriven());
      return session;
   }
*/
   protected EntityBeanMetaData assertFullEntity(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      throw new NotImplementedException();
   }

   private EntityBeanMetaData assertNullEntity(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      EntityBeanMetaData entity = assertEntity(ejbName + "EjbName", enterpriseBeansMetaData);
      assertNullEntity(entity);      
      return entity;
   }

   public void assertNullEntity(EntityBeanMetaData entity)
   {
      assertNull(entity.getId());
      assertNull(entity.getMappedName());
      assertNull(entity.getHome());
      assertNull(entity.getRemote());
      assertNull(entity.getLocalHome());
      assertNull(entity.getLocal());
      assertNull(entity.getEjbClass());
      assertNull(entity.getPersistenceType());
      assertNull(entity.getPrimKeyClass());
      assertFalse(entity.isReentrant());
      assertNull(entity.getCmpVersion());
      assertFalse(entity.isCMP1x());
      assertNull(entity.getAbstractSchemaName());
      assertNull(entity.getCmpFields());
      assertNull(entity.getPrimKeyField());
      assertNull(entity.getJndiEnvironmentRefsGroup());
      assertNull(entity.getContainerTransactions());
      assertNull(entity.getMethodPermissions());
      assertNull(entity.getExcludeList());
      assertNull(entity.getSecurityRoleRefs());
      assertNull(entity.getSecurityIdentity());
      assertNull(entity.getQueries());
   }
   
   protected EntityBeanMetaData assertEntity(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      EntityBeanMetaData entity = assertEnterpriseBean(ejbName, enterpriseBeansMetaData, EntityBeanMetaData.class);
      assertFalse(entity.isSession());
      assertTrue(entity.isEntity());
      assertFalse(entity.isMessageDriven());
      return entity;
   }

/*   private EntityMetaData assertFullEntity(String ejbName, ApplicationMetaData applicationMetaData)
   {
      EntityMetaData entity = assertEntity(ejbName, applicationMetaData);
      assertEquals(ejbName + "Home", entity.getHome());
      assertEquals(ejbName + "Remote", entity.getRemote());
      assertEquals(ejbName + "LocalHome", entity.getLocalHome());
      assertEquals(ejbName + "Local",entity.getLocal());
      assertEquals(ejbName + "EjbClass", entity.getEjbClass());
      assertTrue(entity.isCMP());
      assertEquals(ejbName + "PrimKeyClass", entity.getPrimaryKeyClass());
      assertTrue(entity.isReentrant());
      assertTrue(entity.isCMP2x());
      assertFalse(entity.isCMP1x());
      assertEquals(ejbName + "AbstractSchemaName", entity.getAbstractSchemaName());
      assertCmpFields(ejbName, 2, entity.getCMPFields());
      assertEquals(ejbName + "PrimKeyField",entity.getPrimKeyField());
      assertEnvironment(ejbName, entity, true);
      assertMethodAttributes(ejbName, entity);
      assertMethodPermissions(7, entity);
      assertExcludedMethods(ejbName, entity.getExcludedMethods());
      assertSecurityRoleRefs(ejbName, 2, entity.getSecurityRoleReferences());
      assertSecurityIdentity(ejbName, "SecurityIdentity", entity.getSecurityIdentityMetaData(), true);
      assertQueries(ejbName, 2, entity.getQueries());
      
      return entity;
   }
*/
/*   private EntityMetaData assertNullEntity(String ejbName, ApplicationMetaData applicationMetaData)
   {
      EntityMetaData entity = assertEntity(ejbName, applicationMetaData);
      assertNull(entity.getHome());
      assertNull(entity.getRemote());
      assertNull(entity.getLocalHome());
      assertNull(entity.getLocal());
      assertNull(entity.getEjbClass());
      assertTrue(entity.isCMP());
      assertNull(entity.getPrimaryKeyClass());
      assertFalse(entity.isReentrant());
      assertTrue(entity.isCMP2x());
      assertFalse(entity.isCMP1x());
      assertNull(entity.getAbstractSchemaName());
      assertEmptyIterator(entity.getCMPFields());
      assertNull(entity.getPrimKeyField());
      assertNullEnvironment(entity);
      assertDefaultMethodAttributes(ejbName, entity);
      assertEmptyIterator(entity.getPermissionMethods());
      assertEmptyIterator(entity.getExcludedMethods());
      assertEmptyIterator(entity.getSecurityRoleReferences());
      assertNull(entity.getSecurityIdentityMetaData());
      assertEmptyIterator(entity.getQueries());
      
      return entity;
   }
*/   
/*   protected EntityMetaData assertEntity(String ejbName, ApplicationMetaData applicationMetaData)
   {
      EntityMetaData entity = assertEnterpriseBean(ejbName, applicationMetaData, EntityMetaData.class);
      assertFalse(entity.isSession());
      assertTrue(entity.isEntity());
      assertFalse(entity.isMessageDriven());
      return entity;
   }
*/
/*   private void assertCmpFields(String ejbName, int size, Iterator<String> cmpFieldsMetaData)
   {
      assertNotNull(cmpFieldsMetaData);
      int count = 1;
      while (cmpFieldsMetaData.hasNext())
      {
         assertEquals(ejbName + "CmpField" + count, cmpFieldsMetaData.next());
         ++count;
      }
      assertEquals(size + 1, count);
   }
*/
/*   private void assertQueries(String ejbName, int size, Iterator<org.jboss.metadata.QueryMetaData> queries)
   {
      assertNotNull(queries);
      int count = 1;
      while (queries.hasNext())
      {
         org.jboss.metadata.QueryMetaData query = queries.next();
         String prefix = ejbName + "Query" + count;
         assertEquals(prefix + "QueryMethod", query.getMethodName());
         assertMethodParams(prefix + "QueryMethod", 2, query.getMethodParams());
         if (count == 1)
            assertEquals("Local", query.getResultTypeMapping());
         else
            assertEquals("Remote", query.getResultTypeMapping());
         assertEquals(prefix + "EjbQL", query.getEjbQl());
         ++count;
      }
      assertEquals(size + 1, count);
   }
*/
   private void assertMethodParams(String prefix, int size, Iterator<String> params)
   {
      assertNotNull(params);
      int count = 1;
      while (params.hasNext())
      {
         assertEquals(prefix + "Param" + count, params.next());
         ++count;
      }
      assertEquals(size + 1, count);
   }

   private void assertMethodParams(String prefix, int size, String[] params)
   {
      assertNotNull(params);
      assertEquals(size, params.length);
      for (int count = 1; count <= params.length; ++count)
         assertEquals(prefix + "Param" + count, params[count-1]);
   }

   protected MessageDrivenBeanMetaData assertFullMDB(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      throw new NotImplementedException();
   }

   private MessageDrivenBeanMetaData assertNullMDB(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      MessageDrivenBeanMetaData mdb = assertMDB(ejbName + "EjbName", enterpriseBeansMetaData);

      assertNull(mdb.getId());
      assertNull(mdb.getMappedName());
      assertNull(mdb.getEjbClass());
      assertNull(mdb.getMessagingType());
      assertNull(mdb.getTimeoutMethod());
      assertNull(mdb.getTransactionType());
      assertNull(mdb.getMessageDestinationType());
      assertNull(mdb.getMessageDestinationLink());
      assertNull(mdb.getActivationConfig());
      assertNull(mdb.getAroundInvokes());
      assertNull(mdb.getJndiEnvironmentRefsGroup());
      assertNull(mdb.getContainerTransactions());
      assertNull(mdb.getMethodPermissions());
      assertNull(mdb.getExcludeList());
      assertNull(mdb.getSecurityIdentity());
      
      return mdb;
   }
   
   protected MessageDrivenBeanMetaData assertMDB(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData)
   {
      MessageDrivenBeanMetaData mdb = assertEnterpriseBean(ejbName, enterpriseBeansMetaData, MessageDrivenBeanMetaData.class);
      assertFalse(mdb.isSession());
      assertFalse(mdb.isEntity());
      assertTrue(mdb.isMessageDriven());
      return mdb;
   }

/*   protected MessageDrivenMetaData assertFullMDB(String ejbName, ApplicationMetaData applicationMetaData)
   {
      MessageDrivenMetaData mdb = assertMDB(ejbName, applicationMetaData);

      assertEquals(ejbName + "EjbClass", mdb.getEjbClass());
      assertEquals(ejbName + "MessagingType", mdb.getMessagingType());
      assertTrue(mdb.isContainerManagedTx());
      assertEquals(ejbName + "MessageDestinationType", mdb.getDestinationType());
      assertEquals(ejbName + "MessageDestinationLink", mdb.getDestinationLink());
      assertActivationConfigProperties(ejbName, 2, mdb.getActivationConfigProperties());
      assertEnvironment(ejbName, mdb, true);
      assertMethodAttributes(ejbName, mdb);
      assertMethodPermissions(7, mdb);
      assertExcludedMethods(ejbName, mdb.getExcludedMethods());
      assertSecurityIdentity(ejbName, "SecurityIdentity", mdb.getSecurityIdentityMetaData(), true);
      
      return mdb;
   }
*/
/*   private MessageDrivenMetaData assertNullMDB(String ejbName, ApplicationMetaData applicationMetaData)
   {
      MessageDrivenMetaData mdb = assertMDB(ejbName, applicationMetaData);

      assertNull(mdb.getEjbClass());
      assertNull(mdb.getMessagingType());
      assertTrue(mdb.isContainerManagedTx());
      assertNull(mdb.getDestinationType());
      assertNull(mdb.getDestinationLink());
      assertEquals(0, mdb.getActivationConfigProperties().size());
      assertNullEnvironment(mdb);
      assertDefaultMethodAttributes(ejbName, mdb);
      assertEmptyIterator(mdb.getPermissionMethods());
      assertEmptyIterator(mdb.getExcludedMethods());
      assertNull(mdb.getSecurityIdentityMetaData());
      
      return mdb;
   }
*/
   protected void assertActivationConfig(String prefix, ActivationConfigMetaData activationConfigMetaData)
   {
      assertNotNull(activationConfigMetaData);
      assertId(prefix + "ActivationConfig", activationConfigMetaData);
      assertActivationConfigProperties(prefix, 2, activationConfigMetaData.getActivationConfigProperties());
   }

   private void assertActivationConfigProperties(String prefix, int size, ActivationConfigPropertiesMetaData activationConfigPropertiesMetaData)
   {
      assertNotNull(activationConfigPropertiesMetaData);
      assertEquals(size, activationConfigPropertiesMetaData.size());
      int count = 1;
      for (ActivationConfigPropertyMetaData property : activationConfigPropertiesMetaData)
      {
         assertId(prefix + "ActivationConfigProperty" + count, property);
         assertEquals(prefix + "ActivationConfigProperty" + count + "Name", property.getActivationConfigPropertyName());
         assertEquals(prefix + "ActivationConfigProperty" + count + "Value", property.getValue());
         ++count;
      }
   }

/*   private void assertActivationConfigProperties(String ejbName, int size, Map<String, org.jboss.metadata.ActivationConfigPropertyMetaData> activationConfigPropertiesMetaData)
   {
      assertNotNull(activationConfigPropertiesMetaData);
      assertEquals(size, activationConfigPropertiesMetaData.size());
      int count = 1;
      for (Entry<String, org.jboss.metadata.ActivationConfigPropertyMetaData> property : activationConfigPropertiesMetaData.entrySet())
      {
         String prefix = ejbName + "ActivationConfigProperty" + count;
         assertEquals(prefix + "Name", property.getKey());
         assertEquals(prefix + "Name", property.getValue().getName());
         assertEquals(prefix + "Value", property.getValue().getValue());
         ++count;
      }
   }
*/
/*   protected MessageDrivenMetaData assertMDB(String ejbName, ApplicationMetaData applicationMetaData)
   {
      MessageDrivenMetaData mdb = assertEnterpriseBean(ejbName, applicationMetaData, MessageDrivenMetaData.class);
      assertFalse(mdb.isSession());
      assertFalse(mdb.isEntity());
      assertTrue(mdb.isMessageDriven());
      return mdb;
   }
*/
   protected <T extends EnterpriseBeanMetaData> T assertEnterpriseBean(String ejbName, EnterpriseBeansMetaData enterpriseBeansMetaData, Class<T> expected)
   {
      EnterpriseBeanMetaData enterpriseBeanMeta = enterpriseBeansMetaData.get(ejbName);
      assertNotNull(enterpriseBeanMeta);
      assertEquals(ejbName, enterpriseBeanMeta.getEjbName());
      return expected.cast(enterpriseBeanMeta);
   }
   
/*   private <T extends BeanMetaData> T assertEnterpriseBean(String ejbName, ApplicationMetaData applicationMetaData, Class<T> expected)
   {
      BeanMetaData beanMeta = applicationMetaData.getBeanByEjbName(ejbName);
      assertNotNull(beanMeta);
      assertEquals(ejbName, beanMeta.getEjbName());
      return expected.cast(beanMeta);
   }
*/   
   protected void assertRelationships(EjbJarMetaData ejbJarMetaData)
   {
      RelationsMetaData relationsMetaData = ejbJarMetaData.getRelationships();
      assertNotNull(relationsMetaData);
      assertId("relationships", relationsMetaData);
      assertDescriptions("relationships", relationsMetaData.getDescriptions());
      assertEquals(2, relationsMetaData.size());
      int count = 1;
      for (RelationMetaData relation : relationsMetaData)
         assertRelation("relation" + (count++), relation);
   }
   
   private void assertRelation(String prefix, RelationMetaData relationMetaData)
   {
      assertId(prefix, relationMetaData);
      assertDescriptions(prefix, relationMetaData.getDescriptions());
      assertEquals(prefix + "Name", relationMetaData.getEjbRelationName());
      assertRelationRole(prefix + "Role1", MultiplicityType.One, relationMetaData.getLeftRole());
      assertRelationRole(prefix + "Role2", MultiplicityType.Many, relationMetaData.getRightRole());
      assertEquals(relationMetaData.getLeftRole(), relationMetaData.getRelatedRole(relationMetaData.getRightRole()));
      assertEquals(relationMetaData.getRightRole(), relationMetaData.getRelatedRole(relationMetaData.getLeftRole()));
      assertEquals(relationMetaData.getLeftRole(), relationMetaData.getRightRole().getRelatedRole());
      assertEquals(relationMetaData.getRightRole(), relationMetaData.getLeftRole().getRelatedRole());
   }
   
   private void assertRelationRole(String prefix, MultiplicityType multiplicity, RelationRoleMetaData relationRoleMetaData)
   {
      assertNotNull(relationRoleMetaData);
      assertId(prefix, relationRoleMetaData);
      assertDescriptions(prefix, relationRoleMetaData.getDescriptions());
      assertEquals(prefix + "Name", relationRoleMetaData.getEjbRelationshipRoleName());
      assertEquals(multiplicity, relationRoleMetaData.getMultiplicity());
      if (multiplicity == MultiplicityType.Many)
      {
         assertTrue(relationRoleMetaData.isMultiplicityMany());
         assertFalse(relationRoleMetaData.isMultiplicityOne());
      }
      else
      {
         assertFalse(relationRoleMetaData.isMultiplicityMany());
         assertTrue(relationRoleMetaData.isMultiplicityOne());
      }
      assertEmpty(prefix, "CascadeDelete", relationRoleMetaData.getCascadeDelete());
      assertTrue(relationRoleMetaData.isCascadedDelete());
      assertRelationRoleSource(prefix + "RoleSource", relationRoleMetaData.getRoleSource());
      assertCmrField(prefix + "CmrField", relationRoleMetaData.getCmrField());
   }
   
   private void assertRelationRoleSource(String prefix, RelationRoleSourceMetaData relationRoleSourceMetaData)
   {
      assertNotNull(relationRoleSourceMetaData);
      assertId(prefix, relationRoleSourceMetaData);
      assertDescriptions(prefix, relationRoleSourceMetaData.getDescriptions());
      assertEquals(prefix + "Name" + "EjbName", relationRoleSourceMetaData.getEjbName());
   }
   
   private void assertCmrField(String prefix, CMRFieldMetaData cmrFieldMetaData)
   {
      assertNotNull(cmrFieldMetaData);
      assertId(prefix, cmrFieldMetaData);
      assertDescriptions(prefix, cmrFieldMetaData.getDescriptions());
      assertEquals(prefix + "Name", cmrFieldMetaData.getCmrFieldName());
      assertEquals(prefix + "Type", cmrFieldMetaData.getCmrFieldType());
   }
   
/*   protected void assertRelationships(ApplicationMetaData applicationMetaData)
   {
      Iterator<org.jboss.metadata.RelationMetaData> i = applicationMetaData.getRelationships();
      assertNotNull(i);
      int count = 1;
      while (i.hasNext())
      {
         org.jboss.metadata.RelationMetaData relation = i.next();
         assertRelation("relation" + (count++), relation);
      }
      assertEquals(3, count);
   }
*/   
/*   private void assertRelation(String prefix, org.jboss.metadata.RelationMetaData relationMetaData)
   {
      assertEquals(prefix + "Name", relationMetaData.getRelationName());
      assertRelationRole(prefix + "Role1", false, relationMetaData.getLeftRelationshipRole());
      assertRelationRole(prefix + "Role2", true, relationMetaData.getRightRelationshipRole());
      assertEquals(relationMetaData.getLeftRelationshipRole(), relationMetaData.getOtherRelationshipRole(relationMetaData.getRightRelationshipRole()));
      assertEquals(relationMetaData.getRightRelationshipRole(), relationMetaData.getOtherRelationshipRole(relationMetaData.getLeftRelationshipRole()));
      assertEquals(relationMetaData.getLeftRelationshipRole(), relationMetaData.getRightRelationshipRole().getRelatedRoleMetaData());
      assertEquals(relationMetaData.getRightRelationshipRole(), relationMetaData.getLeftRelationshipRole().getRelatedRoleMetaData());
   }
*/   
/*   private void assertRelationRole(String prefix, boolean many, RelationshipRoleMetaData relationRoleMetaData)
   {
      assertNotNull(relationRoleMetaData);
      assertEquals(prefix + "Name", relationRoleMetaData.getRelationshipRoleName());
      if (many)
      {
         assertTrue(relationRoleMetaData.isMultiplicityMany());
         assertFalse(relationRoleMetaData.isMultiplicityOne());
      }
      else
      {
         assertFalse(relationRoleMetaData.isMultiplicityMany());
         assertTrue(relationRoleMetaData.isMultiplicityOne());
      }
      assertTrue(relationRoleMetaData.isCascadeDelete());
      assertEquals(prefix + "RoleSourceName", relationRoleMetaData.getEntityName());
      assertEquals(prefix + "CmrFieldName", relationRoleMetaData.getCMRFieldName());
      assertEquals(prefix + "CmrFieldType", relationRoleMetaData.getCMRFieldType());
   }
*/   
   protected AssemblyDescriptorMetaData assertAssemblyDescriptor(EjbJarMetaData ejbJarMetaData)
   {
      AssemblyDescriptorMetaData assemblyDescriptorMetaData = ejbJarMetaData.getAssemblyDescriptor();
      assertNotNull(assemblyDescriptorMetaData);
      assertId("assembly-descriptor", assemblyDescriptorMetaData);
      assertSecurityRoles(2, assemblyDescriptorMetaData.getSecurityRoles());
      assertMethodPermissions(null, "methodPermission", 21, 3, assemblyDescriptorMetaData.getMethodPermissions());
      assertContainerTransactions(null, 42, 6, assemblyDescriptorMetaData.getContainerTransactions());
      assertMessageDestinations(2, assemblyDescriptorMetaData.getMessageDestinations());
      assertExcludeList(null, 35, 5, assemblyDescriptorMetaData.getExcludeList());
      return assemblyDescriptorMetaData;
   }
   
/*   protected void assertAssemblyDescriptor(ApplicationMetaData applicationMetaData)
   {
      org.jboss.metadata.AssemblyDescriptorMetaData assemblyDescriptorMetaData = applicationMetaData.getAssemblyDescriptor();
      assertNotNull(assemblyDescriptorMetaData);
      assertSecurityRoles(2, assemblyDescriptorMetaData.getSecurityRoles());
      assertMessageDestination("messageDestination1", assemblyDescriptorMetaData.getMessageDestinationMetaData("messageDestination1Name"));
      assertMessageDestination("messageDestination2", assemblyDescriptorMetaData.getMessageDestinationMetaData("messageDestination2Name"));
   }
*/
   protected void assertMethodPermissions(String ejbName, String prefix, int size, int limit, MethodPermissionsMetaData methodPermissionsMetaData)
   {
      assertNotNull(methodPermissionsMetaData);
      assertEquals(size, methodPermissionsMetaData.size());
      int count = 1;
      for (MethodPermissionMetaData methodPermissionMetaData : methodPermissionsMetaData)
      {
         assertId(prefix + count, methodPermissionMetaData);
         assertDescriptions(prefix + count, methodPermissionMetaData.getDescriptions());
         if (count == 1)
         {
            assertEmpty(prefix + count, "Unchecked", methodPermissionMetaData.getUnchecked());
            assertTrue(methodPermissionMetaData.isNotChecked());
         }
         else
         {
            assertRolesNames(prefix + count, 2, methodPermissionMetaData.getRoles());
            assertFalse(methodPermissionMetaData.isNotChecked());
         }
         if (count == 1)
            assertMethods(ejbName, prefix + count + "Method", 5, methodPermissionMetaData.getMethods());
         else
            assertMethods(ejbName, prefix + count + "Method", 1, methodPermissionMetaData.getMethods());
         ++count;
         if (count > limit)
            break;
      }
   }

/*   private void assertMethodPermissions(int size, BeanMetaData beanMetaData)
   {
      int count = 1;
      Iterator<org.jboss.metadata.MethodMetaData> methods = beanMetaData.getPermissionMethods();
      assertNotNull(methods);
      while(methods.hasNext())
      {
         String permissionPrefix = null;
         if (count <= 5)
            permissionPrefix = beanMetaData.getEjbName() + "MethodPermission1";
         else if (count == 6)
            permissionPrefix = beanMetaData.getEjbName() + "MethodPermission2";
         else if (count == 7)
            permissionPrefix = beanMetaData.getEjbName() + "MethodPermission3";

         org.jboss.metadata.MethodMetaData<?> methodMetaData = methods.next();
         if (count <= 5)
         {
            assertEmpty(methodMetaData.getRoles());
            assertTrue(methodMetaData.isUnchecked());
         }
         else
         {
            assertRolesNames(permissionPrefix, 2, methodMetaData.getRoles());
            assertFalse(methodMetaData.isUnchecked());
         }
         assertEquals(beanMetaData.getEjbName(), methodMetaData.getEjbName());
         assertTrue(methodMetaData.isIntfGiven());
         if (count == 1)
         {
            assertEquals(org.jboss.metadata.MethodMetaData.HOME_TYPE, methodMetaData.getInterfaceType());
            assertTrue(methodMetaData.isHomeMethod());
            assertFalse(methodMetaData.isRemoteMethod());
            assertFalse(methodMetaData.isLocalHomeMethod());
            assertFalse(methodMetaData.isLocalMethod());
            assertFalse(methodMetaData.isServiceEndpointMethod());
            String prefix = permissionPrefix + "Method1"; 
            assertEquals(prefix + "MethodName", methodMetaData.getMethodName());
            assertTrue(methodMetaData.isParamGiven());
            assertMethodParams(prefix, 2, methodMetaData.getMethodParams());
         }
         else if (count == 2)
         {
            assertEquals(org.jboss.metadata.MethodMetaData.REMOTE_TYPE, methodMetaData.getInterfaceType());
            assertFalse(methodMetaData.isHomeMethod());
            assertTrue(methodMetaData.isRemoteMethod());
            assertFalse(methodMetaData.isLocalHomeMethod());
            assertFalse(methodMetaData.isLocalMethod());
            assertFalse(methodMetaData.isServiceEndpointMethod());
            String prefix = permissionPrefix + "Method2"; 
            assertEquals(prefix + "MethodName", methodMetaData.getMethodName());
            assertFalse(methodMetaData.isParamGiven());
         }
         else if (count == 3)
         {
            assertEquals(org.jboss.metadata.MethodMetaData.LOCAL_HOME_TYPE, methodMetaData.getInterfaceType());
            assertFalse(methodMetaData.isHomeMethod());
            assertFalse(methodMetaData.isRemoteMethod());
            assertTrue(methodMetaData.isLocalHomeMethod());
            assertFalse(methodMetaData.isLocalMethod());
            assertFalse(methodMetaData.isServiceEndpointMethod());
            String prefix = permissionPrefix + "Method3"; 
            assertEquals(prefix + "MethodName", methodMetaData.getMethodName());
            assertFalse(methodMetaData.isParamGiven());
         }
         else if (count == 4)
         {
            assertEquals(org.jboss.metadata.MethodMetaData.LOCAL_TYPE, methodMetaData.getInterfaceType());
            assertFalse(methodMetaData.isHomeMethod());
            assertFalse(methodMetaData.isRemoteMethod());
            assertFalse(methodMetaData.isLocalHomeMethod());
            assertTrue(methodMetaData.isLocalMethod());
            assertFalse(methodMetaData.isServiceEndpointMethod());
            String prefix = permissionPrefix + "Method4"; 
            assertEquals(prefix + "MethodName", methodMetaData.getMethodName());
            assertFalse(methodMetaData.isParamGiven());
         }
         else if (count == 5)
         {
            assertEquals(org.jboss.metadata.MethodMetaData.SERVICE_ENDPOINT_TYPE, methodMetaData.getInterfaceType());
            assertFalse(methodMetaData.isHomeMethod());
            assertFalse(methodMetaData.isRemoteMethod());
            assertFalse(methodMetaData.isLocalHomeMethod());
            assertFalse(methodMetaData.isLocalMethod());
            assertTrue(methodMetaData.isServiceEndpointMethod());
            String prefix = permissionPrefix + "Method5"; 
            assertEquals(prefix + "MethodName", methodMetaData.getMethodName());
            assertFalse(methodMetaData.isParamGiven());
         }
         else if (count > 5)
         {
            assertEquals(org.jboss.metadata.MethodMetaData.HOME_TYPE, methodMetaData.getInterfaceType());
            assertTrue(methodMetaData.isHomeMethod());
            assertFalse(methodMetaData.isRemoteMethod());
            assertFalse(methodMetaData.isLocalHomeMethod());
            assertFalse(methodMetaData.isLocalMethod());
            assertFalse(methodMetaData.isServiceEndpointMethod());
            String prefix = permissionPrefix + "Method1"; 
            assertEquals(prefix + "MethodName", methodMetaData.getMethodName());
            assertMethodParams(prefix, 2, methodMetaData.getMethodParams());
         }
         ++count;
      }
      assertEquals(beanMetaData.getEjbName(), size, count-1);
   }
*/   
   private void assertRolesNames(String prefix, int size, Set<String> roles)
   {
      assertNotNull(roles);
      assertEquals(size, roles.size());
      int count = 0;
      while (++count <= roles.size())
      {
         assertTrue(roles.contains(prefix + "RoleName" + count));
      }
   }

   private void assertMethods(String ejbName, String prefix, int size, MethodsMetaData methodsMetaData)
   {
      assertMethods(ejbName, prefix, size, size, methodsMetaData);
   }

   private void assertMethods(String ejbName, String prefix, int size, int limit, MethodsMetaData methodsMetaData)
   {
      assertNotNull(methodsMetaData);
      assertEquals(size, methodsMetaData.size());
      int count = 1;
      for (MethodMetaData methodMetaData : methodsMetaData)
      {
         assertId(prefix + count, methodMetaData);
         assertDescriptions(prefix + count, methodMetaData.getDescriptions());
         if (ejbName == null)
            assertEquals(prefix + count + "EjbName", methodMetaData.getEjbName());
         else
            assertEquals(ejbName + "EjbName", methodMetaData.getEjbName());
         if (count == 1)
            assertEquals(MethodInterfaceType.Home, methodMetaData.getMethodIntf());
         else if (count == 2)
            assertEquals(MethodInterfaceType.Remote, methodMetaData.getMethodIntf());
         else if (count == 3)
            assertEquals(MethodInterfaceType.LocalHome, methodMetaData.getMethodIntf());
         else if (count == 4)
            assertEquals(MethodInterfaceType.Local, methodMetaData.getMethodIntf());
         else
            assertEquals(MethodInterfaceType.ServiceEndpoint, methodMetaData.getMethodIntf());
         assertEquals(prefix + count + "MethodName", methodMetaData.getMethodName());
         if (count == 1)
            assertMethodParams(prefix + count, 2, methodMetaData.getMethodParams());
         ++count;
         if (count > limit)
            break;
      }
   }

/*   private void assertMethods(String ejbName, String prefix, int size, Iterator<org.jboss.metadata.MethodMetaData> methods)
   {
      assertNotNull(methods);
      int count = 1;
      while (methods.hasNext())
      {
         org.jboss.metadata.MethodMetaData methodMetaData = methods.next();
         if (ejbName == null)
            assertEquals(prefix + count + "EjbName", methodMetaData.getEjbName());
         else
            assertEquals(ejbName, methodMetaData.getEjbName());
         if (count == 1)
            assertEquals(org.jboss.metadata.MethodMetaData.HOME_TYPE, methodMetaData.getInterfaceType());
         else if (count == 2)
            assertEquals(org.jboss.metadata.MethodMetaData.REMOTE_TYPE, methodMetaData.getInterfaceType());
         else if (count == 3)
            assertEquals(org.jboss.metadata.MethodMetaData.LOCAL_HOME_TYPE, methodMetaData.getInterfaceType());
         else if (count == 4)
            assertEquals(org.jboss.metadata.MethodMetaData.LOCAL_TYPE, methodMetaData.getInterfaceType());
         else
            assertEquals(org.jboss.metadata.MethodMetaData.SERVICE_ENDPOINT_TYPE, methodMetaData.getInterfaceType());
         assertEquals(prefix + count + "MethodName", methodMetaData.getMethodName());
         if (count == 1)
            assertMethodParams(prefix + count, 2, methodMetaData.getMethodParams());
         ++count;
      }
      assertEquals(size, count-1);
   }
*/
   protected void assertMethodParams(String prefix, int size, MethodParametersMetaData methodParametersMetaData)
   {
      assertNotNull(methodParametersMetaData);
      assertEquals(size, methodParametersMetaData.size());
      int count = 1;
      for (String parameter : methodParametersMetaData)
         assertEquals(prefix + "Param" + (count++), parameter);
   }

   protected void assertContainerTransactions(String ejbName, int size, int limit, ContainerTransactionsMetaData containerTransactionsMetaData)
   {
      assertNotNull(containerTransactionsMetaData);
      assertEquals(size, containerTransactionsMetaData.size());
      int count = 1;
      String prefix;
      if (ejbName == null)
         prefix = "containerTransaction";
      else
         prefix = ejbName + "ContainerTransaction";
      for (ContainerTransactionMetaData containerTransactionMetaData : containerTransactionsMetaData)
      {
         assertId(prefix + count, containerTransactionMetaData);
         assertDescriptions(prefix + count, containerTransactionMetaData.getDescriptions());
         if (count == 1)
            assertMethods(ejbName, prefix + count + "Method", 5, containerTransactionMetaData.getMethods());
         else
            assertMethods(ejbName, prefix + count + "Method", 1, containerTransactionMetaData.getMethods());
         if (count == 1)
            assertEquals(TransAttributeType.NotSupported, containerTransactionMetaData.getTransAttribute());
         else if (count == 2)
            assertEquals(TransAttributeType.Supports, containerTransactionMetaData.getTransAttribute());
         else if (count == 3)
            assertEquals(TransAttributeType.Required, containerTransactionMetaData.getTransAttribute());
         else if (count == 4)
            assertEquals(TransAttributeType.RequiresNew, containerTransactionMetaData.getTransAttribute());
         else if (count == 5)
            assertEquals(TransAttributeType.Mandatory, containerTransactionMetaData.getTransAttribute());
         else
            assertEquals(TransAttributeType.Never, containerTransactionMetaData.getTransAttribute());
         ++count;
         if (count > limit)
            break;
      }
   }

/*   private void assertContainerTransactions(Iterator<org.jboss.metadata.MethodMetaData> transactions)
   {
      assertNotNull(transactions);
      int count = 1;
      while(transactions.hasNext())
      {
         org.jboss.metadata.MethodMetaData<?> method = transactions.next();
         if (count <= 5)
            assertEquals(MetaData.TX_NOT_SUPPORTED, method.getTransactionType());
         else if (count == 6)
            assertEquals(MetaData.TX_SUPPORTS, method.getTransactionType());
         else if (count == 7)
            assertEquals(MetaData.TX_REQUIRED, method.getTransactionType());
         else if (count == 8)
            assertEquals(MetaData.TX_REQUIRES_NEW, method.getTransactionType());
         else if (count == 9)
            assertEquals(MetaData.TX_MANDATORY, method.getTransactionType());
         else
            assertEquals(MetaData.TX_NEVER, method.getTransactionType());
         ++count;
      }
      assertEquals(10, count-1);
   }
*/
   protected void assertExcludeList(String ejbName, int size, int limit, ExcludeListMetaData excludeListMetaData)
   {
      assertNotNull(excludeListMetaData);
      assertId("excludeList", excludeListMetaData);
      assertDescriptions("excludeList", excludeListMetaData.getDescriptions());
      if (ejbName != null)
         assertMethods(ejbName, ejbName + "ExcludeListMethod", size, limit, excludeListMetaData.getMethods());
      else
         assertMethods(null, "excludeListMethod", size, limit, excludeListMetaData.getMethods());
   }

/*   private void assertExcludedMethods(String ejbName, Iterator<org.jboss.metadata.MethodMetaData> excluded)
   {
      assertMethods(ejbName, ejbName + "ExcludeListMethod", 5, excluded);
   }
*/   
/*   protected void assertEnvironment(String prefix, BeanMetaData bean, boolean full)
   {
      if (full)
         assertEnvEntries(prefix, 2, bean.getEnvironmentEntries(), full);
      assertEjbRefs(prefix, 2, bean.getEjbReferences(), full);
      assertEjbLocalRefs(prefix, 2, bean.getEjbLocalReferences(), full);
      // TODO webservice service references
      assertResourceRefs(prefix, 2, bean.getResourceReferences(), full);
      assertResourceEnvRefs(prefix, 2, bean.getResourceEnvReferences(), full);
      assertMessageDestinationRefs(prefix, 3, bean.getMessageDestinationReferences(), full);
   }
*/
/*   protected void assertNullEnvironment(BeanMetaData bean)
   {
      assertEmptyIterator(bean.getEnvironmentEntries());
      assertEmptyIterator(bean.getEjbReferences());
      assertEmptyIterator(bean.getEjbLocalReferences());
      // TODO webservice service references
      assertEmptyIterator(bean.getResourceReferences());
      assertEmptyIterator(bean.getResourceEnvReferences());
      assertEmptyIterator(bean.getMessageDestinationReferences());
   }
*/   
   protected void assertSecurityIdentity(String ejbName, String type, SecurityIdentityMetaData securityIdentity, boolean full)
   {
      assertNotNull(ejbName, securityIdentity);
      String prefix = ejbName + type;
      assertId(prefix, securityIdentity);
      assertDescriptions(prefix, securityIdentity.getDescriptions());
      if (full)
      {
         assertEmpty(prefix, "UseCallerIdentity", securityIdentity.getUseCallerIdentity());
         assertTrue(securityIdentity.isUseCallerId());
         assertRunAs(ejbName, type, securityIdentity.getRunAs());
      }
   }
   
/*   protected void assertSecurityIdentity(String ejbName, String type, org.jboss.metadata.SecurityIdentityMetaData securityIdentity, boolean full)
   {
      assertNotNull(ejbName, securityIdentity);
      String prefix = ejbName + type;
      assertEquals("en-" + prefix + "-desc", securityIdentity.getDescription());
      if (full)
      {
         assertTrue(securityIdentity.getUseCallerIdentity());
         assertEquals(prefix + "RunAsRoleName", securityIdentity.getRunAsRoleName());
      }
   }
*/
   protected void assertRunAs(String prefix, String type, RunAsMetaData runAsMetaData)
   {
      assertNotNull(runAsMetaData);
      assertId(prefix + type + "RunAs", runAsMetaData);
      assertDescriptions(prefix + type + "RunAs", runAsMetaData.getDescriptions());
      assertEquals(prefix + type + "RunAsRoleName", runAsMetaData.getRoleName());
   }

   protected void assertSecurityRoleRefs(String ejbName, int size, SecurityRoleRefsMetaData securityRoleRefsMetaData)
   {
      assertNotNull(securityRoleRefsMetaData);
      assertEquals(size, securityRoleRefsMetaData.size());
      int count = 1;
      for (SecurityRoleRefMetaData securityRoleRefMetaData : securityRoleRefsMetaData)
      {
         assertId(ejbName + "SecurityRoleRef" + count, securityRoleRefMetaData);
         assertDescriptions(ejbName + "SecurityRoleRef" + count, securityRoleRefMetaData.getDescriptions());
         assertEquals(ejbName + "SecurityRoleRef" + count + "RoleName", securityRoleRefMetaData.getRoleName());
         assertEquals(ejbName + "SecurityRoleRef" + count + "RoleLink", securityRoleRefMetaData.getRoleLink());
         ++count;
      }
   }

/*   protected void assertSecurityRoleRefs(String ejbName, int size, Iterator<org.jboss.metadata.SecurityRoleRefMetaData> securityRoleRefs)
   {
      assertNotNull(securityRoleRefs);
      int count = 1;
      while (securityRoleRefs.hasNext())
      {
         org.jboss.metadata.SecurityRoleRefMetaData securityRoleRefMetaData = securityRoleRefs.next();
         assertEquals("en-" + ejbName + "SecurityRoleRef" + count + "-desc", securityRoleRefMetaData.getDescription());
         assertEquals(ejbName + "SecurityRoleRef" + count + "RoleName", securityRoleRefMetaData.getName());
         assertEquals(ejbName + "SecurityRoleRef" + count + "RoleLink", securityRoleRefMetaData.getLink());
         ++count;
      }
      assertEquals(ejbName, size, count-1);
   }
*/
   protected void assertClusterConfig(String prefix, ClusterConfigMetaData clusterConfig, boolean isSession)
   {
      assertNotNull(clusterConfig);
      prefix = prefix + "ClusterConfig";
      assertId(prefix, clusterConfig);
      assertDescriptions(prefix, clusterConfig.getDescriptions());
      assertEquals(prefix + "PartitionName", clusterConfig.getPartitionName());
      assertEquals(prefix + "HomeLoadBalancingPolicy", clusterConfig.getHomeLoadBalancingPolicy());
      assertEquals(prefix + "BeanLoadBalancingPolicy", clusterConfig.getBeanLoadBalancingPolicy());
      if (isSession)
         assertEquals(prefix + "SessionStateManagerJndiName", clusterConfig.getSessionStateManagerJndiName());
   }

/*   protected void assertClusterConfig(String prefix, org.jboss.metadata.ClusterConfigMetaData clusterConfig, boolean isSession)
   {
      assertNotNull(clusterConfig);
      prefix = prefix + "ClusterConfig";
      assertEquals(prefix + "PartitionName", clusterConfig.getPartitionName());
      assertEquals(prefix + "HomeLoadBalancingPolicy", clusterConfig.getHomeLoadBalancePolicy());
      assertEquals(prefix + "BeanLoadBalancingPolicy", clusterConfig.getBeanLoadBalancePolicy());
      if (isSession)
         assertEquals(prefix + "SessionStateManagerJndiName", clusterConfig.getHaSessionStateName());
   }
*/
   protected void assertCacheInvalidationConfig(String prefix, CacheInvalidationConfigMetaData invalidationConfig)
   {
      assertNotNull(invalidationConfig);
      prefix = prefix + "CacheInvalidationConfig";
      assertId(prefix, invalidationConfig);
      assertDescriptions(prefix, invalidationConfig.getDescriptions());
      assertEquals(prefix + "InvalidationGroupName", invalidationConfig.getInvalidationGroupName());
      assertEquals(prefix + "InvalidationManagerName", invalidationConfig.getInvalidationManagerName());
   }

/*   protected void assertCacheInvalidationConfig(String prefix, org.jboss.metadata.CacheInvalidationConfigMetaData invalidationConfig)
   {
      assertNotNull(invalidationConfig);
      prefix = prefix + "CacheInvalidationConfig";
      assertEquals(prefix + "InvalidationGroupName", invalidationConfig.getInvalidationGroupName());
      assertEquals(prefix + "InvalidationManagerName", invalidationConfig.getInvalidationManagerName());
   }
*/
   protected void assertIORSecurityConfig(String ejbName, IORSecurityConfigMetaData iorSecurityConfig)
   {
      assertNotNull(iorSecurityConfig);
      String prefix = ejbName + "IorSecurityConfig";
      assertId(prefix, iorSecurityConfig);
      assertDescriptions(prefix, iorSecurityConfig.getDescriptions());
      assertIORTransportConfig(ejbName, iorSecurityConfig.getTransportConfig());
      assertIORASContext(ejbName, iorSecurityConfig.getAsContext());
      assertIORSASContext(ejbName, iorSecurityConfig.getSasContext());
   }

/*   protected void assertIORSecurityConfig(String ejbName, IorSecurityConfigMetaData iorSecurityConfig)
   {
      assertNotNull(iorSecurityConfig);
      assertIORTransportConfig(ejbName, iorSecurityConfig.getTransportConfig());
      assertIORASContext(ejbName, iorSecurityConfig.getAsContext());
      assertIORSASContext(ejbName, iorSecurityConfig.getSasContext());
   }
*/
   protected void assertIORTransportConfig(String ejbName, IORTransportConfigMetaData iorTransportConfig)
   {
      assertNotNull(iorTransportConfig);
      String prefix = ejbName + "TransportConfig";
      assertId(prefix, iorTransportConfig);
      assertDescriptions(prefix, iorTransportConfig.getDescriptions());
      assertEquals(IORTransportConfigMetaData.INTEGRITY_NONE, iorTransportConfig.getIntegrity());
      assertEquals(IORTransportConfigMetaData.CONFIDENTIALITY_NONE, iorTransportConfig.getConfidentiality());
      assertEquals(IORTransportConfigMetaData.ESTABLISH_TRUST_IN_TARGET_NONE, iorTransportConfig.getEstablishTrustInTarget());
      assertEquals(IORTransportConfigMetaData.ESTABLISH_TRUST_IN_CLIENT_NONE, iorTransportConfig.getEstablishTrustInClient());
      assertEquals(IORTransportConfigMetaData.DETECT_MISORDERING_NONE, iorTransportConfig.getDetectMisordering());
      assertEquals(IORTransportConfigMetaData.DETECT_REPLAY_NONE, iorTransportConfig.getDetectReplay());
   }

/*   protected void assertIORTransportConfig(String ejbName, IorSecurityConfigMetaData.TransportConfig iorTransportConfig)
   {
      assertNotNull(iorTransportConfig);
      assertEquals(IORTransportConfigMetaData.INTEGRITY_NONE, iorTransportConfig.getIntegrity());
      assertEquals(IORTransportConfigMetaData.CONFIDENTIALITY_NONE, iorTransportConfig.getConfidentiality());
      assertEquals(IORTransportConfigMetaData.ESTABLISH_TRUST_IN_TARGET_NONE, iorTransportConfig.getEstablishTrustInTarget());
      assertEquals(IORTransportConfigMetaData.ESTABLISH_TRUST_IN_CLIENT_NONE, iorTransportConfig.getEstablishTrustInClient());
      assertEquals(IORTransportConfigMetaData.DETECT_MISORDERING_NONE, iorTransportConfig.getDetectMisordering());
      assertEquals(IORTransportConfigMetaData.DETECT_REPLAY_NONE, iorTransportConfig.getDetectReplay());
   }
*/
   protected void assertIORASContext(String ejbName, IORASContextMetaData asContext)
   {
      assertNotNull(asContext);
      String prefix = ejbName + "ASContext";
      assertId(prefix, asContext);
      assertDescriptions(prefix, asContext.getDescriptions());
      assertEquals(IORASContextMetaData.AUTH_METHOD_USERNAME_PASSWORD, asContext.getAuthMethod());
      assertEquals(prefix + "Realm", asContext.getRealm());
      assertFalse(asContext.isRequired());
   }

/*   protected void assertIORASContext(String ejbName, IorSecurityConfigMetaData.AsContext asContext)
   {
      assertNotNull(asContext);
      String prefix = ejbName + "ASContext";
      assertEquals(IORASContextMetaData.AUTH_METHOD_USERNAME_PASSWORD, asContext.getAuthMethod());
      assertEquals(prefix + "Realm", asContext.getRealm());
      assertFalse(asContext.isRequired());
   }
*/
   protected void assertIORSASContext(String ejbName, IORSASContextMetaData sasContext)
   {
      assertNotNull(sasContext);
      String prefix = ejbName + "SASContext";
      assertId(prefix, sasContext);
      assertDescriptions(prefix, sasContext.getDescriptions());
      assertEquals(IORSASContextMetaData.CALLER_PROPAGATION_NONE, sasContext.getCallerPropagation());
   }

/*   protected void assertIORSASContext(String ejbName, IorSecurityConfigMetaData.SasContext sasContext)
   {
      assertNotNull(sasContext);
      assertEquals(IORSASContextMetaData.CALLER_PROPAGATION_NONE, sasContext.getCallerPropagation());
   }
*/
   protected void assertIgnoreDependency(String ejbName, IgnoreDependencyMetaData ignoreDependency)
   {
      assertNotNull(ejbName, ignoreDependency);
      String prefix = ejbName + "IgnoreDependency";
      assertId(prefix, ignoreDependency);
      assertDescriptions(prefix, ignoreDependency.getDescriptions());
      assertResourceInjectionTargets(prefix, 2, ignoreDependency.getInjectionTargets());
   }

   protected void assertRemoteBinding(String ejbName, RemoteBindingMetaData remoteBinding)
   {
      assertNotNull(ejbName, remoteBinding);
      String prefix = ejbName + "RemoteBinding";
      assertId(prefix, remoteBinding);
      assertDescriptions(prefix, remoteBinding.getDescriptions());
      assertEquals(prefix + "JndiName", remoteBinding.getJndiName());
      assertEquals(prefix + "ClientBindUrl", remoteBinding.getClientBindUrl());
      assertEquals(prefix + "InterceptorStack", remoteBinding.getInterceptorStack());
      assertEquals(prefix + "ProxyFactory", remoteBinding.getProxyFactory());
   }

   protected void assertMethodAttributes(String ejbName, MethodAttributesMetaData methodAttributes)
   {
      assertNotNull(ejbName, methodAttributes);
      assertEquals(2, methodAttributes.size());
      String prefix = ejbName + "MethodAttributes";
      assertId(prefix, methodAttributes);
      int count = 1;
      for (MethodAttributeMetaData methodAttribute : methodAttributes)
      {
         if (count++ == 1)
         {
            assertEquals("get*", methodAttribute.getMethodName());
            assertTrue(methodAttribute.isReadOnly());
            assertTrue(methodAttribute.isIdempotent());
            assertEquals(5000, methodAttribute.getTransactionTimeout());
         }
         else
         {
            assertEquals("*", methodAttribute.getMethodName());
            assertFalse(methodAttribute.isReadOnly());
            assertFalse(methodAttribute.isIdempotent());
            assertEquals(0, methodAttribute.getTransactionTimeout());
         }
      }
   }

/*   protected void assertMethodAttributes(String ejbName, BeanMetaData bean)
   {
      assertDefaultMethodAttributes(ejbName, bean);
   }
*/
/*   protected void assertDefaultMethodAttributes(String ejbName, BeanMetaData bean)
   {
      assertFalse(bean.isMethodReadOnly("getSomething"));
      assertEquals(0, bean.getTransactionTimeout("getSomething"));
   }
*/   
   private void assertEmptyIterator(Iterator i)
   {
      if (i != null && i.hasNext())
         throw new AssertionFailedError("The iterator should be empty: " + i);
   }
}