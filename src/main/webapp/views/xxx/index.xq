<div class="form">
<form action="/app/xxx/save" method="post" accept-charset="utf-8">
  <label for="phone">
      Phone number, no spaces
  </label>
 
  <input type="text" name="phone" id="phone" value="someTestString" dojoType="dijit.form.ValidationTextBox"
  regExp="[\w]+" required="true" invalidMessage="Invalid Non-Space Text." />

  <p><input type="submit" value="Continue →" /></p>
</form>
</div>
