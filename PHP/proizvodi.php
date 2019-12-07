<?php

/* ini_set('display_errors', 1);
  error_reporting(E_ALL); */

require_once 'db_function.php';
$db = new DB_Functions();

header('Content-Type: application/json');


if ($db->checkAuth($_POST["Token"])) {

    if (isset($_POST["Readall"]) && $_POST["Readall"] == true) {
        $allProducts = $db->getAllProducts();
        $response->DATA = json_decode($allProducts);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "OK";
        $response = json_encode($response);
        echo $response;
    } else {
        $productCheck = $db->checkProductEmpty($_POST);
        $isDelete = $db->isDelete($_POST);
        $isUpdate = $db->isUpdate($_POST);

        if ($productCheck === 0) {
            $response->STATUS = false;
            $response->STATUSMESSAGE = "Niste unijeli jedan od potrebnih parametara: ";
            $response = json_encode($response);
            echo $response;
            return;
        } else if ($productCheck === 1) {
            $newProduct = $db->addNewProduct($_POST);
            $response->STATUS = true;
            $response->STATUSMESSAGE = "Proizvod uspjesno dodan!";
            $response->DATA = $newProduct;
            $response = json_encode($response);
            echo $response;
            return;
        } else if ($isDelete === 1) {
            $deleteProduct = $db->deleteProduct($_POST);
            $response->STATUS = true;
            $response->STATUSMESSAGE = "Proizvod jest uspješno izbrisan!";
            $response = json_encode($response);
            echo $response;
            return;
        } else if ($isUpdate === 1) {
            $updateProduct = $db->updateProduct($_POST);
            $response->DATA = $updateProduct;
            $response->STATUS = true;
            $response->STATUSMESSAGE = "OK";
            $response = json_encode($response);
            echo $response;
        }
    }
}
else{
    $response->STATUS=false;
    $response->STATUSMESSAGE="OLD TOKEN";
    echo json_encode($response);
}
?>