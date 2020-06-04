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
        $role = $db->getUserRole($_POST["KorisnickoIme"]);
        if ($authorised==false && $role!=1){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "UNAUTHORISED";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $stores = $db->getStores($role);
        $response->STATUS = true;
        $response->STATUSMESSAGE = "SUCCESS";
        $response->DATA= $stores;
        
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    if (isset($_POST["ASSIGNSTORE"]) && $_POST["ASSIGNSTORE"] == true) {
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
        $p["KorisnickoIme"] = $_POST["KorisnickoImeKorisnik"];
        $userExists = $db->userExistsLogin($p);
        if ($userExists==false){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "USER DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if ((!isset($_POST["Id_Trgovine"]) || empty($_POST["Id_Trgovine"])) && $_POST["ASSIGNSTORE"]=="true"){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO STORE";
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
        
        $assigned = $db->assignStore($_POST);
        if ($assigned==1){
            $response->STATUSMESSAGE = "STORE ASSIGNED";
            $response->DATA["KorisnickoImeKorisnik"]=$_POST["KorisnickoImeKorisnik"];
            $response->DATA["Id_Trgovine"]=$_POST["Id_Trgovine"];
            $response->DATA["Naziv_Trgovine"]=$db->getStoreName($_POST["Id_Trgovine"]);
        }
        elseif($assigned==0){
            $response->STATUSMESSAGE = "STORE UNASSIGNED";
        }
        elseif($assigned==-1){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "STORE DOESN'T EXIST";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        $response->STATUS = true;
        
        
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    if (isset($_POST["CREATESTORE"]) && $_POST["CREATESTORE"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["NazivTrgovine"]) || empty($_POST["NazivTrgovine"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO STORE NAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $authorised = $db->isAdmin($_POST["KorisnickoIme"]);
        $role = $db->getUserRole($_POST["KorisnickoIme"]);
        if ($authorised==false && $role!=3){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "UNAUTHORISED";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        
        $created = $db->createStore($_POST);
        if ($created == false){
            $response->STATUSMESSAGE = "STORE ALREADY EXISTS";
            $response->STATUS = false;
            $response->DATA["NazivTrgovine"]=$_POST["NazivTrgovine"];
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        
        if ($role==3){
            $p["KorisnickoImeKorisnik"]=$_POST["KorisnickoIme"];
            $p["Id_Trgovine"]=$created;
            $p["ASSIGNSTORE"]=true;
            $db->assignStore($p);
        }
        $response->STATUSMESSAGE = "STORE CREATED";
        
        $response->STATUS = true;
        $response->DATA["NazivTrgovine"]=$_POST["NazivTrgovine"];
        $response->DATA["Id_Trgovine"]=$created;
        
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["DELETESTORE"]) && $_POST["DELETESTORE"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["Id_Trgovine"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO STORE";
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
        
        $deleted = $db->deleteStore($_POST);
        if ($deleted == false){
            $response->STATUSMESSAGE = "STORE DOESN'T EXIST";
            $response->STATUS = false;
            $response->DATA=$_POST["Id_Trgovine"];
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $response->STATUSMESSAGE = "STORE DELETED";
        $response->STATUS = true;
        $response->DATA["Naziv_Trgovine"]=$deleted;
        $response->DATA["Id_Trgovine"]=$_POST["Id_Trgovine"];
        
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["BULKCREATESTORE"]) && $_POST["BULKCREATESTORE"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["BrojTrgovina"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO STORE COUNT";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["Sufiks"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO STORE SUFFIX";
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
        
        $created = $db->createStoresInBulk($_POST);
        $response->STATUSMESSAGE = "{$_POST["BrojTrgovina"]} STORES CREATED";
        
        $response->STATUS = true;
        $response->DATA=$created;
        
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["EDITSTORE"]) && $_POST["EDITSTORE"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["Id_Trgovine"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO STORE";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if (!isset($_POST["NazivTrgovine"]) && !isset($_POST["StanjeRacuna"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO STORE NAME OR BALANCE";
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
        
        $edited = $db->editStore($_POST);
        if ($edited==true){
            if (isset($_POST["NazivTrgovine"]) && !isset($_POST["StanjeRacuna"])){
                $response->STATUSMESSAGE = "STORE NAME EDITED";
                $response->STATUS = true;
                $response->DATA["Id_Trgovine"]=$_POST["Id_Trgovine"];
                $response->DATA["NazivTrgovine"]=$_POST["NazivTrgovine"];
            }
            elseif (!isset($_POST["NazivTrgovine"]) && isset($_POST["StanjeRacuna"])){
                $response->STATUSMESSAGE = "STORE BALANCE EDITED";
                $response->STATUS = true;
                $response->DATA["Id_Trgovine"]=$_POST["Id_Trgovine"];
                $response->DATA["StanjeRacuna"]=$_POST["StanjeRacuna"];
            }
            elseif (isset($_POST["NazivTrgovine"]) && isset($_POST["StanjeRacuna"])){
                $response->STATUSMESSAGE = "STORE NAME AND BALANCE EDITED";
                $response->STATUS = true;
                $response->DATA["Id_Trgovine"]=$_POST["Id_Trgovine"];
                $response->DATA["NazivTrgovine"]=$_POST["NazivTrgovine"];
                $response->DATA["StanjeRacuna"]=$_POST["StanjeRacuna"];
            }
        }
        else{
            $response->STATUSMESSAGE = "STORE DOESN'T EXIST";
            $response->STATUS = false;
            $response->DATA["Id_Trgovine"]=$_POST["Id_Trgovine"];
        }
        
        
        $response = json_encode($response, JSON_UNESCAPED_UNICODE);
        echo $response;
        return;
    }
    
    if (isset($_POST["ASSIGNSTORESELF"]) && $_POST["ASSIGNSTORESELF"] == true) {
        if (!isset($_POST["KorisnickoIme"])){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO USERNAME";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        if ((!isset($_POST["Id_Trgovine"]) || empty($_POST["Id_Trgovine"])) && $_POST["ASSIGNSTORESELF"]=="true"){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "NO STORE";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $authorised = $db->isAdmin($_POST["KorisnickoIme"]);
        $role = $db->getUserRole($_POST["KorisnickoIme"]);
        if ($role!=1){
            $response->STATUS = false;
            $response->STATUSMESSAGE = "UNAUTHORISED";
            $response = json_encode($response, JSON_UNESCAPED_UNICODE);
            echo $response;
            return;
        }
        $p["KorisnickoImeKorisnik"]=$_POST["KorisnickoIme"];
        $p["Id_Trgovine"] = $_POST["Id_Trgovine"];
        $p["ASSIGNSTORE"]=true;
        $assigned = $db->assignStore($p);
        if ($assigned==1){
            $response->STATUSMESSAGE = "STORE ASSIGNED";
            //$response->DATA["KorisnickoImeKorisnik"]=$_POST["KorisnickoImeKorisnik"];
            //$response->DATA["Id_Trgovine"]=$_POST["Id_Trgovine"];
            //$response->DATA["Naziv_Trgovine"]=$db->getStoreName($_POST["Id_Trgovine"]);
        }
        elseif($assigned==0){
            $response->STATUSMESSAGE = "STORE UNASSIGNED";
        }
        
        $response->STATUS = true;
        
        
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