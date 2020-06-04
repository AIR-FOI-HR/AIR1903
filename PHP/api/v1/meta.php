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
        $authorised = $db->isAdmin($_POST["KorisnickoIme"]);
        if ($authorised==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "UNAUTHORISED";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA = $db->getAllEvents($_POST);
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    if (isset($_POST["CREATEEVENT"]) && $_POST["CREATEEVENT"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
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
        if (!isset($_POST["NazivEventa"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO EVENT NAME";
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
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA = $db->createEvent($_POST);
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    if (isset($_POST["GETCURRENTEVENT"]) && $_POST["GETCURRENTEVENT"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
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
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA = $db->getCurrentEvent();
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    if (isset($_POST["SETCURRENTEVENT"]) && $_POST["SETCURRENTEVENT"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
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
        if (!isset($_POST["Id_Eventa"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO EVENT ID";
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
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA = $db->setCurrentEvent($_POST);
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["EDITEVENT"]) && $_POST["EDITEVENT"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
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
        $userExists = $db->userExistsLogin($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["Id_Eventa"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO EVENT ID";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["Naziv_Eventa"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO EVENT ID";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $edit = $db->editEvent($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "EVENT EDITED";
        $response->DATA = $edit;
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