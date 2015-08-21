<!-- Bring in the wowtags qualified names -->
<!ENTITY % WOWTags-qname.mod
         PUBLIC "-//University of Science//Information Systems Group//ENTITIES XHTML WOWTags Qnames 1.0//EN"
         "wowtags-qname-1.mod" >
%WOWTags-qname.mod;

<!-- Bring in the local extension module -->
<!ENTITY % Extension-qname.mod
        SYSTEM "extension-qname-1.mod" >
%Extension-qname.mod;

<!-- Define the xmlns extension attributes -->
<!ENTITY % XHTML.xmlns.extra.attrib
         "%WOWTags.xmlns.extra.attrib;
          %Extension.xmlns.extra.attrib;" >
