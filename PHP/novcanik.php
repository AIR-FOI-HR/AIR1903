<?php
//ini_set('display_errors', 1);
//error_reporting(E_ALL);

require_once 'db_function.php';
$db = new DB_Functions();
header('Content-Type: application/json');
require_once 'responseTemplate.php';
if ($db->checkAuth($_POST["Token"])) {
    if (isset($_POST["GETCLIENT"]) && $_POST["GETCLIENT"] == true) {
        $userBalance = $db->getBalance($_POST);
        if ($userBalance==-1){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $response->STATUS = true;
        $response->STATUSMESSAGE = "Stanje računa: ";
        $response->DATA = $userBalance;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    if (isset($_POST["GETSTORE"]) && $_POST["GETSTORE"] == true) {
        $userBalance = $db->getBalanceStore($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "Stanje računa: ";
        $response->DATA = $userBalance;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    if(isset($_POST["SET"])){
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["KorisnickoImeKorisnik"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USER USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $authorised = $db->isAdmin($_POST["KorisnickoIme"]);
        if ($authorised==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "UNAUTHORISED";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $p["KorisnickoIme"] = $_POST["KorisnickoImeKorisnik"];//gitnew
        $userExists = $db->userExistsLogin($p);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $setUserBalance = $db->setInitialBalance($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "BALANCE SET";
        $response->DATA["StanjeRacuna"] = $setUserBalance;
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
        return;
    }
}
?>