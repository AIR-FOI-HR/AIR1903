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
        $authorised = $db->isAdmin($_POST["KorisnickoIme"]);
        if ($authorised==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "UNAUTHORISED";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $users = $db->getAllUsers($_POST);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA = $users;
        
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["GETROLES"]) && $_POST["GETROLES"] == true) {
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
        $loginCheck = $db->userConfirmed($_POST);
        if ($loginCheck==0){
            $response->STATUS=false;
            $response->STATUSMESSAGE="This user hasn't been confirmed yet. Please contact your admin.";
            $response = json_encode($response);
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
        
        $db->getRoles();
        $response->STATUS=true;
        $response->STATUSMESSAGE = "ROLES GOTTEN";
        $roles = $db->getRoles();
        $response->DATA=$roles;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    
    if (isset($_POST["SETROLE"]) && $_POST["SETROLE"] == true) {
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
        if (!isset($_POST["RoleId"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO ROLE";
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
        $loginCheck = $db->userConfirmed($p);
        if ($loginCheck==0){
            $response->STATUS=false;
            $response->STATUSMESSAGE="This user hasn't been confirmed yet. Please contact your admin.";
            $response = json_encode($response);
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
        
        $role=$db->setRole($_POST);
        $response->STATUS=true;
        $response->STATUSMESSAGE = "ROLE SET";
        $response->DATA["KorisnickoImeKorisnik"]=$_POST["KorisnickoImeKorisnik"];
        $response->DATA["Uloga"]=$role;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    
    if (isset($_POST["DELETE"]) && $_POST["DELETE"] == true) {
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
        
        $db->deleteUser($_POST);
        $response->STATUS=true;
        $response->STATUSMESSAGE = "DELETED";
        $response->DATA["KorisnickoImeKorisnik"]=$_POST["KorisnickoImeKorisnik"];
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["EDITUSER"]) && $_POST["EDITUSER"] == true) {
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
        $userExists = $db->checkExistsById($_POST);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["Id_Korisnika"]) || empty($_POST["Id_Korisnika"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USER ID";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["KorisnickoImeKorisnik"]) || empty($_POST["KorisnickoImeKorisnik"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USER USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (strlen($_POST["KorisnickoImeKorisnik"])<5){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER USERNAME TOO SHORT";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        if (!isset($_POST["Ime"]) || empty($_POST["Ime"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO NAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (strlen($_POST["Ime"])<3){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NAME TOO SHORT";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        if (!isset($_POST["Prezime"]) || empty($_POST["Prezime"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO SURNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (strlen($_POST["Prezime"])<3){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "SURNAME TOO SHORT";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        if (!isset($_POST["Email"]) || empty($_POST["Email"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO EMAIL";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if(!preg_match("/(\w+\.)*(\w+)@(\w+\.){1,2}(\w{2,5})/", $_POST["Email"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "BAD EMAIL";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $edit = $db->editUser($_POST);
        if ($edit===-1){
            echo "Username exists already";
            return;
        }
        if ($edit===-2){
            echo "Email exists already";
            return;
        }
        $response->STATUS=true;
        $response->STATUSMESSAGE = "USER EDITED";
        $response->DATA=$edit;
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["SETLANGUAGE"]) && $_POST["SETLANGUAGE"] == true) {
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
        if (!isset($_POST["Jezik"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO LANGUAGE";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        $lang=$db->setLanguage($_POST);
        $response->STATUS=true;
        $response->STATUSMESSAGE = "LANGUAGE SET";
        $response->DATA["KorisnickoIme"]=$_POST["KorisnickoIme"];
        $response->DATA["Jezik"]=$_POST["Jezik"];
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    if (isset($_POST["SETOWNROLE"]) && $_POST["SETOWNROLE"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["RoleId"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO ROLE";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $admin = $db->isAdmin($_POST["KorisnickoIme"]);
        if ($admin==true){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "ADMINS SHOULDN'T USE THIS CALL";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if ($_POST["RoleId"]==2){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "LOL YOU CAN'T MAKE YOURSELF ADMIN";
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
        
        $role=$db->setOwnRole($_POST);
        $response->STATUS=true;
        $response->STATUSMESSAGE = "OWN ROLE SET";
        $response->DATA["KorisnickoIme"]=$_POST["KorisnickoIme"];
        $response->DATA["Uloga"]=$role;
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