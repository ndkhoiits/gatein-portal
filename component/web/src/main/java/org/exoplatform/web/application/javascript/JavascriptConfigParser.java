/*
 * Copyright (C) 2009 eXo Platform SAS.
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
package org.exoplatform.web.application.javascript;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:hoang281283@gmail.com">Minh Hoang TO</a>
 * @version $Id$
 *
 */
public class JavascriptConfigParser
{

   public static void processConfigResource(InputStream is, JavascriptConfigService service, ServletContext scontext){
      List<JavascriptTask> tasks = fetchTasks(is);
      if(tasks != null){
         for(JavascriptTask task : tasks){
            task.execute(service, scontext);
         }
      }
   }
   
   private static List<JavascriptTask> fetchTasks(InputStream is)
   {
      try
      {
         DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document document = docBuilder.parse(is);
         return fetchTasksFromXMLConfig(document);
      }
      catch (Exception ex)
      {
         return null;
      }
   }
   
   private static List<JavascriptTask> fetchTasksFromXMLConfig(Document document){
      List<JavascriptTask> tasks = new ArrayList<JavascriptTask>();
      Element element = document.getDocumentElement();
      //NodeList nodes = element.getElementsByTagName(GateinResource.JAVA_SCRIPT_TAG);
      NodeList nodes = element.getElementsByTagName("javascript");
      int length = nodes.getLength();
      for(int i = 0; i < length; i++){
         JavascriptTask task = xmlToTask((Element)nodes.item(i));
         if(task != null){
            tasks.add(task);
         }
      }
      return tasks;
   }
   
   private static JavascriptTask xmlToTask(Element element){
      //if(!GateinResource.JAVA_SCRIPT_TAG.equals(element.getTagName())){
      if(!"javascript".equals(element.getTagName())){
         return null;
      }
      try{
         JavascriptTask task = new JavascriptTask();
         //NodeList nodes = element.getElementsByTagName(GateinResource.JAVA_SCRIPT_PARAM);
         NodeList nodes = element.getElementsByTagName("param");
         int length = nodes.getLength();
         for(int i = 0; i < length ; i++){
            Element param_ele = (Element)nodes.item(i);
            String js_module = param_ele.getElementsByTagName("js-module").item(0).getFirstChild().getNodeValue();
            String js_path = param_ele.getElementsByTagName("js-path").item(0).getFirstChild().getNodeValue();
            task.addParam(js_module, js_path);
         }
         return task;
      }catch(Exception ex){
         ex.printStackTrace();
         return null;
      }
   }   
}
