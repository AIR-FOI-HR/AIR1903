<?php

require_once 'db_function.php';
$db = new DB_Functions();
header('Content-Type: application/json');

if ($db->checkAuth($_POST["Token"])) {
    
}
?>


