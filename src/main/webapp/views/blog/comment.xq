import module namespace web="http://basex.org/lib/web";


 if($POST('name') and $POST('your_comment')) 
    then
        let $red := web:redirect("/app/blog/", "You Comment has been saved") 
                return (
                    $red, 
                    blog:comment($POST('name'), $POST('your_comment'), $POST('uuid'))
                    )
    else 
        blog:err()
