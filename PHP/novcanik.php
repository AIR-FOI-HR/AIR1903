<?php
//ini_set('display_errors', 1);
//error_reporting(E_ALL);

require_once 'db_function.php';
$db = new DB_Functions();
header('Content-Type: application/json');
require_once 'responseTemplate.php';

if ($db->checkAuth($_POST["Token"], $_POST["KorisnickoIme"])) {
    if (isset($_POST["GETCLIENT"]) && $_POST["GETCLIENT"] == true) {
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
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
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $store = $db->getStoreOfUser($_POST["KorisnickoIme"]);
        if ($store==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER NOT IN STORE";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
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
        foreach ($_POST["KorisnickoImeKorisnik"] as $korisnik){
            $p["KorisnickoIme"] = $korisnik;
            $userExists = $db->userExistsLogin($p);
            if ($userExists==false){
                $response->STATUS = false;
                $response->STATUSMESSAGE = "USER DOESN'T EXIST";
                $response->DATA["KorisnickoIme"]=$korisnik;
                $response = json_encode($response, JSON_UNESCAPED_UNICODE);
                echo $response;
                return;
            }
        }
        $setUserBalance = $db->setInitialBalance($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "BALANCE SET";
        $response->DATA["StanjeRacuna"] = $setUserBalance;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
    }
    if(isset($_POST["STATISTICS"])){
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $store = $db->getStoreOfUser($_POST["KorisnickoIme"]);
        if ($store==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER NOT IN STORE";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $dataForStatistics = $db->getSumOfInvoices($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "Ukupna cijena: ";
        $response->DATA = $dataForStatistics;
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