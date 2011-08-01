import module namespace web="http://basex.org/lib/web";

web:flash(),
<div class="guestbook">  <div class="form">
  <h3>Add a new entry</h3>
  <form action="/app/guestbook/add" method="post" accept-charset="utf-8">
    <label for="name">Your Name</label><br /><input type="text" name="name" value="" id="name" /> <br/>
    <label for="message">Your Message</label><br />
    <textarea name="message">Enter your message...</textarea>
    <p><input type="submit" value="Continue →" /></p>
  </form>
  </div> 
  <h1>Hello World.</h1>
  <h2>Guestbook Demo Application</h2>
  <div class="entries">
    {
        guestbook:list()
    }
   
  </div>
</div>