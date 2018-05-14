/**
 *movieserver.cpp
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
 * @version August 2016
 */

#include <jsonrpccpp/server.h>
#include <jsonrpccpp/server/connectors/httpserver.h>
#include <json/json.h>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>

#include "MovieLibrary.h"
#include "movieserverstub.h"

using namespace jsonrpc;
using namespace std;

class MovieServer : public movieserverstub {
public:
   MovieServer(AbstractServerConnector &connector, int port);
   virtual std::string serviceInfo();
   virtual bool saveToJsonFile();
   virtual bool resetFromJsonFile();
   virtual bool add(const Json::Value& mediaName);
   virtual bool remove(const std::string& mediaName);
   virtual Json::Value get(const std::string& mediaName);
   virtual Json::Value getTitles();
   
private:
   MovieLibrary * library;
   int portNum;
};

MovieServer::MovieServer(AbstractServerConnector &connector, int port) :
                             movieserverstub(connector){
   library = new MovieLibrary();
   library->resetFromJsonFile("movies.json.txt");
   portNum = port;
}

string MovieServer::serviceInfo(){
   std::string msg =
                "Movie Library management service.";
   stringstream ss;
   ss << portNum;
   cout << "serviceInfo called. Returning: Movie Library management service."
        << endl;
   return  msg.append(ss.str());
}

bool MovieServer::saveToJsonFile(){
   cout << "saving collection to movies.json.txt" << endl;
   bool ret = library->saveToJsonFile("movies.json.txt");
   return ret;
}

bool MovieServer::resetFromJsonFile(){
   cout << "restoring collection from movies.json.txt" << endl;
   bool ret = library->resetFromJsonFile("movies.json.txt");
   return ret;
}

bool MovieServer::add(const Json::Value& MovieDescriptionTitle) {
   cout << "Adding " << MovieDescriptionTitle << endl;
   bool ret = library->add(MovieDescriptionTitle);
   return ret;
}

bool MovieServer::remove(const string& MovieDescriptionTitle) {
   cout << "Removing " << MovieDescriptionTitle << endl;
   bool ret = library->remove(MovieDescriptionTitle);
   return ret;
}

Json::Value MovieServer::get(const string& MovieTitle){
   cout << "Now Getting " << MovieTitle << endl;
   return library->get(MovieTitle);
}

Json::Value MovieServer::getTitles(){
   Json::FastWriter fw;
   std::string titles = fw.write(library->getTitles());
   cout << "Get titles returning: " << titles  << endl;
   return library->getTitles();
}


int main(int argc, char * argv[]) {
   // invoke with ./bin/movieRPCServer 2222
   int port = 2222;
   if(argc > 1){
      port = atoi(argv[1]);
   }
   HttpServer httpserver(port);
   MovieServer ss(httpserver, port);
   cout << "Movie Library server listening on port " << port
        << " press return/enter to quit." << endl;
   ss.StartListening();
   int c = getchar();
   ss.StopListening();
   return 0;
}
