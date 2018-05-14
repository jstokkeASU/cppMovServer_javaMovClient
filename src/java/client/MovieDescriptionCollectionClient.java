package movie.client;

import java.io.*;
import java.util.*;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

import movie.server.MovieDescription;
import movie.server.MovieDescriptionCollection;
import movie.server.MovieDescriptionHttpProxy;

/**
 * Copyright (c) 2016 James Stokke,
 * Software Engineering,
 * Arizona State University at the Polytechnic campus
 * <p/>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation version 2
 * of the License.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but without any warranty or fitness for a particular purpose.
 * <p/>
 * Please review the GNU General Public License at:
 * http://www.gnu.org/licenses/gpl-2.0.html
 * see also: https://www.gnu.org/licenses/gpl-faq.html
 * so you are aware of the terms and your rights with regard to this software.
 * Or, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,USA
 * <p/>
 * Purpose:
 * A Java class and main method demonstrating an approach to calling
 * JsonRpc methods where argument and return types are user-defined classes.
 *
 * @author James Stokke (jstokke@asu.edu), ASU Software Engineering
 * @version September 2016
 * @license See above
 */
public class MovieDescriptionCollectionClient extends Object {

   static final boolean debugOn = false;

   private static void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }


   public static void main(String args[]) {

      String host = "localhost";
      String port = "8080";
      
      try {
         if(args.length >= 2){
            host = args[0];
            port = args[1];
         }
         String url = "http://"+host+":"+port+"/";
         System.out.println("Opening connection to: "+url);
         MovieDescriptionCollection mc = (MovieDescriptionCollection)new MovieDescriptionHttpProxy(new URL(url));
         BufferedReader stdin = new BufferedReader(
            new InputStreamReader(System.in));
         System.out.print("Enter end or {add|get|getTitles|remove} followed by args>");
         String inStr = stdin.readLine();
         StringTokenizer st = new StringTokenizer(inStr);
         String opn = st.nextToken();
         while(!opn.equalsIgnoreCase("end")) {
            if(opn.equalsIgnoreCase("add")){
               String name = "";
               while(st.hasMoreTokens()){
                  name = name + st.nextToken();
                  if(st.hasMoreTokens()) name = name + " ";
               }
               MovieDescription aMov = new MovieDescription(name,"everyone","that one time","a minute","Booy calls it a dooy.","thisfile",new String[]{"Drama","Love"},new String[]{"Bill Williamson","Jack Johnson"});
               boolean result = mc.add(aMov);
               System.out.println("Add "+aMov.title+" result "+result);
            }else if (opn.equalsIgnoreCase("get")) {
               String name = st.nextToken();
               while(st.hasMoreTokens()){
                  name = name + " " + st.nextToken();
               }
               MovieDescription result = mc.get(name);
               System.out.println("Got "+result.toString());
            }else if (opn.equalsIgnoreCase("getTitles")) {
               String[] result = mc.getTitles();
               System.out.print("The collection has entries for the following movies: ");
               for (int i = 0; i < result.length; i++){
                  System.out.println(result[i]+", ");
               }
            }else if (opn.equalsIgnoreCase("remove")) {
               String name = st.nextToken();
               while(st.hasMoreTokens()){
                  name = name + " " + st.nextToken();
               }
               boolean result = mc.remove(name);
               System.out.println("remove "+name+" result "+result);
            }
            System.out.print("Enter end or {add|get|getTitles|remove} followed by args>");
            inStr = stdin.readLine();
            st = new StringTokenizer(inStr);
            opn = st.nextToken();
         }
      }catch (Exception e) {
         e.printStackTrace();
         System.out.println("Oops, you didn't enter the right stuff");
      }
   }
}
