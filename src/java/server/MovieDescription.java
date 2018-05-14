package movie.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;
import java.util.Arrays;


/**
 * Copyright 2016 James Stokke,
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
 * 
 * I granted my permission for the code to be used by ASU to copy, execute, and 
 * distribute as necessary for evaluation in the Ser321 course, and as otherwise 
 * required for SE program evaluation. Java and C++ programs and classes are defined 
 * and function according to the assignment.
 * 
 * Purpose: Collection of movie for purposes of building with ant.
 *
 * Ser321 Foundations of Distributed Applications
 * see http://pooh.poly.asu.edu/Ser321
 * @author James Stokke jstokke@asu.edu
 *         Software Engineering
 * @version August 2016
 */


public class MovieDescription {

	private static final boolean debugOn = false;

	public String title, rated, released, runtime, plot, filename;
	public Vector<String> genre, actors;
	
	
	public MovieDescription(){
		
	}
	public MovieDescription(String title, String rated, String released, String runtime, String plot, String filename, String[] type, String[] performer){
	  this.title = title;
	  this.rated = rated;
	  this.released = released;
	  this.runtime = runtime;
	  this.plot = plot;
	  this.filename = filename;
	  this.genre = new Vector<String>();
	  this.genre.addAll(Arrays.asList(type));
	  this.actors = new Vector<String>();
	  this.actors.addAll(Arrays.asList(performer));
		
	}
	public MovieDescription(String jsonStr){
	  try{
	    JSONObject jo = new JSONObject(jsonStr);
	    System.out.println("1.  The problem is in MovieDescription(String jsonStr)");
	    title = jo.getString("Title");
	    rated = jo.getString("Rated");
	    released = jo.getString("Released");
	    runtime = jo.getString("Runtime");
	    plot = jo.getString("Plot");
	    filename = jo.getString("Filename");
	    genre = new Vector<String>();
	    JSONArray ja = jo.optJSONArray("Genre");
	    for (int i=0; i< ja.length(); i++){
	      genre.add(ja.getString(i));
	    }
	    actors = new Vector<String>();
	    JSONArray jb = jo.optJSONArray("Actors");
	    for (int i=0; i< jb.length(); i++){
	      actors.add(jb.getString(i));
	    }
	  }catch (Exception ex){
	    System.out.println(this.getClass().getSimpleName()+
                            ": error(0) converting from json string");
	  }
	}
	public MovieDescription(JSONObject jsonObj){
	  try{
	    debug("constructor from json received: " + jsonObj.toString());
	    title = jsonObj.optString("Title","unknown");
	    rated = jsonObj.optString("Rated","unknown1");
	    released = jsonObj.optString("Released","unknown2");
	    runtime = jsonObj.optString("Runtime","unknown3");
	    plot = jsonObj.optString("Plot","unknown4");
	    filename = jsonObj.optString("Filename","unknown5");
	    genre = new Vector<String>();
	    JSONArray ja = jsonObj.getJSONArray("Genre");
	    for (int i=0; i< ja.length(); i++){
	      genre.add(ja.getString(i));
	    }
	    actors = new Vector<String>();
	    JSONArray jb = jsonObj.getJSONArray("Actors");
	    for (int j=0; j< jb.length(); j++){
	      actors.add(jb.getString(j));
	    }
	}catch(Exception ex){
	  System.out.println(this.getClass().getSimpleName()+
                            ":error(1) converting from json string");
      }
   }
	public JSONObject toJson(){
	  JSONObject jo = new JSONObject();
	  try{
	    jo.put("Title",title);
	    jo.put("Rated",rated);
	    jo.put("Released",released);
	    jo.put("Runtime",runtime);
	    jo.put("Plot",plot);
	    jo.put("Filename",filename);
	    jo.put("Genre",genre);
	    jo.put("Actors",actors);
	  }catch (Exception ex){
	    System.out.println(this.getClass().getSimpleName()+
                            ": error(2) converting to json");
	  }
	  return jo;
	}
	public String toJsonString(){
	  String ret = "";
	  try{
	    ret = this.toJson().toString();
	  }catch (Exception ex){
	    System.out.println(this.getClass().getSimpleName()+
                            ":error(3) error converting to json string");
	  }
	  return ret;
	}
	public String toString(){
	  StringBuilder sb = new StringBuilder();
	  sb.append("Movie ").append(title).append(" has runtime ");
	  sb.append(runtime).append(" is rated ").append(rated);
	  sb.append(" has categories ");
	  for (int i=0; i<genre.size(); i++){
	    sb.append(genre.get(i)).append(" ");
	  }
	  return sb.toString();
	}
	private void debug(String message) {
	  if (debugOn)
	    System.out.println("debug: "+message);
	}




	/*
	public MovieDescription(JSONObject obj) throws JSONException{
		title = obj.getString("title");
		rated = obj.getString("rated");
		released = obj.getString("released");
		runtime = obj.getString("runtime");
		plot = obj.getString("plot");
		filename = obj.getString("filename");
		genre = obj.getJSONArray("genres");
		actors = obj.getJSONArray("actors");	
	}
	public JSONObject toJSONObject() throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put("title", title);
		obj.put("rated", rated);
		obj.put("released", released);
		obj.put("runtime", runtime);
		obj.put("plot", plot);
		obj.put("filename", filename);
		obj.put("genres", genre);
		obj.put("actors", actors);
		System.out.println("MovieDescription added to JSON: "+obj.toString());
		return obj;
	}

	public String getTitle() {
		return title;
	}

	public String getRated() {
		return rated;
	}

	public String getReleased() {
		return released;
	}

	public String getRuntime() {
		return runtime;
	}

	public String getPlot() {
		return plot;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public void setRated(String rated) {
		this.rated = rated;
	}
	public void setReleased(String released) {
		this.released = released;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void setGenre(JSONArray genre) {
		this.genre = genre;
	}
	public void setActors(JSONArray actors) {
		this.actors = actors;
	}
	public String getFilename() {
		return filename;
	}

	//public JSONArray getGenre() {
	//	return genre;
	//}

	//public JSONArray getActors() {
	//	return actors;
	//}
	public String toString(){
		String description = "";
		description += "\""+title+"\": {"+"\n    \"Released:\" : \""+released+
				",\n    \"Rated:\" : \""+rated+",\n    \"Actors:\" :"+actors+
				",\n    \"Plot:\" : \""+plot+"\""+",\n    \"Runtime:\" : \""+runtime+"\""+
				",\n    \"Genre:\" :"+genre+"\""+",\n    \"Filename:\" : \""+filename+
				",\n    \"Title:\" : \""+title+"\"";
		return description;
	}
	
	public MovieDescription add(JSONObject obj, String input, HashMap lib) throws JSONException{
		JSONObject ojb = new JSONObject();
		String title = ojb.getJSONObject(input).getString("Title");
		String runtime = ojb.getJSONObject(input).getString("Runtime");
		String rated = ojb.getJSONObject(input).getString("Rated");
		String plot = ojb.getJSONObject(input).getString("Plot");
		String released = ojb.getJSONObject(input).getString("Released");
		JSONArray genre = ojb.getJSONObject(input).getJSONArray("Genre");
		JSONArray actors = ojb.getJSONObject(input).getJSONArray("Actors");
		MovieDescription newMov = new MovieDescription(title,rated,released,runtime,plot,filename,genre,actors);
		System.out.println(newMov.toString());
		return newMov;
	}
	/*
	public String[] genreString(){
		MovieDescription obj = new MovieDescription();
		String[] genre = obj.getNames("Genre");
		return genre;
	}
	public String[] actorsString(){
		MovieDescription obj = new MovieDescription();
		String[] actors = obj.getActors("Actors");
		return actors;
	}
	*/
	
}
