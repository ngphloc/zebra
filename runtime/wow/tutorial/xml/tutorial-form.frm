<!DOCTYPE html SYSTEM "/wow/WOWstandard/xhtml-wowext-1.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<object data="../header.xhtml" type="text/wow"> </object>
<h2>WOW! Tutorial Form Demo</h2>
<form method="post" action="/wow/ViewGet/FormProcess">

<p>How well do you know the topic of authoring for WOW! by now?<br></br>
 <input type="radio" name="Element6.authoring.knowledge" value="0" >not at all</input><br></br>
 <input type="radio" name="Element6.authoring.knowledge" value="25" >a little bit</input><br></br>
 <input type="radio" checked="true" name="Element6.authoring.knowledge" value="50" >about average I guess</input><br></br>
 <input type="radio" name="Element6.authoring.knowledge" value="75" >quite well</input><br></br>
 <input type="radio" name="Element6.authoring.knowledge" value="100" >completely</input> 
</p> 

<p>Please rate your knowlege of the form editor, between 0 and 100:
 <input name="Element7.formeditor.knowledge" size="3" maxlength="3" type="int" default="50"></input> 
</p> 


<p>Watch the effect of changing the value of the knowledge for the concept "welcome":
 <input name="Element8.welcome.knowledge" size="3" maxlength="3" type="int" default="100"></input> 
</p> 

<p>Are you interested in contributing to the WOW! project?
 <select name="Element9.contribute.interest" size="1" > 
	 <option value="0" name="0">nope</option>
	 <option value="50" name="1">maybe</option>
	 <option value="100" name="2">count me in!</option>
</select> 
 </p> 

<p><input type="submit" value="Update User Model Now!"> </input> 
 </p>
</form><br></br><object data="../footer.xhtml" type="text/wow"></object>
</html>
