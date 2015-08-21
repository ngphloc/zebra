<!-- ...................................................................... -->
<!-- WOWTags Elements Module ................................................... -->
<!-- file: wowtags-1.mod

     PUBLIC "-//University of Science//Information Systems Group//ELEMENTS XHTML WOWTags Elements 1.0//EN"
     SYSTEM "wowtags-1.mod"

     xmlns:wowtags="../../xmlns/wowtags"
     ...................................................................... -->

<!-- WOWTags Module

       block
       footer

     This module defines a simple wowtags xmldoc structure
-->

<!-- Define the global namespace attributes -->
<![%WOWTags.prefixed;[
<!ENTITY % WOWTags.xmlns.attrib
    "%NS.decl.attrib;"
>
]]>
<!ENTITY % WOWTags.xmlns.attrib
    "%NS.decl.attrib;
     xmlns  %URI.datatype;  #FIXED '%WOWTags.xmlns;'"
>

<!ELEMENT %WOWTags.block.qname; 
     ( %WOWTags.a.qname;, %WOWTags.if.qname;)* >
<!ATTLIST %WOWTags.block.qname;
     %WOWTags.xmlns.attrib;
>

<!ELEMENT %WOWTags.footer.qname; ( EMPTY ) >
<!ATTLIST %WOWTags.footer.qname;
     print   (yes|no)   'yes'
     file   CDATA   #IMPLIED      
     %WOWTags.xmlns.attrib;
>


<!ELEMENT %WOWTags.a.qname; ( #PCDATA ) >
<!ATTLIST %WOWTags.a.qname; 
     href   CDATA   #IMPLIED
     class   CDATA   #IMPLIED
     %WOWTags.xmlns.attrib;
>

<!ELEMENT %WOWTags.if.qname; 
     ( %WOWTags.block.qname;, %WOWTags.block.qname;) >
<!ATTLIST %WOWTags.if.qname;
     expr   CDATA   #REQUIRED     
     %WOWTags.xmlns.attrib;
>

<!-- end of wowtags-1.mod -->
