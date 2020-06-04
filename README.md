# AIR1903


1. UPUTE ZA KOPIRANJE DOKUMENATA

Localhost -> Server

Kopirati:
public					   -> app
sve ostalo (i mapa app)	   -> app/laravel

Izmijeniti:
root/app/index.php (onaj što je localno bio u public) -> podesiti putanje do laravel mape na serveru u odnosu na sam index.php

npr, za ovu gore konfiguraciju te putanje bi trebale biti
require __DIR__.'/laravel/vendor/autoload.php';
$app = require_once __DIR__.'/laravel/bootstrap/app.php';

To sve znači da dokumenti koji su u app/laravel mogu biti bilo gdje, pa čak i izvan app mape (u npr root/laravel), ali onda bi u index.php
složiti putanju tipa __DIR__./../laravel...



2. UPUTE CHMOD
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