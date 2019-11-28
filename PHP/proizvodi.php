<?php

require_once 'db_function.php';
$db = new DB_Functions();

if($_SERVER['REQUEST_METHOD'] === 'GET'){
$allProducts = $db->getAllProducts();
$allProducts = json_encode(array('data' => $allProducts));
echo ($allProducts);
}
 else {
  $productCheck = $db->checkProductEmpty($_POST);
    if ($productCheckCheck === 0) {
        $response->STATUS = false;
        $response->STATUSMESSAGE = "BAD REQUEST: BAD PARAMETER: " . $registerCheck;
        $response = json_encode($response);
        echo $response;
        return;
    } else if ($productCheck === 1) {
        $response->STATUS = true;
        $response->STATUSMESSAGE = "OK";
        $newProduct = $db->addNewProduct($_POST);
        $response2->NAME = $newProduct;
        $response2 = json_encode($response2);
        $response->DATA = $response2;
    }
}

?>

