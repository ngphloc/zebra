<!-- ...................................................................... -->
<!-- WOWTags Extension Model Module  .................................... -->
<!-- file: xhtml-wowext-model-1.mod

     SYSTEM "xhtml-wowext-model-1.mod"
     ...................................................................... -->

<!-- Define the content model for Misc.extra -->
<!ENTITY % Misc.class
     "| %script.qname; | %noscript.qname; | %Extension.store.qname; ">

<!-- ....................  Inline Elements  ...................... -->

<!ENTITY % HeadOpts.mix  
     "( %meta.qname; )*" >

<!ENTITY % I18n.class "" >

<!ENTITY % InlStruct.class "%br.qname; | %span.qname;" >

<!ENTITY % InlPhras.class
     "| %em.qname; | %strong.qname; | %dfn.qname; | %code.qname; 
      | %samp.qname; | %kbd.qname; | %var.qname; | %cite.qname; 
      | %abbr.qname; | %acronym.qname; | %q.qname;" >

<!ENTITY % InlPres.class
     "| %tt.qname; | %i.qname; | %b.qname; | %big.qname; 
      | %small.qname; | %sub.qname; | %sup.qname;" >

<!ENTITY % Anchor.class "| %a.qname;" >

<!ENTITY % InlSpecial.class "| %img.qname; " >

<!ENTITY % Inline.extra "" >

<!-- %Inline.class; includes all inline elements,
     used as a component in mixes
-->
<!ENTITY % Inline.class
     "%InlStruct.class;
      %InlPhras.class;
      %InlPres.class;
      %Anchor.class;
      %InlSpecial.class;"
>

<!-- %InlNoAnchor.class; includes all non-anchor inlines,
     used as a component in mixes
-->
<!ENTITY % InlNoAnchor.class
     "%InlStruct.class;
      %InlPhras.class;
      %InlPres.class;
      %InlSpecial.class;"
>

<!-- %InlNoAnchor.mix; includes all non-anchor inlines
-->
<!ENTITY % InlNoAnchor.mix
     "%InlNoAnchor.class;
      %Misc.class;"
>

<!-- %Inline.mix; includes all inline elements, including %Misc.class;
-->
<!ENTITY % Inline.mix
     "%Inline.class;
      %Misc.class;"
>

<!-- .....................  Block Elements  ...................... -->

<!ENTITY % Heading.class 
     "%h1.qname; | %h2.qname; | %h3.qname; 
      | %h4.qname; | %h5.qname; | %h6.qname;" >

<!ENTITY % List.class "%ul.qname; | %ol.qname; | %dl.qname;" >

<!ENTITY % Blkstruct.class "%p.qname; | %div.qname;" >

<!ENTITY % Blkphras.class 
     "| %pre.qname; | %blockquote.qname; | %address.qname;" >

<!ENTITY % Blkpres.class "| %hr.qname;" >

<!ENTITY % Block.extra "" >

<!-- %Block.class; includes all block elements,
     used as an component in mixes
-->
<!ENTITY % Block.class
     "%Blkstruct.class;
      %Blkphras.class;
      %Blkpres.class;
      %Block.extra;"
>

<!-- %Block.mix; includes all block elements plus %Misc.class;
-->
<!ENTITY % Block.mix
     "%Heading.class;
      | %List.class;
      | %Block.class;
      %Misc.class;"
>

<!-- ................  All Content Elements  .................. -->

<!-- %Flow.mix; includes all text content, block and inline
-->
<!ENTITY % Flow.mix
     "%Heading.class;
      | %List.class;
      | %Block.class;
      | %Inline.class;
      %Misc.class;"
>

<!-- end of xhtml-wowext-model-1.mod -->

