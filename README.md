# AIR1903

Ovo su upute koje sam koristio za podešavanje chmoda na mape.
Preuzete su sa: 
https://stackoverflow.com/questions/30639174/how-to-set-up-file-permissions-for-laravel

Your user as owner

I prefer to own all the directories and files (it makes working with everything much easier), so I do:

sudo chown -R my-user:www-data /path/to/your/laravel/root/directory

Then I give both myself and the webserver permissions:

sudo find /path/to/your/laravel/root/directory -type f -exec chmod 664 {} \;    
sudo find /path/to/your/laravel/root/directory -type d -exec chmod 775 {} \;

Then give the webserver the rights to read and write to storage and cache

Whichever way you set it up, then you need to give read and write permissions to the webserver for storage, cache and any other directories the webserver needs to upload or write too (depending on your situation), so run the commands from bashy above :

sudo chgrp -R www-data storage bootstrap/cache
sudo chmod -R ug+rwx storage bootstrap/cache

Now, you're secure and your website works, AND you can work with the files fairly easily