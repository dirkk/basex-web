import module namespace web="http://basex.org/lib/web";


 if($POST('name') and $POST('message')) 
    then
        let $red := web:redirect("/app/guestbook/", "You Message has been saved")
        return ($red, 
        guestbook:add($POST('name'), $POST('message')))

    else 
        guestbook:err()
