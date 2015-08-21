// (c) 2008 WOW Project
// Author: Rob Essink
// Changed by: Loc Nguyen

// this is part of the WOW! 1.99 distribution


// useage:
//  1) include this file
//   with <script language="javascript" src="[thisfile]"></script>
//  2) if you send a cookie within the HTTP-header of the HTTP-result
//     containing lastWOWpage=[this URL]
//     the page will not be reloaded the first time (saves trafic and CPUtime)
//     Example:
//       for http://localhost:8080/wow/Get/file:/demo/index.xhtml
//       response.addCookie("lastWOWpage",
//              "http://localhost:8080/wow/Get/file:/demo/index.xhtml")
//       should be executed
//  3) the body tag should be <body onload="checkreload()">
//     to get this working

    function cleanURL(s) {
      // strip ';' constructions
      if(s.indexOf(";")>=0) {
        return(s.substring(0,i).toLowerCase())
      } else {
        return(s.toLowerCase())
      }  
    }

    function lastWOW() {
       var mycookies=document.cookie.split(";")
       for(var i=0;i<mycookies.length;++i) {
         var cookiepair=mycookies[i].split("=");
         if (cookiepair[0]=="lastWOWpage") return cookiepair[1]
       }
       return null      
    }

    // storeWOW can be used as insurance, if
    // a servlet does not provide a cookie.. it
    // doesn't start reloading over and over.
    //
    function storeWOW() {
       document.cookie="lastWOWpage="+window.document.URL;
    }

    function checkreload() {
      // cut ';' and everything after it (session cookies)
      myWOW=cleanURL(lastWOW())
      docURL=cleanURL(window.document.URL)
      storeWOW()
      if ((myWOW!=null) && (myWOW!=docURL)) {
        // debugging:
        //alert("reloading with URL="+docURL+" lastWOW="+myWOW)
        window.document.location.reload(true)
        // didn't work eh?
        window.document.reload()
        // hmmm... what next.. error..
        alert("Your browser understands javascript, but won't reload!")
      } else {
        storeWOW()
      }
    }
