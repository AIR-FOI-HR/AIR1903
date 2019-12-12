<?php
require_once 'db_function.php';
$db = new DB_Functions();
header('Content-Type: application/json');
if (isset($_POST["GET"]) && $_POST["GET"] == true) {
        $allPackeges = $db->getAllPackeges($_POST);
        if ($allPackeges[0] == 1) {
            $response->DATA = null;
            $response->STATUS = false;
            $response->STATUSMESSAGE = "REGULAR USERS CAN'T READ";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
        } else {
            $response->DATA = $allPackeges[1];
            $response->STATUS = true;
            $response->STATUSMESSAGE = "OK";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
        }
} else if ($_POST["ADD"] == true) {
        if ($packageCheck === 0) {
            $response->STATUS = false;
            $response->STATUSMESSAGE = "Niste unijeli jedan od potrebnih parametara: ";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        } else if ($packageCheck === 1) {
            $newPackage = $db->addNewPackage($_POST);
            $response->STATUS = true;
            $response->STATUSMESSAGE = "SUCCESS";
            $response->DATA = $newPackage;
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        } 
} else if ($_POST["DELETE"] == true) {
        $deleteProduct = $db->deletePackage($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "DELETED";
        $response->DATA = null;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
 } else if ($_POST["UPDATE"] == true) {
        $updatePackage = $db->updatePackage($_POST);
        $response2["NazivPaketa"] = $_POST["NazivPaketa"];
        $response2["Id_Proizvoda"] = $_POST["Id_Proizvoda"];
        $response2["Kolicina"] = $_POST["Kolicina"];
        $response2["Popust"] = $_POST["Popust"];

        $response->DATA = $response2;
        $response->STATUS = true;
        $response->STATUSMESSAGE = "UPDATED";
        $response = json_encode($response);
        echo $response;
    }
?>