                 A USER MODELING SYSTEM FOR  ADAPTIVE LEARNING
Thesis Submitted for the Degree of Doctor of Philosophy in Computer Science
                                   at 
                        University of Science 
             Vietnam National University, Ho Chi Minh city

                               -----------

Supervisor:  Prof. Dr. Dong Thi Bich Thuy 
Affiliation: Department of Information System 
             Faculty of Information Technology 
             University of Science 
             Vietnam National University, Ho Chi Minh city 
PhD Student: 
             Nguyen Phuoc Loc 
             Email: ng_phloc@yahoo.com 
             Phone: 84-90-8222007

                               -----------

This CD contains three directories:
+ lib: Java libraries for runtime and compiling
+ docs: containing thesis, slides, papers
+ source: containing the source code of the User Modeling System (Zebra) and the Adaptive Learning System (WOW!)
+ runtime: containing JDK 6, Tom cat sever that need to run the program.
Note that two config files "runtime/tomcat/webapps/wow/WEB-INF/wowconfig.xml, zebraconfig.xml" are very important in configuring WOW! and Zebra.

                               -----------

The User Modeling System in this thesis named Zebra aimes to support Adaptive Learning. I hope that it has robust inference mechanism and runs fast like an African zebra. The architecture of Zebra has two engines: mining engine (ME) and belief network engine (BNE). Both engines are responsible for constructing the Triangular Learner Model (TLM) which is constituted of three sub-models: Knowledge, Learning Style and Learning History.

Zebra includes two modules: client and server. The communication between server and client may obeys many protocols such as RMI, SOAP (web service), HTTP, and socket.
It requires JDK 6 (or newer), Tom cat server, maybe MySQL server to run Zebra but you don't need to consider such sofwares because I have already included them in this CD.

1. Run Zebra server: zebra-server.bat
2. Run Zebra client: zebra-client.bat
3. Run adaptive learning system: wow.bat (you should run registerusers.bat so as to register some initial users)
4. Re-compile source code: build.bat
5. Register some initial users: registerusers.bat
6. Unmout virtual disk: unmount.bat
Note: After executing "registerusers.bat", there are many initial users with the same password: test. You can login by such users. Please be carefull, all old existing users in database will be removed when running "registerusers.bat"

                               -----------

WOW! support three types of user knowledge evaluation: overlay, bayes (default), dynbayes when performing adaptive learning tasks
1. overlay: using the attribute "knowledge" in concept
2. bayes: using overlay bayesian network together the parameter algorithm learning algorithm EM
3. dynbayes: using dynamic overlay bayesian network
You can alter the types of user knowledge evaluation in the config file "zebraconfig.xml"

                               -----------

Special users:
1. WOW! administrator: (username=admin, password=test)
2. Zebra administrator: (username=admin, password=test)
3. WOW author-teacher composing domain model, learning material: (username=author, password=test)
4. Default student who using adaptive leatning system WOW!: (username=guest, password=test).

                               -----------

Note that Zebra is the test program; so the mining engine performs mining tasks every 2*10-minute and the Zebra garbage collection also perform every two hours. You can modify the variable UPDATE_LEARNING_HISTORY_INTERVAL in config file "zebraconfig.xml" so as to be longer, maybe every day or even every week.

Besides two main engines: mining engine (ME) and belief network engine (BNE), Zebra has another one so-called Garbage collection engine aiming to destroy users who don't access learning system in specific time interval. Garbage collection engine is not discussed in the thesis but it is necessary in some situations when there are many users accessing Zebra concurrently. You can modify the variables in config file "zebraconfig.xml" so as to control the behaviors of Garbage collection engine.

