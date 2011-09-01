import module namespace web="http://basex.org/lib/web";

<h1>My Blog!</h1>,web:flash(),
for $entry in blog:entries()
    return 
    <div class="post">
        <h3>{$entry/title/text()} <small>{" ", data($entry/@date)}</small></h3>
        <p>{substring($entry/body/text(),0,100), "… "}
        <a href="/app/blog/view?entry={$entry/@uuid}" class="right">→ more</a>
        </p>
        <hr />
    </div>,
            <script language="javascript">
    <![CDATA[
    // get some data, convert to JSON
          dojo.xhrGet({
              url:"/app/blog/view?entry=5c5b3629-d839-436d-a14a-da3258902b55",
              handleAs:"xml",
              load: function(data){
                  console.log("Data loaded");
                  console.log(data);
              }
          });

    ]]>      
    </script>