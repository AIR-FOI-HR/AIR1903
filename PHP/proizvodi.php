<?php

/* ini_set('display_errors', 1);
  error_reporting(E_ALL); */

require_once 'db_function.php';
$db = new DB_Functions();

header('Content-Type: application/json');


if ($db->checkAuth($_POST["Token"])) {
    if (isset($_POST["Readall"]) && $_POST["Readall"] == true) {
        $allProducts = $db->getAllProducts($_POST);
        if ($allProducts[0]==1){
            $response->DATA = null;
            $response->STATUS=false;
            $response->STATUSMESSAGE="REGULAR USERS CAN'T READ";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
        }
        else{
        $response->DATA = $allProducts[1];
        $response->STATUS = true;
        $response->STATUSMESSAGE = "OK";
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        }
    } else {
        $productCheck = $db->checkProductEmpty($_POST);
        $isDelete = $db->isDelete($_POST);
        $isUpdate = $db->isUpdate($_POST);
        if ($productCheckk === 0) {
            $response->STATUS = false;
            $response->STATUSMESSAGE = "Niste unijeli jedan od potrebnih parametara: ";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        } else if ($productCheck === 1) {
            $newProduct = $db->addNewProduct($_POST);
            if ($newProduct[0]==1){
                $response->STATUS = false;
                $response->STATUSMESSAGE = "REGULAR USER CAN'T ADD";
                $response->DATA=null;
                echo json_encode($response, JSON_UNESCAPED_UNICODE);
                return;
            }
            else{
                $response->STATUS = true;
                $response->STATUSMESSAGE = "SUCCESS";
                $response->DATA = $newProduct[1];
            }
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        } else if ($isDelete === 1) {
            $deleteProduct = $db->deleteProduct($_POST);
            $response->STATUS = true;
            $response->STATUSMESSAGE = "DELETED";
            $response->DATA=null;
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        } else if ($_POST["Edit"]==true) {
            $updateProduct = $db->updateProduct($_POST);
            $response2["Naziv"]=$_POST["Naziv"];
            $response2["Opis"]=$_POST["Opis"];
            $response2["Cijena"]=$_POST["Cijena"];
            $response2["Slika"]=$_POST["Slika"];
            
            $response->DATA = $response2;
            $response->STATUS = true;
            $response->STATUSMESSAGE = "UPDATED";
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