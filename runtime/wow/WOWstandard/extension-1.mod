<!-- ...................................................................... -->
<!-- Extension Elements Module ................................................... -->
<!-- file: extension-1.mod

     SYSTEM "extension-1.mod"

     xmlns:wowext="../../xmlns/wowext"
     ...................................................................... -->

<!-- Extension Module

     store
       aisle

     This module defines an extension to the wowtags structure
-->

<!-- Define the global namespace attributes -->
<![%Extension.prefixed;[
<!ENTITY % Extension.xmlns.attrib
    "%NS.decl.attrib;"
>
]]>
<!ENTITY % Extension.xmlns.attrib
    "%NS.decl.attrib;
     xmlns  %URI.datatype;  #FIXED '%Extension.xmlns;'"
>

<!ELEMENT %Extension.store.qname;
     ( %Extension.aisle.qname; )* >
<!ATTLIST %Extension.store.qname;
     name   CDATA   #IMPLIED
     %Extension.xmlns.attrib;
>

<!-- end of extension-1.mod -->


