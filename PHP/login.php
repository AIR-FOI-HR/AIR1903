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
?>