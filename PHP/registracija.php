<?php

require_once 'db_function.php';
$db = new DB_Functions();

require_once 'responseTemplate.php';

header('Content-Type: application/json');

if (isset($_POST["CONFIRM"])){
    $response->STATUS=$db->confirmRegistration($_POST);
    if ($response->STATUS == true && ($_POST["CONFIRM"]==true || $_POST["CONFIRM"]==false)){
        if ($_POST["CONFIRM"]=="true"){
            $response->STATUSMESSAGE = "Account confirmed";
        }
        elseif ($_POST["CONFIRM"]=="false"){
            $response->STATUSMESSAGE = "Account unconfirmed";
        }
    }
    else{
        $response->STATUSMESSAGE = "User doesn't exist";
    }
    $response->DATA["KorisnickoIme"]=$_POST["KorisnickoIme"];
    $response = json_encode($response);
    echo $response;
    return;
}

$registerCheck=$db->checkRegisterEmpty($_POST);

if ($registerCheck!==1){
    $response->STATUS=false;
    $response->STATUSMESSAGE="{$registerCheck} can't be empty";
    $response = json_encode($response);
    echo $response;
    return;
}

$registerCheck=$db->checkRegister($_POST);

if ($registerCheck!==1){
    $response->STATUS=false;
    $response->STATUSMESSAGE="{$registerCheck} is invalid";
    $response = json_encode($response);
    echo $response;
    return;
}

$registerCheck = $db->userExistsRegister($_POST);

if ($registerCheck){
    $response->STATUS=false;
    $response->STATUSMESSAGE="{$registerCheck} already exists";
    $response = json_encode($response);
    echo $response;
    return;
}

$hash = $db->hashPassword($_POST);

$regUser = $db->storeUser($_POST, $hash);

$response->STATUS=true;
$response->STATUSMESSAGE= "Registration successful";

$response->DATA=$regUser;

$response = json_encode($response);
echo $response;
return;

?>