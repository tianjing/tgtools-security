/**
 * 
 */
try{
    if(!ServerPath)
    {
        ServerPath=serverpath;
    };
}
catch(e){var ServerPath=serverpath}

document.write('<script src="'+ServerPath+'webjars/com/github/tianjing/tgtools/encrypt/js/sm4/sm4enc.js" type="text/javascript" ></script>');
document.write('<script src="'+ServerPath+'admin/js/sm4utilsext.js" type="text/javascript" ></script>');
document.write('<script src="'+ServerPath+'admin/js/tgtoolsext.js" type="text/javascript" ></script>');