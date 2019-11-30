<?php
require_once 'db_function.php';
$db = new DB_Functions();
$isSeller = $db->isSeller($_POST);

?>

