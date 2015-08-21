<!-- ...................................................................... -->
<!-- WOWTags Qname Module ................................................... -->
<!-- file: wowtags-qname-1.mod

     PUBLIC "-//University of Science//Information Systems Group//ELEMENTS XHTML WOWTags Qnames 1.0//EN"
     SYSTEM "WOWStandard/wowtags-qname-1.mod"

     xmlns:wowtags="../../xmlns/wowtags"
     ...................................................................... -->

<!-- Declare the default value for prefixing of this module's elements -->
<!-- Note that the NS.prefixed will get overridden by the XHTML Framework or
     by a document instance. -->
<!ENTITY % NS.prefixed "IGNORE" >
<!ENTITY % WOWTags.prefixed "%NS.prefixed;" >

<!-- Declare the actual namespace of this module -->
<!ENTITY % WOWTags.xmlns "../../xmlns/wowtags" >

<!-- Declare the default prefix for this module -->
<!ENTITY % WOWTags.prefix "wowtags" >

<!-- Declare the prefix and any prefixed namespaces that are required by 
     this module -->
<![%WOWTags.prefixed;[
<!ENTITY % WOWTags.pfx "%WOWTags.prefix;:" >
<!ENTITY % WOWTags.xmlns.extra.attrib
    "xmlns:%WOWTags.prefix;   %URI.datatype;  #FIXED  '%WOWTags.xmlns;'" >
]]>
<!ENTITY % WOWTags.pfx "" >
<!ENTITY % WOWTags.xmlns.extra.attrib "" >

<!ENTITY % XHTML.xmlns.extra.attrib "%WOWTags.xmlns.extra.attrib;" >

<!ENTITY % WOWTags.block.qname "%WOWTags.pfx;block" >
<!ENTITY % WOWTags.footer.qname "%WOWTags.pfx;footer" >
<!ENTITY % WOWTags.a.qname "%WOWTags.pfx;a" >
<!ENTITY % WOWTags.if.qname "%WOWTags.pfx;if" >
