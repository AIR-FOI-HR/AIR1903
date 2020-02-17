<?php
require_once 'db_function.php';
$db = new DB_Functions();

require_once 'responseTemplate.php';

header('Content-Type: application/json');

$loginCheck=$db->checkLogin($_POST);

if ($loginCheck!==1){
    $response->STATUS=false;
    $response->STATUSMESSAGE="{$loginCheck} can't be empty";
    $response = json_encode($response);
    echo $response;
    return;
}

$loginCheck = $db->userExistsLogin($_POST);

if ($loginCheck==0){
    $response->STATUS=false;
    $response->STATUSMESSAGE="This user doesn't exist. Please register first";
    $response = json_encode($response);
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

$loginCheck = $db->checkPassword($_POST);

if ($loginCheck[0]["Forbidden"]==true){
    $response->STATUS=false;
    $response->STATUSMESSAGE="This account is locked. Login failed 3 or more times. Contact your administrator.";
    $response=json_encode($response);
    echo $response;
    return;
            
}
elseif ($loginCheck[0]["KrivePrijave"]!=0){
    $response->STATUS=false;
    $response->STATUSMESSAGE="Wrong password. Login attempt {$loginCheck[0]["KrivePrijave"]} of 3";
    $response=json_encode($response);
    echo $response;
    return;
}
else {
    $response->STATUS=true;
    $response->STATUSMESSAGE="Login successful";
    $response->DATA=json_decode($loginCheck[1]);
    $response= json_encode($response);
    echo $response;
    return;
}

?>
