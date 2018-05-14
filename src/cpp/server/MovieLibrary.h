/**
 *MovieLibrary.h
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

#ifndef MOVIELIBRARY_H
#define MOVIELIBRARY_H
#include "MovieDescription.h"
#include <string>
#include <map>
#include <vector>

using namespace std;

class MovieLibrary
{
public:
    MovieLibrary();
    ~MovieLibrary();
    /*bool add(MovieDescription const aClip);*/
    bool remove(string aTitle);
    /*//MovieDescription get(string aTitle);
    //std::vector<string> getTitles();*/
    void initMovieLibraryFromJsonFile(string jsonFileName);
    void getAllString(string aTitle);
    void toJsonFile(string filename);
    
    bool resetFromJsonFile(string jsonFileName);
    bool saveToJsonFile(string jsonFileName);
    string serviceInfo();
    bool add(const Json::Value& aMovieDescriptionJson);
    Json::Value get(string aTitle); //returns a MovieDescription's json
    Json::Value getTitles(); //returns a json array of strings

protected:
    std::map<std::string, MovieDescription> library;
    //std::map<std::string, MovieDescription> it;
    string title;
    //T* array;
};

#endif
