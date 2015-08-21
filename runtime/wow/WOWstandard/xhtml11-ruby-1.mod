<!-- ...................................................................... -->
<!-- XHTML 1.1 Ruby 1.0 Module ............................................ -->
<!-- file: xhtml11-ruby-1.mod

     This is Ruby 1.0, an XML module providing ruby annotation markup.
     Copyright 1999 W3C (MIT, INRIA, Keio), All Rights Reserved.
     Revision: $Id: xhtml11-ruby-1.mod,v 1.0 2005/02/13 17:17:09 wow Exp $

     This module is based on the W3C Ruby Annotation specification:

        http://www.w3.org/TR/ruby

     This DTD module is identified by the PUBLIC and SYSTEM identifiers:

     PUBLIC "-//W3C//ELEMENTS XHTML 1.1 Ruby 1.0//EN"
     SYSTEM "xhtml11-ruby-1.mod"

     ...................................................................... -->

<!-- Ruby Elements

        ruby, rbc, rtc, rb, rt, rp

     This module declares the elements and their attributes used to
     support ruby annotation markup.
-->

<!-- rp fallback is included by default.
-->
<!ENTITY % Ruby.fallback "INCLUDE" >

<!-- Group ruby is included by default; it may be overridden by
     other modules to ignore it.
-->
<!ENTITY % Ruby.group "INCLUDE" >

<!-- Fragments for the content model of the ruby element -->
<![%Ruby.fallback;[
<!ENTITY % Ruby.content.simple "( rb, rp?, rt, rp? )" >
]]>
<!ENTITY % Ruby.content.simple "( rb, rt )" >

<![%Ruby.group;[
<!ENTITY % Ruby.content.group "| ( rbc, rtc, rtc? )" >
]]>
<!ENTITY % Ruby.content.group "" >

<!-- Content models of the rb and the rt elements are intended to
     allow other inline-level elements of its parent markup language,
     but it should not include ruby descendent elements. The following
     parameter entity %Noruby.content; can be used to redefine
     those content models with minimum effort.  It's defined as
     '( #PCDATA )' by default.
-->
<!ENTITY % Noruby.content "( #PCDATA )" >

<!-- one or more digits (NUMBER) -->
<!ENTITY % Number.datatype "CDATA" >

<!-- ruby element ...................................... -->

<!ENTITY % Ruby.element  "INCLUDE" >
<![%Ruby.element;[
<!ENTITY % Ruby.content
     "( %Ruby.content.simple; %Ruby.content.group; )"
>
<!ELEMENT ruby  %Ruby.content; >
<!-- end of Ruby.element -->]]>

<![%Ruby.group;[
<!-- rbc (ruby base component) element ................. -->

<!ENTITY % Rbc.element  "INCLUDE" >
<![%Rbc.element;[
<!ENTITY % Rbc.content
     "(rb)+"
>
<!ELEMENT rbc  %Rbc.content; >
<!-- end of Rbc.element -->]]>

<!-- rtc (ruby text component) element ................. -->

<!ENTITY % Rtc.element  "INCLUDE" >
<![%Rtc.element;[
<!ENTITY % Rtc.content
     "(rt)+"
>
<!ELEMENT rtc  %Rtc.content; >
<!-- end of Rtc.element -->]]>
]]>

<!-- rb (ruby base) element ............................ -->

<!ENTITY % Rb.element  "INCLUDE" >
<![%Rb.element;[
<!-- %Rb.content; uses %Noruby.content; as its content model,
     which is '( #PCDATA )' by default. It may be overridden
     by other modules to allow other inline-level elements
     of its parent markup language, but it should not include
     ruby descendent elements.
-->
<!ENTITY % Rb.content "%Noruby.content;" >
<!ELEMENT rb  %Rb.content; >
<!-- end of Rb.element -->]]>

<!-- rt (ruby text) element ............................ -->

<!ENTITY % Rt.element  "INCLUDE" >
<![%Rt.element;[
<!-- %Rt.content; uses %Noruby.content; as its content model,
     which is '( #PCDATA )' by default. It may be overridden
     by other modules to allow other inline-level elements
     of its parent markup language, but it should not include
     ruby descendent elements.
-->
<!ENTITY % Rt.content "%Noruby.content;" >

<!ELEMENT rt  %Rt.content; >
<!-- end of Rt.element -->]]>

<!-- rbspan attribute is used for group ruby only ...... -->
<![%Ruby.group;[
<!ENTITY % Rt.attlist  "INCLUDE" >
<![%Rt.attlist;[
<!ATTLIST rt
      rbspan         %Number.datatype;      "1"
>
<!-- end of Rt.attlist -->]]>
]]>

<!-- rp (ruby parenthesis) element ..................... -->

<![%Ruby.fallback;[
<!ENTITY % Rp.element  "INCLUDE" >
<![%Rp.element;[
<!ENTITY % Rp.content
     "( #PCDATA )"
>
<!ELEMENT rp  %Rp.content; >
<!-- end of Rp.element -->]]>
]]>

<!-- Ruby Common Attributes

     The following optional ATTLIST declarations provide an easy way
     to define common attributes for ruby elements.  These declarations
     are ignored by default.

     Ruby elements are intended to have common attributes of its
     parent markup language.  For example, if a markup language defines
     common attributes as a parameter entity %attrs;, you may add
     those attributes by just declaring the following parameter entities
     
         <!ENTITY % Ruby.common.attlists  "INCLUDE" >
         <!ENTITY % Ruby.common.attrib  "%attrs;" >

     before including the Ruby module.
-->

<!ENTITY % Ruby.common.attlists  "IGNORE" >
<![%Ruby.common.attlists;[
<!ENTITY % Ruby.common.attrib  "" >

<!-- common attributes for ruby ........................ -->

<!ENTITY % Ruby.common.attlist  "INCLUDE" >
<![%Ruby.common.attlist;[
<!ATTLIST ruby
      %Ruby.common.attrib;
>
<!-- end of Ruby.common.attlist -->]]>

<![%Ruby.group;[
<!-- common attributes for rbc ......................... -->

<!ENTITY % Rbc.common.attlist  "INCLUDE" >
<![%Rbc.common.attlist;[
<!ATTLIST rbc
      %Ruby.common.attrib;
>
<!-- end of Rbc.common.attlist -->]]>

<!-- common attributes for rtc ......................... -->

<!ENTITY % Rtc.common.attlist  "INCLUDE" >
<![%Rtc.common.attlist;[
<!ATTLIST rtc
      %Ruby.common.attrib;
>
<!-- end of Rtc.common.attlist -->]]>
]]>

<!-- common attributes for rb .......................... -->

<!ENTITY % Rb.common.attlist  "INCLUDE" >
<![%Rb.common.attlist;[
<!ATTLIST rb
      %Ruby.common.attrib;
>
<!-- end of Rb.common.attlist -->]]>

<!-- common attributes for rt .......................... -->

<!ENTITY % Rt.common.attlist  "INCLUDE" >
<![%Rt.common.attlist;[
<!ATTLIST rt
      %Ruby.common.attrib;
>
<!-- end of Rt.common.attlist -->]]>

<![%Ruby.fallback;[
<!-- common attributes for rp .......................... -->

<!ENTITY % Rp.common.attlist  "INCLUDE" >
<![%Rp.common.attlist;[
<!ATTLIST rp
      %Ruby.common.attrib;
>
<!-- end of Rp.common.attlist -->]]>
]]>
]]>

<!-- end of xhtml11-ruby-1.mod -->
