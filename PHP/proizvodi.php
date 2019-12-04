<?php


if($_SERVER['REQUEST_METHOD'] === 'GET'){
   $allProducts = $db->getAllProducts();
    $response->DATA = $allProducts;
    $response->STATUS = true;
    $response->STATUSMESSAGE = "OK";
    $response= json_encode($response);
    echo stripslashes($response);
}
 else {
  $productCheck = $db->checkProductEmpty($_POST);
  $isDelete = $db->isDelete($_POST);
  $isUpdate = $db->isUpdate($_POST);
    if ($productCheckCheck === 0) {
        $response->STATUS = false;
        $response->STATUSMESSAGE = "Niste unijeli Naziv, Opis ili Cijenu proizvoda! " . $registerCheck;
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
    }else if ($isDelete === 1) {
        $deleteProduct = $db->deleteProduct($_POST);
        $response->ID = $deleteProduct;
        $response->STATUS = true;
        $response->STATUSMESSAGE = "Proizvod jest uspješno izbrisan!";
        $response = json_encode($response);
        return;
    }
}

?>

