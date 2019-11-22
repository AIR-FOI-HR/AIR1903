<?php
require_once 'db_function.php';
$db = new DB_Functions();

require_once 'responseTemplate.php';

$loginCheck=$db->checkLogin($_POST);

if ($loginCheck!==1){
    $response->STATUS=400;
    $response->STATUSMESSAGE="BAD REQUEST: BAD PARAMETER: ".$loginCheck;
    $response = json_encode($response);
    echo $response;
    return;
}

$loginCheck = $db->userExistsLogin($_POST);

if ($loginCheck==0){
    $response->STATUS=400;
    $response->STATUSMESSAGE="BAD REQUEST: USER DOESN'T EXIST";
    $response = json_encode($response);
    echo $response;
    return;
}

$loginCheck = $db->checkPassword($_POST);

if ($loginCheck===0){
    $response->STATUS=401;
    $response->STATUSMESSAGE="UNAUTHORIZED: WRONG PASSWORD";
    $response=json_encode($response);
    echo $response;
    return;
}
else {
    $response->STATUS=200;
    $response->STATUSMESSAGE="OK";
    $response->DATA=$loginCheck;
    $response= json_encode($response);
    echo $response;
    return;
}
?>