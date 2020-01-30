<?php
//ini_set('display_errors', 1);
//error_reporting(E_ALL);

require_once 'db_function.php';
$db = new DB_Functions();
header('Content-Type: application/json');

if ($db->checkAuth($_POST["Token"])) {
    if (isset($_POST["GETCLIENT"]) && $_POST["GETCLIENT"] == true) {
        $userBalance = $db->getBalance($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "Stanje računa: ";
        $response->DATA = $userBalance;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
    }
    if (isset($_POST["GETSTORE"]) && $_POST["GETSTORE"] == true) {
        $userBalance = $db->getBalanceStore($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "Stanje računa: ";
        $response->DATA = $userBalance;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
    }
    if(isset($_POST["SET"])){
        $setUserBalance = $db->setInitialBalance($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "Stanje računa: ";
        $response->DATA = $setUserBalance;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
    }
    if(isset($_POST["SELL"])){
        $sellProducts = $db->sellItems($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "Stanje računa: ";
        $response->DATA = $sellProducts;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
    }
}
?>


