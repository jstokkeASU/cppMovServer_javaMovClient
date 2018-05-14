package movie.server;

import java.util.Hashtable;
import java.util.Iterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.Enumeration;

import org.json.JSONObject;
import org.json.JSONTokener;

import edu.asu.ser.jsonrpc.common.JsonRpcException;
import edu.asu.ser.jsonrpc.server.HttpServer;

import java.io.IOException;

/**
 * Copyright (c) 2016 James Stokke,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: This class is part of an example developed to demonstrated distributed
 * and mobile apps connecting to servers using JsonRPC.
 * The application provides a Java RPC server using http for connection.
 *
 * @author James Stokke jstokke@asu.edu
 * @version September 2016
 *
 **/

class MovieDescriptionCollectionHttpServer extends Object implements MovieDescriptionCollection{

   public Hashtable<String,MovieDescription> MovieDescriptions;
   private static final boolean debugOn = false;
   private static final String MovieDescriptionJsonFileName = "movies.json.txt";

   public MovieDescriptionCollectionHttpServer() {
      debug("creating a new MovieDescription collection");
      MovieDescriptions = new Hashtable<String,MovieDescription>();
      try{
         this.resetFromJsonFile();
      }catch(Exception ex){
         System.out.println("error resetting from MovieDescriptions json file"+ex.getMessage());
      }
   }

   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

   public boolean resetFromJsonFile() throws JsonRpcException {
      boolean ret = true;
      try{
         MovieDescriptions.clear();
         String fileName = MovieDescriptionJsonFileName;
         File f = new File(fileName);
         FileInputStream is = new FileInputStream(f);
         JSONObject MovieDescriptionMap = new JSONObject(new JSONTokener(is));
         Iterator<String> it = MovieDescriptionMap.keys();
         while (it.hasNext()){
            String mType = it.next();
            JSONObject MovieDescriptionJson = MovieDescriptionMap.optJSONObject(mType);
            MovieDescription movie = new MovieDescription(MovieDescriptionJson);
            MovieDescriptions.put(movie.title, movie);
            debug("added "+movie.title+" : "+movie.toJsonString()+
                  "\nMovieDescriptions.size() is: " + MovieDescriptions.size());
         }
      }catch (Exception ex){
         System.out.println("Exception reading json file: "+ex.getMessage());
         ret = false;
      }
      return ret;
   }

   public boolean saveToJsonFile() throws JsonRpcException {
      boolean ret = true;
      try {
         String jsonStr;
         JSONObject obj = new JSONObject();
         for (Enumeration<String> e = MovieDescriptions.keys(); e.hasMoreElements();){
            MovieDescription amovie = MovieDescriptions.get((String)e.nextElement());
            obj.put(amovie.title,amovie.toJson());
         }
         PrintWriter out = new PrintWriter(MovieDescriptionJsonFileName);
         out.println(obj.toString(2));
         out.close();
      }catch(Exception ex){
         ret = false;
      }
      return ret;
   }
   
   public boolean add(MovieDescription amovie) throws JsonRpcException {
      boolean ret = true;
      debug("adding MovieDescription named: "+((amovie==null)?"unknown":amovie.title));
      try{
         MovieDescriptions.put(amovie.title,amovie);
      }catch(Exception ex){
         ret = false;
      }
      return ret;
   }

   public boolean remove(String aTitle) throws JsonRpcException {
      debug("removing MovieDescription named: "+aTitle);
      return ((MovieDescriptions.remove(aTitle)==null)?false:true);
   }

   public String[] getTitles() throws JsonRpcException {
      String[] ret = {};
      debug("getting "+MovieDescriptions.size()+" MovieDescription titles.");
      if(MovieDescriptions.size()>0){
         ret = (String[])(MovieDescriptions.keySet()).toArray(new String[0]);
      }
      return ret;
   }
   
   
   public MovieDescription get(String aTitle) throws JsonRpcException {
      MovieDescription ret = new MovieDescription("unknown","unknown1","unknown2","unknown3","unknown4","unknown5",new String[]{"empty"},new String[]{"empty"});
      MovieDescription aMovie = MovieDescriptions.get(aTitle);
      if (aMovie != null) {
	ret = aMovie;
      }
      return ret;
   }

   public static void main(String[] args) throws IOException {
      String port = "2222";
      if (args.length > 0) {
         port = args[0];
      }
      HttpServer serv = new HttpServer(
         new MovieDescriptionCollectionHttpServer(),Integer.parseInt(port));
      serv.start();
   }

}
