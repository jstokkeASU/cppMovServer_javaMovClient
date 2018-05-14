/**
 *MovieDescription.cpp 
 *Copyright 2016 James Stokke,
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
 * @version September 2016
 */


#include "MovieDescription.h"
#include <iostream>
#include <stdlib.h>
#include <cmath>

using namespace std;


MovieDescription::MovieDescription()
{
    //Intentionally blank
}

MovieDescription::MovieDescription(const Json::Value& jsonObj){
   string releasedStr = "Released";
   string ratedStr = "Rated";
   string actorsStr = "Actors";
   string plotStr = "Plot";
   string runtimeStr = "Runtime";
   string genreStr = "Genre";
   string filenameStr = "Filename";
   string titleStr = "Title";
   Json::Value::Members mbr = jsonObj.getMemberNames();
   for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
      Json::Value jsonM = jsonObj[*i];
      if(releasedStr.compare(*i)==0){
        released = jsonM.asString();
      }else if (ratedStr.compare(*i)==0){
	rated = jsonM.asString();
      }else if(actorsStr.compare(*i)==0){
         actors = vector<string>();
         for(int i=0; i<jsonM.size(); i++){
            actors.push_back(jsonM[i].asString());
         }
      }else if(plotStr.compare(*i)==0){
         plot = jsonM.asString();
      }else if(runtimeStr.compare(*i)==0){
         runtime = jsonM.asString();
      }else if(genreStr.compare(*i)==0){
         genre = vector<string>();
         for(int i=0; i<jsonM.size(); i++){
            genre.push_back(jsonM[i].asString());
         } 
      }else if(filenameStr.compare(*i)==0){
         filename = jsonM.asString();
      }else if(titleStr.compare(*i)==0){
         title = jsonM.asString();
      }
  }
}

MovieDescription::MovieDescription(string title1, string rated1, string released1, string runtime1, string plot1, string filename1, std::vector<string> genre1, std::vector<string> actors1)
{
    title = title1;
    rated = rated1;
    released = released1;
    runtime = runtime1;
    plot = plot1;
    filename = filename1;
    genre = genre1;
    actors = actors1;
}

MovieDescription::MovieDescription(string jsonString){
   string titleStr = "Title";
   string ratedStr = "Rated";
   string releasedStr = "Released";
   string runtimeStr = "Runtime";
   string plotStr = "Plot";
   string filenameStr = "Filename";
   string genreStr = "Genre";
   string actorsStr = "Actors";
   Json::Reader reader;
   Json::Value root;
   bool parseSuccess = reader.parse(jsonString,root,false);
   if(parseSuccess){
      Json::Value::Members mbr = root.getMemberNames();
      for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
         Json::Value jsonM = root[*i];
         if(titleStr.compare(*i)==0){
            title = jsonM.asString();
         }else if(ratedStr.compare(*i)==0){
            rated = jsonM.asString();
	 }else if(releasedStr.compare(*i)==0){
            released = jsonM.asString();
	 }else if(runtimeStr.compare(*i)==0){
            runtime = jsonM.asString();
         }else if(plot.compare(*i)==0){
            plot = jsonM.asString();
	 }else if(filenameStr.compare(*i)==0){
            filename = jsonM.asString();
	 }else if(ratedStr.compare(*i)==0){
            rated = jsonM.asString();
	 }else if(genreStr.compare(*i)==0){
            genre = vector<string>();
            for(int i=0; i<jsonM.size(); i++){
               genre.push_back(jsonM[i].asString());
            }
         }else if(actorsStr.compare(*i)==0){
            actors = vector<string>();
            for(int i=0; i<jsonM.size(); i++){
               actors.push_back(jsonM[i].asString());
            }
         }
      }
   }else{
      cout << "MovieDescription constructor parse error with input: " << jsonString
           << endl;
   }
}

MovieDescription::~MovieDescription()
{
    title = "";
    rated = "";
    released = "";
    runtime = "";
    plot = "";
    filename = "";
}

string MovieDescription::getTitle()
{
    return title;
}
string MovieDescription::getRated()
{
    return rated;
}
string MovieDescription::getReleased()
{
    return released;
}
string MovieDescription::getRuntime()
{
    return runtime;
}
string MovieDescription::getPlot()
{
    return plot;
}
string MovieDescription::getFilename()
{
    return filename;
}
string MovieDescription::getGenreString()
{
    std::stringstream ss;
     ss << "[\"";
    std::copy(genre.begin(), genre.end()-1,std::ostream_iterator<std::string>(ss,"\", \""));
    std::copy(genre.end()-1, genre.end(),std::ostream_iterator<std::string>(ss,"\""));
    ss << "]";
    return ss.str();
}
string MovieDescription::getActorsString()
{
    std::stringstream ss;
    ss << "[\"";
    std::copy(actors.begin(), actors.end()-1,std::ostream_iterator<std::string>(ss,"\", \""));
    std::copy(actors.end()-1, actors.end(),std::ostream_iterator<std::string>(ss,"\""));
    ss << "]";
    return ss.str();
}
string MovieDescription::descriptString()
{
    std::stringstream buf;
    buf << "  \"" << getTitle() << "\": {" << endl;
    buf << "    \"Released\": \""  << getRated() << "\"," << endl;
    buf << "    \"Rated\": \""  << getReleased() << "\"," << endl;
    buf << "    \"Actors\": "  << getActorsString() << "," << endl;
    buf << "    \"Plot\": \""  << getRuntime() << "\"," << endl;
    buf << "    \"Runtime\": \""  << getPlot() << "\"," << endl;
    buf << "    \"Genre\": "  << getGenreString() << "," << endl;
    buf << "    \"Title\": \""  << getTitle() << "\"" << endl;
    buf << "  }," << endl;
    //getTitle() << ", Rated:  " << getRated() << ", Released:  "  << ", Runtime:  " << getRuntime() << ", Plot:  " << getPlot() << endl;
    //buf << "Genre:  " << getGenreString() << endl;
    //buf << "Actors:  " << getActorsString() << endl;
    return buf.str();
}

void MovieDescription::printString()
{
    cout << "Title: " << title << endl;
    cout << "Released: "  << released << endl;
    cout << "Rated: " << rated << endl;
    cout << "Plot: "  << plot << endl;
    cout << "Runtime: " << runtime << endl;
    cout << "Filename: "  << filename << endl;
    cout << "Actors: ";
    for (int i=0; i < actors.size()-1; i++)
      cout << actors[i] << ", ";  
    for (int j=actors.size()-1; j < actors.size(); j++)
      cout << actors[j] << endl;
    cout << "Genre: ";
    for (int m=0; m < actors.size()-1; m++)
      cout << genre[m] << ", "; 
    for (int k=genre.size()-1; k < genre.size(); k++)
      cout << genre[k] << endl;
    /*getTitle() << ", Rated:  " << getRated() << ", Released:  "  << ", Runtime:  " << getRuntime() << ", Plot:  " << getPlot() << endl;
    //buf << "Genre:  " << getGenreString() << endl;
    //buf << "Actors:  " << getActorsString() << endl;
    */
}

string MovieDescription::toJsonString()
{
   string ret = "{}";
   Json::Value jsonMov;
   jsonMov["Title"] = title;
   jsonMov["Rated"] = rated;
   jsonMov["Released"] = released;
   jsonMov["Runtime"] = runtime;
   jsonMov["Plot"] = plot;
   jsonMov["Filename"] = filename;
   Json::Value gn(Json::arrayValue);
   for(int i=0; i<genre.size(); i++){
      gn[i]=genre[i];
   }
   jsonMov["Genre"] = gn;
   Json::Value ac(Json::arrayValue);
   for(int i=0; i<actors.size(); i++){
      ac[i]=actors[i];
   }
   jsonMov["Actors"] = ac;
   ret = jsonMov.toStyledString();
   return ret;
}

Json::Value MovieDescription::toJson(){
   Json::Value jsonMov;
   jsonMov["Title"] = title;
   jsonMov["Rated"] = rated;
   jsonMov["Released"] = released;
   jsonMov["Runtime"] = runtime;
   jsonMov["Plot"] = plot;
   jsonMov["Filename"] = filename;
   Json::Value gn(Json::arrayValue);
   for(int i=0; i<genre.size(); i++){
      gn[i]=genre[i];
   }
   jsonMov["Genre"] = gn;
   Json::Value ac(Json::arrayValue);
   for(int i=0; i<actors.size(); i++){
      ac[i]=actors[i];
   }
   jsonMov["Actors"] = ac;
   return jsonMov;
}

void MovieDescription::setValues(string title1, string rated1, string released1, string runtime1, string plot1, string filename1, std::vector<string> genre1, std::vector<string> actors1)
{
  title = title1;
  rated = rated1;
  released = released1;
  runtime = runtime1;
  plot = plot1;
  filename = filename1;
  genre = genre1;
  actors = actors1;
}

//main()
//{
//    std::vector<string> genre;
//    genre.push_back("Action");
//    genre.push_back("Comedy");
//    std::vector<string> actors;
//    actors.push_back("Pete Jackson");
//    actors.push_back("Ura Phung");
    
//    MovieDescription madeUp("Made Up","G","2016","1:30","None.","mp4",genre,actors);
//    cout << madeUp.toString();
    
//}




    
    
    
    
    
    
    
