<?php
require_once 'db_function.php';
$db = new DB_Functions();
header('Content-Type: application/json');
if (isset($_POST["GET"]) && $_POST["GET"] == true) {
        $allPackeges = $db->getAllPackeges($_POST);
        if ($allPackeges[0] == 1) {
            $response->DATA = null;
            $response->STATUS = false;
            $response->STATUSMESSAGE = "REGULAR USERS CAN'T READ";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
        } else {
            $response->DATA = $allPackeges[1];
            $response->STATUS = true;
            $response->STATUSMESSAGE = "OK";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
        }
?>