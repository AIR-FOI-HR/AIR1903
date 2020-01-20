<?php
//ini_set('display_errors', 1);
//error_reporting(E_ALL); 
require_once 'db_function.php';
$db = new DB_Functions();
header('Content-Type: application/json');
if ($db->checkAuth($_POST["Token"])) {
    if (isset($_POST["Readall"]) && $_POST["Readall"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA = $db->getAllInvoices($_POST);
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }else if ($_POST["GENERATESALE"] == true){
        $productSell = $db->sellItems($_POST);
        $response->STATUS=true;
        $response->STATUSMESSAGE = "INVOICE GENERATED";
        $response->DATA=$productSell;
        $response= json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }else if ($_POST["CONFIRMSALE"] == true) {
        $changeSellerAndBuyerBallance = $db->confirmSale($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "INVOICE FINALIZED";
        $response->DATA = $changeSellerAndBuyerBallance;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    else if (isset($_POST["Readone"]) && $_POST["Readone"] == true){
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA = $db->getInvoice($_POST);
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
}
else{
    $response->STATUS=false;
    $response->STATUSMESSAGE="OLD TOKEN";
    echo json_encode($response);
}
?>