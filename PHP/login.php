<?php
require_once 'db_function.php';
$db = new DB_Functions();

require_once 'responseTemplate.php';

header('Content-Type: application/json');

$loginCheck=$db->checkLogin($_POST);

if ($loginCheck!==1){
    $response->STATUS=false;
    $response->STATUSMESSAGE="BAD REQUEST: BAD PARAMETER: ".$loginCheck;
    $response = json_encode($response);
    echo $response;
    return;
}

$loginCheck = $db->userExistsLogin($_POST);

if ($loginCheck==0){
    $response->STATUS=false;
    $response->STATUSMESSAGE="BAD REQUEST: USER DOESN'T EXIST";
    $response = json_encode($response);
    echo $response;
    return;
}

$loginCheck = $db->checkPassword($_POST);

if ($loginCheck[0]["Forbidden"]==true){
    $response->STATUS=false;
    $response->STATUSMESSAGE="FORBIDDEN: TOO MANY FAILED LOGINS: CONTACT ADMINISTRATOR";
    $response=json_encode($response);
    echo $response;
    return;
            
}
elseif ($loginCheck[0]["KrivePrijave"]!=0){
    $response->STATUS=false;
    $response->STATUSMESSAGE="UNAUTHORIZED: WRONG PASSWORD: ATTEMPT {$loginCheck[0]["KrivePrijave"]} OF 3";
    $response=json_encode($response);
    echo $response;
    return;
}
else {
    $response->STATUS=true;
    $response->STATUSMESSAGE="OK";
    $response->DATA=json_decode($loginCheck[1]);
    $response= json_encode($response);
    echo $response;
    return;
}

?>
