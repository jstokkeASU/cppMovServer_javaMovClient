/**
 *MovieLibrary.cpp 
 *Copyright 2015 James Stokke,
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

#include "MovieLibrary.h"
#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <jsoncpp/json/json.h>
#include <jsonrpccpp/server/connectors/httpserver.h>

MovieLibrary::MovieLibrary()
{
    //Intentionally blank
}

MovieLibrary::~MovieLibrary()
{
  library.clear();
}

//bool MovieLibrary::add(MovieDescription aClip)
//{
    //if (library.find(aClip.toString()) != library.end()){
      //  return false;
    //}
    //library.insert(aClip.toString(),aClip);
    //return true;;
//}
bool MovieLibrary::remove(string aTitle)
{
   library.erase(aTitle);
   return true;
}

/*MovieDescription MovieLibrary::get(string aTitle)
{
    
    map<string,MovieDescription>::iterator it;   
    it=library.find(aTitle);
    if (it != library.end()){
	cout << endl << aTitle << " Information: " << library.find(aTitle)->second.descriptString();
    }
    MovieDescription b(aTitle);
    return b;
}
std::vector<string> MovieLibrary::getTitles()
{
    std::vector<string> titles;
    for( map<string,MovieDescription>::iterator j = library.begin(); j != library.end(); ++j ) {
    	titles.push_back( j->first );
    }
        //titles.push_back(library.first);
    cout << "List of Titles" << endl;
    for (unsigned i=0; i < titles.size()-1; i++){
        cout << titles[i] << ", ";
    }
    for (unsigned j=titles.size()-1; j < titles.size(); j++)
        cout << " " << titles[j];
    return titles;
}
*/

void MovieLibrary::initMovieLibraryFromJsonFile(string jsonFileName){
   ifstream jsonFile(jsonFileName.c_str());
   string line;
   cout << "The content of the Json file as a string: " << endl;
   if(jsonFile.is_open()){
      while(getline(jsonFile,line)){
         //cout << line << endl;
      }
   }else{
      cout << "Json file not opened properly" << endl;
   }
   jsonFile.close();
   Json::Reader reader;
   Json::Value root;
   std::ifstream json(jsonFileName.c_str(), std::ifstream::binary);
   bool parseSuccess = reader.parse(json,root,false);
   if(parseSuccess){
      //cout << "successful parse" << endl;
      Json::Value::Members mbr = root.getMemberNames();
      for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
         //cout << *i << " " << endl;
         Json::Value movieDesc = root[*i];
         string titleStr = "title";
         if(titleStr.compare(*i)==0){
            title = movieDesc.asString();
            //cout << "found title " << *i << " value: " << title << endl;
         }else{
            //cout << "found " << *i << endl;
	    std::vector<string> genre;
	    std::vector<string> actors;
            string title = movieDesc["Title"].asString();
	    //cout << "Test:" << title << endl;
	    string released = movieDesc["Released"].asString();
	    string rated = movieDesc["Rated"].asString();
	    string plot = movieDesc["Plot"].asString();
	    string runtime = movieDesc["Runtime"].asString();
	    string filename = movieDesc["Filename"].asString();
	    Json::Value genre1 = movieDesc["Genre"];
	    for (unsigned j=0; j < genre1.size(); j++){
	      //cout << "\n2. Entry: " << genre1[j].asString() << endl;
	      genre.push_back(genre1[j].asString());
	    }
	    //cout << "Genre1 is : " << genre1.size() << endl;
	    Json::Value actors1 = movieDesc["Actors"];
	    for (unsigned j=0; j < actors1.size(); j++){
	      //cout << "\n1. Actor: " << actors1[j].asString() << endl;
	      actors.push_back(actors1[j].asString());
	    }

            MovieDescription * movie = new MovieDescription(title,released,rated,plot,runtime,filename,genre,actors);
	    //library.insert(movie->toString(),*movie);
            //user->print();
            library[*i] = *movie;
         }
      }
   }
}
void MovieLibrary::getAllString(string aTitle)
{
  ofstream jsonText;
  jsonText.open("movies.json.cpp.text");  
  if (library.find(aTitle) != library.end()){
        for(map<string,MovieDescription>::iterator j = library.begin(); j != library.end(); ++j ) {
	  jsonText << j->second.descriptString(); 
        }
    }
  jsonText.close();  
}

bool MovieLibrary::resetFromJsonFile(string jsonFileName)
{
  bool ret = false;
   Json::Reader reader;
   Json::Value root;
   std::ifstream json(jsonFileName.c_str(), std::ifstream::binary);
   bool parseSuccess = reader.parse(json,root,false);
   if(parseSuccess){
      Json::Value::Members mbr = root.getMemberNames();
      for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
         //cout << *i << " " << endl;
        Json::Value jsonMedia = root[*i];
         MovieDescription * aMovieDescription = new MovieDescription(jsonMedia);
         library[*i] = *aMovieDescription;
         //cout << "adding ";
         //aMovieDescription->print();
      }
   }
   return true;
}

bool MovieLibrary::saveToJsonFile(string jsonFileName)
{
  bool ret = false;
   Json::Value jsonLib;
   for(std::map<string,MovieDescription>::iterator i = library.begin();
                                              i != library.end(); i++){
      string key = i->first;
      //cout << key << " " << endl;
      MovieDescription aMovieDescription = library[key];
      Json::Value jsonMovieDescription = aMovieDescription.toJson();
      jsonLib[key] = jsonMovieDescription;
   }
   Json::StyledStreamWriter ssw("  ");
   std::ofstream jsonOutFile(jsonFileName.c_str(), std::ofstream::binary);
   ssw.write(jsonOutFile, jsonLib);
   return true;
}

bool MovieLibrary::add(const Json::Value& aMovieDescriptionJson)
{
  bool ret = false;
  MovieDescription aMovieDescription(aMovieDescriptionJson);
  //aMovieDescription.print();
  library[aMovieDescription.title]=aMovieDescription;
  return true;
}
  
Json::Value MovieLibrary::get(string aTitle)
{
  MovieDescription aMovieDescription = library[aTitle];
  return aMovieDescription.toJson();
} 

Json::Value MovieLibrary::getTitles()
{
  Json::Value ret(Json::arrayValue);
  vector<string> myVec;
  for(map<string,MovieDescription>::iterator it = library.begin(); it != library.end(); ++it){
    myVec.push_back(it->first);
  }
  for(std::vector<string>::iterator it = myVec.begin(); it!=myVec.end();++it) {
    ret.append(Json::Value(*it));
  }
  return ret;
}

/*void MovieLibrary::toJsonFile(string filename)
{
  ofstream jsonText;
  jsonText.open(filename);
  jsonText << library.getAllString();
  jsonText.close();
}
*/

  


