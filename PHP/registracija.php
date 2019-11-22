<?php

require_once 'db_function.php';
$db = new DB_Functions();

require_once 'responseTemplate.php';

header('Content-Type: application/json');
        
$registerCheck=$db->checkRegisterEmpty($_POST);

if ($registerCheck!==1){
    $response->STATUS=false;
    $response->STATUSMESSAGE="BAD REQUEST: BAD PARAMETER: ".$registerCheck;
    $response = json_encode($response);
    echo $response;
    return;
}

$registerCheck=$db->checkRegister($_POST);

if ($registerCheck!==1){
    $response->STATUS=false;
    $response->STATUSMESSAGE="BAD REQUEST: BAD PARAMETER: ".$registerCheck;
    $response = json_encode($response);
    echo $response;
    return;
}

$registerCheck = $db->userExistsRegister($_POST);

if ($registerCheck){
    $response->STATUS=false;
    $response->STATUSMESSAGE="BAD REQUEST: ALREADY EXISTS: ".$registerCheck;
    $response = json_encode($response);
    echo $response;
    return;
}

$hash = $db->hashPassword($_POST);

$regUser = $db->storeUser($_POST, $hash);

$response->STATUS=true;
$response->STATUSMESSAGE= "OK";

$response2->USERNAME=$regUser;
$response->DATA=$response2;

$response = json_encode($response);
echo $response;
return;

?>