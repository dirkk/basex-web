import module namespace gb = "http://basex.org/guests" at "src/main/webapp/guestbook/lib/guestbook.xq";
import module namespace web="http://basex.org/lib/web";

declare option output:omit-xml-declaration "no";
declare option output:method "xhtml";
declare option output:include-content-type "yes";
declare option output:doctype-public "-//W3C//DTD XHTML 1.1//EN";
declare option output:doctype-system "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd";
<html>
<head>
  <title>Guestbook Demo </title>
  <meta name="author" content="Michael Seiferle" />
  <style type="text/css">
  small {{ float:left; padding-right:5px;}}
  </style>
</head>
<body>
    {web:flash()}
  <div id="container">
  <div class="form">
  <h3>Add a new entry</h3>
  <form action="add.xq" method="post" accept-charset="utf-8">
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
        gb:list()
    }
  
  </div>
  </div>
</body>
</html>