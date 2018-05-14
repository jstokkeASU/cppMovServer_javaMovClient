/**
 *MovieDescription.h
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

#ifndef MOVIEDESCRIPTION_H
#define MOVIEDESCRIPTION_H
#include <cstdlib>
#include <map>
#include <vector>
#include <iostream>
#include <iterator>
#include <string>
#include <sstream>
#include <fstream>
#include <algorithm>
#include <iterator>
#include <jsoncpp/json/json.h>

using namespace std;


class MovieDescription
{
public:
    MovieDescription();
    MovieDescription(const Json::Value& jsonObj);
    MovieDescription(string jsonString);
    MovieDescription(string title1, string rated1, string released1, string runtime1, string plot1, string filename1, std::vector<string> genre1, std::vector<string> actors1);
    ~MovieDescription();
    
    string getTitle();
    string getRated();
    string getReleased();
    string getRuntime();
    string getPlot();
    string getFilename();
    string getGenreString();
    string getActorsString();
    string descriptString();
    
    void setTitle();
    void setRated();
    void setReleased();
    void setRuntime();
    void setPlot();
    void setFilename();
    string toJsonString();
    Json::Value toJson();
    void fromJson(Json::Value json);
    void setValues(string title1, string rated1, string released1, string runtime1, string plot1, string filename1, std::vector<string> genre1, std::vector<string> actors1);
    void printString();
    

    string title;
    string rated;
    string released;
    string runtime;
    string plot;
    string filename;
    std::vector<string> genre;
    std::vector<string> actors;
};

#endif
