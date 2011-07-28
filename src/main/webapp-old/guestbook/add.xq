import module namespace gb = "http://basex.org/guests" at "src/main/webapp/guestbook/lib/guestbook.xq";
import module namespace web="http://basex.org/lib/web";
declare option output:omit-xml-declaration "no";
declare option output:method "xhtml";
declare option output:include-content-type "yes";
declare option output:doctype-public "-//W3C//DTD XHTML 1.1//EN";
declare option output:doctype-system "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd";


 if($POST('name') and $POST('message')) 
    then
        let $red := web:redirect("/guestbook/index.xq", "You Message has been saved")
        return ($red, 
        gb:add($POST('name'), $POST('message')))

    else 
        gb:err()