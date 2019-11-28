<?php

require_once 'db_function.php';
$db = new DB_Functions();

if($_SERVER['REQUEST_METHOD'] === 'GET'){
$allProducts = $db->getAllProducts();
$allProducts = json_encode(array('data' => $allProducts));
echo ($allProducts);
}
 else {
    $response->STATUS = true;
    $response->STATUSMESSAGE = "OK";
    $newProduct = $db->addNewProduct($_POST);
    $response2->NAME = $newProduct;
    $response2 = json_encode($response2);
    $response->DATA = $response2;
   
}

?>

