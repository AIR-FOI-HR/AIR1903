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
        $users = $db->getAllUsers($_POST);
        if ($users==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "UNAUTHORISED";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA = $users;
        
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["SETROLETRADER"]) && $_POST["SETROLETRADER"] == true) {
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
        $p["KorisnickoIme"] = $_POST["KorisnickoImeKorisnik"];
        $userExists = $db->userExistsLogin($p);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        $db->setRoleTrader($_POST);
        $response->STATUS=true;
        $response->STATUSMESSAGE = "TRADER ADDED";
        $response->DATA["KorisnickoImeKorisnik"]=$_POST["KorisnickoImeKorisnik"];
        $response->DATA["Id_Trgovine"]=$_POST["Id_Trgovine"];
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["SETROLECUSTOMER"]) && $_POST["SETROLECUSTOMER"] == true) {
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
        $p["KorisnickoIme"] = $_POST["KorisnickoImeKorisnik"];
        $userExists = $db->userExistsLogin($p);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        $db->setRoleCustomer($_POST);
        $response->STATUS=true;
        $response->STATUSMESSAGE = "CUSTOMER ADDED";
        $response->DATA["KorisnickoImeKorisnik"]=$_POST["KorisnickoImeKorisnik"];
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["SETROLEADMIN"]) && $_POST["SETROLEADMIN"] == true) {
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
        $p["KorisnickoIme"] = $_POST["KorisnickoImeKorisnik"];
        $userExists = $db->userExistsLogin($p);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        $db->setRoleAdmin($_POST);
        $response->STATUS=true;
        $response->STATUSMESSAGE = "ADMIN ADDED";
        $response->DATA["KorisnickoImeKorisnik"]=$_POST["KorisnickoImeKorisnik"];
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