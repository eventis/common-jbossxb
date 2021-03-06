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
package org.jboss.xb.binding.introspection;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.ref.WeakReference;
import org.jboss.xb.binding.JBossXBRuntimeException;
import org.jboss.xb.util.NoopMap;


/**
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version <tt>$Revision: $</tt>
 */
public class ClassInfos
{
   private static final Map<ClassLoader, Map<String, WeakReference<ClassInfo>>> NO_CACHE = new NoopMap<ClassLoader, Map<String, WeakReference<ClassInfo>>>();
   private static Map<ClassLoader, Map<String, WeakReference<ClassInfo>>> classloaderCache = new WeakHashMap<ClassLoader, Map<String, WeakReference<ClassInfo>>>();

   /**
    * Disables caching of ClassInfo's. Already cached ClassInfo's will be lost after
    * the method returns.
    */
   public static void disableCache()
   {
      synchronized(classloaderCache)
      {
         classloaderCache = NO_CACHE;
      }
   }

   /**
    * Enables caching of ClassInfo's unless caching is already enabled.
    */
   public static void enableCache()
   {
      synchronized(classloaderCache)
      {
         if(!isCacheEnabled())
         {
            classloaderCache = new WeakHashMap<ClassLoader, Map<String, WeakReference<ClassInfo>>>();
         }
      }
   }

   /**
    * @return true if caching is enabled, false otherwise.
    */
   public static boolean isCacheEnabled()
   {
      synchronized(classloaderCache)
      {
         return classloaderCache != NO_CACHE;
      }
   }

   /**
    * Flushes all the cached ClassInfo's.
    */
   public static void flushCache()
   {
      synchronized(classloaderCache)
      {
         classloaderCache.clear();
      }
   }

   /**
    * Evicts ClassInfo for a specific class.
    * @param cls  fully qualified class name of the class
    */
   public static void flushCache(String cls)
   {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      Map<String, WeakReference<ClassInfo>> classLoaderCache = getClassLoaderCache(cl);
      classLoaderCache.remove(cls);
   }

   /**
    * Evicts ClassInfo for a specific class.
    * @param cls  the class to remove the ClassInfo for
    */
   public static void flushCache(Class<?> cls)
   {
      Map<String, WeakReference<ClassInfo>> classLoaderCache = getClassLoaderCache(cls.getClassLoader());
      classLoaderCache.remove(cls.getName());
   }

   public static ClassInfo getClassInfo(Class<?> cls)
   {
      Map<String, WeakReference<ClassInfo>> classLoaderCache = getClassLoaderCache(cls.getClassLoader());

      WeakReference<ClassInfo> weak = classLoaderCache.get(cls.getName());
      if(weak != null)
      {
         Object result = weak.get();
         if(result != null)
         {
            return (ClassInfo)result;
         }
      }

      ClassInfo clsInfo = new ClassInfo(cls);
      weak = new WeakReference<ClassInfo>(clsInfo);
      classLoaderCache.put(cls.getName(), weak);
      return clsInfo;
   }

   public static ClassInfo getClassInfo(String name, boolean required)
   {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      Map<String, WeakReference<ClassInfo>> classLoaderCache = getClassLoaderCache(cl);

      WeakReference<ClassInfo> weak = classLoaderCache.get(name);
      if(weak != null)
      {
         Object result = weak.get();
         if(result != null)
         {
            return (ClassInfo)result;
         }
      }

      try
      {
         ClassInfo clsInfo = new ClassInfo(cl.loadClass(name));
         weak = new WeakReference<ClassInfo>(clsInfo);
         classLoaderCache.put(name, weak);
         return clsInfo;
      }
      catch(ClassNotFoundException e)
      {
         if(required)
         {
            throw new JBossXBRuntimeException("Failed to load class " + name);
         }
      }

      return null;
   }

   private static Map<String, WeakReference<ClassInfo>> getClassLoaderCache(ClassLoader cl)
   {
      synchronized(classloaderCache)
      {
         Map<String, WeakReference<ClassInfo>> result = classloaderCache.get(cl);
         if (result == null)
         {
            result = new ConcurrentHashMap<String, WeakReference<ClassInfo>>();
            classloaderCache.put(cl, result);
         }
         return result;
      }
   }
}
