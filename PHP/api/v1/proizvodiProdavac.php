<?php
require_once 'db_function.php';
$db = new DB_Functions();
$isSeller = $db->isSeller($_POST);

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $sellerProducts = $db->getSellerProducts($_POST);
    $response->DATA = $sellerProducts;
    $response->STATUS = true;
    $response->STATUSMESSAGE = "OK";
    $response= json_encode($response);
    echo stripslashes($response);
}

?>

