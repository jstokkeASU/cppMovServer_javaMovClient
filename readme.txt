To build everthing use "ant build.all".

c++ Movie Server
To start the server the command is ./bin/movieRPCServer ${port.num}.  The ${port.num} is currently set to 2222.

c++ Client (Assignment 5)
To start the cpp client the command is:  ./bin/movieRPCClient http://hostname:portnum, 
where hostname is IP Address of the machine that the MovieServer is running on, and portnum is port number that the server is set to run on.
Example:
if "./bin/movieRPCServer 2222" used to start server on machine with IP Address of 192.210.1, begin client as:
./bin/movieRPCClient http://192.210.1:2222

Java Download Server
To build the Download Server use "ant build.downserver".  To execute the download server use "ant execute.downserver"
When the play command is run in the Java MovieClient, the files are downloaded from the Server src/java/server/DataServer and saved in the src/java/client/DataClient folder.
The MovieClient plays the files from the this folder.  The ant clean command deletes these files.

Java Movie Client
To execute the Java Movie Client use the command: "java -cp classes:lib/* movie.client.MovieClient ${host.name} ${port.num}" where ${host.name} = IP Address for Server
and ${host.name} = Port Number that the server is listening on. 

Using the command "ant" or "ant targets" will provide you with the information as well.

