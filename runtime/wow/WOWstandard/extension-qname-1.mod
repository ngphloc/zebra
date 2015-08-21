<!-- ...................................................................... -->
<!-- Extension Qname Module ............................................... -->
<!-- file: extension-qname-1.mod

     xmlns:wowext="../../xmlns/wowext"
     ...................................................................... -->

<!-- Declare the default value for prefixing of this module's elements -->
<!-- Note that the NS.prefixed will get overridden by the XHTML Framework or
     by a document instance. -->
<!ENTITY % NS.prefixed "IGNORE" >
<!ENTITY % Extension.prefixed "%NS.prefixed;" >

<!-- Declare the actual namespace of this module -->
<!ENTITY % Extension.xmlns "../../2L690/xml/xmlns/wowext" >

<!-- Declare the default prefix for this module -->
<!ENTITY % Extension.prefix "wowext" >

<!-- Declare the prefix and any prefixed namespaces that are required by 
     this module -->
<![%Extension.prefixed;[
<!ENTITY % Extension.pfx "%Extension.prefix;:" >
<!ENTITY % Extension.xmlns.extra.attrib
    "xmlns:%Extension.prefix;   %URI.datatype;  #FIXED  '%Extension.xmlns;'" >
]]>
<!ENTITY % Extension.pfx "" >
<!ENTITY % Extension.xmlns.extra.attrib "" >

<!ENTITY % Extension.store.qname "%Extension.pfx;store" >
<!ENTITY % Extension.aisle.qname "%Extension.pfx;aisle">
