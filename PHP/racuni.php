<?php
//ini_set('display_errors', 1);
//error_reporting(E_ALL); 
require_once 'db_function.php';
$db = new DB_Functions();
require_once 'responseTemplate.php';
header('Content-Type: application/json');
if ($db->checkAuth($_POST["Token"], $_POST["KorisnickoIme"])) {
    if (isset($_POST["Readall"]) && $_POST["Readall"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $invoices = $db->getAllInvoices($_POST);
        if ($invoices===false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER NOT IN STORE";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (empty($invoices)){
            $response->STATUSMESSAGE = "SUCCESS, NO INVOICES";
            $response->DATA = null;
        }
        else{
            $response->DATA = $invoices;
            $response->STATUSMESSAGE = "SUCCESS";
        }
        $response->STATUS = true;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }else if ($_POST["GENERATESALE"] == true){
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $productSell = $db->sellItems($_POST);
        if ($productSell==false){
            $response->STATUS=false;
            $response->STATUSMESSAGE = "MISSING AMOUNT";
            $response->DATA=null;
        }
        else{
            $response->STATUS=true;
            $response->STATUSMESSAGE = "INVOICE GENERATED";
            $response->DATA=$productSell;
        }
        $response= json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }else if ($_POST["CONFIRMSALE"] == true) {
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $loginCheck = $db->userConfirmed($_POST);
        if ($loginCheck==0){
            $response->STATUS=false;
            $response->STATUSMESSAGE="This user hasn't been confirmed yet. Please contact your admin.";
            $response = json_encode($response);
            echo $response;
            return;
        }
        $saleInvoice = $db->confirmSale($_POST);
        if ($saleInvoice== -1){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "MISSING AMOUNT";
            $response->DATA = null;
        }
        else if ($saleInvoice == -2){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "MISSING BALANCE";
            $response->DATA = null;
        }
        else{
            $response->STATUS = true;
            $response->STATUSMESSAGE = "INVOICE FINALIZED";
            $response->DATA = $saleInvoice;
        }
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
	else if ($_POST["CONFIRMSALEFROMCODE"] == true) {
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $loginCheck = $db->userConfirmed($_POST);
        if ($loginCheck==0){
            $response->STATUS=false;
            $response->STATUSMESSAGE="This user hasn't been confirmed yet. Please contact your admin.";
            $response = json_encode($response);
            echo $response;
            return;
        }
        $saleInvoice = $db->confirmSaleFromCode($_POST);
        if ($saleInvoice== -1){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "MISSING AMOUNT";
            $response->DATA = null;
        }
        else if ($saleInvoice == -2){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "MISSING BALANCE";
            $response->DATA = null;
        }
        else if ($saleInvoice == -3){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO BUYING FROM OWN STORE";
            $response->DATA = null;
        }
        else if ($saleInvoice == -4){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "INVALID CODE";
            $response->DATA = null;
        }
        else{
            $response->STATUS = true;
            $response->STATUSMESSAGE = "INVOICE FINALIZED";
            $response->DATA = $saleInvoice;
        }
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    else if (isset($_POST["DELETE"]) && $_POST["DELETE"] == true){
        $response->STATUS = true;
        $response->STATUSMESSAGE = "DELETED";
        $response->DATA=$db->deleteInvoice($_POST);
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    else if (isset($_POST["Readone"]) && $_POST["Readone"] == true){
        $userExists = $db->userExistsLogin($_POST);
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $loginCheck = $db->userConfirmed($_POST);
        if ($loginCheck==0){
            $response->STATUS=false;
            $response->STATUSMESSAGE="This user hasn't been confirmed yet. Please contact your admin.";
            $response = json_encode($response);
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
    return;
}
?>