		WOW! 1.0


WOW! is based on AHA! (the Adapative Hypermedia Architecture) which is a system for
user-profile based adaptive hypertext content.
If you do not know what that means but wish to find out
we suggest you just try it out through the AHA! tutorial.
This tutorial is included in the AHA! distribution but is
not "installed".

License: GNU Public License 2.0 (see gpl.txt)

main authors: Paul De Bra, Natalia Stash, David Smits
student contributors include: Monique Ansems, Bart Berden,
Ferry van den Boom, Rob Essink, Davy Floes, Hitesh Gupta,
Barend de Lange, Ewald Ramp, Brendan Rousseau, Tomi Santic, Koen Verheyen.
Modified by Loc Nguyen, email: ng_phloc@yahoo.com, mobile: +84-8-222008 

main website: http://www.hcmuns.edu.vn/

requirements:
  * Java SDK version 1.5, (1.5.0 or higher).
    Go to http://java.sun.com/j2se/downloads.html
    to download the SDK.
    Do not just download the Runtime Environment (JRE), as the
    Sun JWSDP or Tomcat server we use requires the SDK version
    for some of its features (which WOW! does not use).

  * TomCat webserver 5.0 (full featured edition), available from Sun
      through the SUN Java Web Service Development Package
        http://java.sun.com/webservices/downloads/webservicespack.html
      or directly from the Jakarta project
        http://jakarta.apache.org/tomcat

    We describe how to install WOW! with the Sun JWSDP, but WOW! works
    equally well with the TomCat directly from Jakarta (if you manage
    to complete the installation using that distribution).

  * The zip-file containing WOW! (You probably have that because
    it contains this readme file.)

installation:

  1) Make sure you have the Java SDK v1.5 (or newer) and a RUNNING TomCat server.
     (use startup.bat on Windows or startup.sh on Unix, found
     in the TomCat bin-directory)
     We will assume from now on that TomCat is running on machine "wowhost".
     Please give TomCat enough time to start up. (Wait until it is idle.
     This can take seconds or minutes depending on the speed of your server.)
     In this README file we assume you are running TomCat on its
     default port 8080. However, you can run TomCat with WOW! on
     any desired port.

  2) Unzip the WOW! zipfile in a directory for which you have read and write
     access (usually, but not necessarily, a part of the TomCat webtree).
     It will create a directory "wow" which contains all files.

  3) Build the WOW! project by running the script in the build directory
     (use bw.bat on Windows or bl on Unix).
     *** Note that this is an extremely important step. ***
     WOW! no longer comes precompiled. The ability to compile WOW!
     is a good indicator that later on it will actually run.
     
  4) Connect to the TomCat server (http://wowhost:8080/admin/).
     You should have created a system administrator on installation
     of JWSDP/TomCat, enter with this login/password.
     Open 'Service (Java Web Services Developer Pack)', by
     clicking on the node (NOT the icon or text).
     Now select 'Host (localhost)' by clicking the icon or text,
     and wait a few seconds.

  5) In the upper-right corner you can select in the 'Available Actions'
     list the option 'Create New Context' (and wait again).
     Please set these values:
        Document Base = /wow
	                (the directory you extracted the WOW! zipfile in)
        Path          = /wow  (the path within the webtree, /wow is good)
        Reloadable    = True or False
			(The server works faster if set to false, but
			if you experiment with code changes you don't have
			to restart the server after every change. Also,
			if Reloadable you have to restart the server once,
			else twice.)
        Use Naming    = True
                        (the server cannot find the WOW! configuration file
                        if this is not set to True)
        Working Dir.. = work/Standard Engine/localhost/wow
        Session ID In.= WOWDISTR30   (can be any string)
     Note: you have to 'Save' your context AND 'Commit Changes'. You
     can verify your settings in conf/server.xml in the TomCat directory.
     (You may have to restart your server at this point.)
     
  6) Now WOW is installed on your system and after you restart the
     server (shutdown + startup) you are ready to go.
     You now have to configure the WOW settings by connecting to
     http://wowhost:8080/wow/Config (mind that
     you don't select wow/Config/Config).
     Some seconds after the first message appears you can click on the
     "Configure" link if the context is reloadable. (If not then you
     have to restart the server again.)
     A login prompt appears.
     Your login is 'wow' and the password is initially empty.

  7) 'wow' with an empty password is not a good login so you want to
     alter your login when you enter the system. Go to 'Manager Configuration'
     choose action 'Change existing user' and press submit. Fill in your
     new login name, name and password. Now press submit, remember your
     changes are SAVED ON LOGOUT.

  8) If -1- you have a Database server and -2- you want to use it for WOW
     read on, otherwise skip the following point (8).

     Go to Configure Database, enable Database and fill in
     all information. If you haven't created a special WOW database and
     user we suggest you use the default value.

     Enable 'create user and database' (unless you already have a fully
     configured WOW database).
     The webserverID is the name of the webserver because it should have
     access to the database.
     If the Database server is on the same system 'localhost' is sufficient.
     Within the webserverID field you can use '%' as a wildcard character.
     Now you can submit.

     The system might ask for a root-password, this happens when it's not
     able to connect and create the tables with the user login you entered.
     If you enter the root-password of the database server it will
     try again but now with administrator access.

     If you're asked to convert the database 'Yes' would transfer
     the examples to the Database server. This is strongly advised.

  9) Don't forget to log out of the Configuration page.
     Now goto 'http://wowhost:8080/wow/' and you should see a
     welcome page that includes this readme file at the bottom.

Here are some typical "problems" you may encounter and how to solve them:

    "Going to http://wowhost:8080/wow doesn't work!"
    -- Likely the webserver has not embeded the WOW system yet.
       You could try to restart (shutdown and start) TomCat.

    "I always see the same window when I go to
     http://wowhost:8080/wow/Config. I never see the login page.
    -- Possibly the Reload option of the WOW-context was not set to
       'True' (see above).
       You can solve the problem by stopping and starting the TomCat
       server. This forces TomCat to look at the updated configuration.

    "When I click the link to 'configure' I only get an errormessage" or
    "When I log in as 'wow' with an empty password I get 'access denied'"
    -- This indicates that WOW! can't access it's environment variables
       where the configfile path is stored. You probably forgot to set
       "Use Naming" to True.

    "There is only an wowconfig.xml.in found in wow/WEB-INF."
    -- The initialization failed. And there is a serious problem.
       You could hack your way through this (but please be
       so kind to report the bug + the catalina.out file).
       First copy wowconfig.xml.in to wowconfig.xml
       You should change '@WOWROOT@' and '@WEBROOT@' in
       the directory ending with /wow .
       @XMLROOT@ = @WOWROOT@ + "/xmlroot" and
       @UIDFILE@ = @XMLROOT@ + "/uid"
       ('/' also works under windows, actually even better than '\').

And one more advanced question you might have (if you know Tomcat well):
    "I would like to install WOW with the deployment application,
     but there is no WAR file?"
    -- Correct! WOW edits files within it's own webtree (for
       user profiles, configuration options, etc.)
       Besides, you would like to add new courses to the WOW system,
       wouldn't you.
       So a WAR file would only complicate things more then they are.
