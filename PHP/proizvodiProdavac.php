<?php
require_once 'db_function.php';
$db = new DB_Functions();
$isSeller = $db->isSeller($_POST);

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $sellerProducts = $db->getSellerProducts($_POST);
    $sellerProducts = json_encode(array('data' => $sellerProducts));
    echo ($sellerProducts);
}

?>

