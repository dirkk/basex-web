import module namespace web="http://basex.org/lib/web";


 if($POST('name') and $POST('your_comment')) 
    then
        let $red := web:redirect(fn:concat("/app/blog/view/?entry=",$POST('uuid')), fn:concat($POST('name'), " ", "Your Comment has been saved")) 
                return (
                    $red, 
                    blog:comment($POST('name'), $POST('your_comment'), $POST('uuid'))
                    )
    else 
        blog:err()
