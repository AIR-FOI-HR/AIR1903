<?php

class DB_Functions {
	private $conn;

	function __construct() {
		require_once 'db_connect.php';
		$db = new DB_CONNECT();
		$this->conn = $db->connect();
	}

	function __destruct() {
		
	}
	
	public function checkRegisterEmpty($post){
        if(!isset($post["Ime"])|| ctype_space($post["Ime"]) || empty($post["Ime"])) return "Ime";
        if(!isset($post["Prezime"])|| ctype_space($post["Prezime"]) || empty($post["Prezime"])) return "Prezime";
        if(!isset($post["Email"])|| ctype_space($post["Email"]) || empty($post["Email"])) return "Email";
        if(!isset($post["KorisnickoIme"])|| ctype_space($post["KorisnickoIme"]) || empty($post["KorisnickoIme"])) return "KorisnickoIme";
        if(!isset($post["Lozinka"])|| ctype_space($post["Lozinka"]) || empty($post["Lozinka"])) return "Lozinka";
        return 1;
    }
    
    public function checkLogin($post) {
        if (!isset($post["KorisnickoIme"]) || ctype_space($post["KorisnickoIme"]) || empty($post["KorisnickoIme"]))
            return "Username";
        if (!isset($post["Lozinka"]) || ctype_space($post["Lozinka"]) || empty($post["Lozinka"]))
            return "Password";
        return 1;
    }

    public function storeUser($post, $hash) {
        $event=$this->getCurrentEvent();
        $q = "INSERT INTO Korisnik (Id ,Ime, Prezime, Email, Id_Uloge, KorisnickoIme, LozinkaSalt, LozinkaHash, DozvolaUpravljanjeUlogama, DozvolaUpravljanjeStanjemRacuna, DozvolaPregledTransakcija, DozvolaUvidUStatistiku, Id_Eventa) ";
        $q .= "VALUES (null,'{$post["Ime"]}', '{$post["Prezime"]}','{$post["Email"]}', 1, '{$post["KorisnickoIme"]}','{$hash[0]}', '{$hash[1]}', 0, 0, 0, 0, {$event["Id"]})";
        $stmt = $this->conn->query($q);
        $id = $this->conn->insert_id;
        
        $time= time();
        $q = "INSERT INTO Korisnik_StanjeRacuna (StanjeRacuna, UnixVrijeme, Id_Korisnika) VALUES ('0', '$time','$id')";
        $stmt = $this->conn->query($q);
        $q = "SELECT k.Ime, k.Prezime, k.Email, k.KorisnickoIme, k.DozvolaUpravljanjeUlogama, k.DozvolaUpravljanjeStanjemRacuna, k.DozvolaPregledTransakcija, k.DozvolaUvidUStatistiku, k.Id_Uloge, u.Naziv, k.PrijavaPotvrdena FROM Korisnik k JOIN Uloga u ON (k.Id_Uloge=u.Id) WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND (k.Id_Eventa={$event["Id"]} OR k.Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $q = "SELECT StanjeRacuna FROM Korisnik_StanjeRacuna WHERE Id_Korisnika = '$id'";
        $stmt2 = $this->conn->query($q);
        $stmt2 = $stmt2->fetch_assoc();
        $response["Ime"] = $stmt["Ime"];
        $response["Prezime"] = $stmt["Prezime"];
        $response["Email"] = $stmt["Email"];
        $response["KorisnickoIme"] = $stmt["KorisnickoIme"];
        $response["StanjeRacuna"] = $stmt2["StanjeRacuna"];
        $response["DozvolaUpravljanjeUlogama"] = $stmt["DozvolaUpravljanjeUlogama"];
        $response["DozvolaUpravljanjeStanjemRacuna"] = $stmt["DozvolaUpravljanjeStanjemRacuna"];
        $response["DozvolaPregledTransakcija"] = $stmt["DozvolaPregledTransakcija"];
        $response["DozvolaUvidUStatistiku"] = $stmt["DozvolaUvidUStatistiku"];
        $response["Id_Uloge"] = $stmt["Id_Uloge"];
        $response["Naziv_Uloge"] = $stmt["Naziv"];
        $response["LoginTime"] = time();

        return $response;
    }
	
	public function checkRegister($post) {
        if (strlen($post["Ime"])<3) return "Ime";
        if (strlen($post["Prezime"])<3) return "Prezime";
        if (!preg_match("/(\w+\.)*(\w+)@(\w+\.){1,2}(\w{2,5})/", $post["Email"])) return "Email";
        if (strlen($post["KorisnickoIme"])<5) return "KorisnickoIme";
        if (strlen($post["Lozinka"])<7) return "Lozinka";
        return 1;
    }
    
    public function userExistsRegister($post) {
        $event = $this->getCurrentEvent();
        $q = "SELECT KorisnickoIme FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);

        if ($stmt->num_rows > 0) {
            $stmt->close();
            return "KorisnickoIme";
        }

        $q = "SELECT Email FROM Korisnik WHERE Email='{$post["Email"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);

        if ($stmt->num_rows > 0) {
            $stmt->close();
            return "Email";
        }
        return 0;
    }

    public function userExistsLogin($post) {
        $event = $this->getCurrentEvent();
        $q = "SELECT KorisnickoIme FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        if ($stmt->num_rows > 0) {
            return 1;
        }
        return 0;
    }

    public function hashPassword($post) {
        $cstrong=true;
        $salt = openssl_random_pseudo_bytes(32, $cstrong);
        $saltB64= base64_encode($salt);
        $iterations = 10000;
        $hash = hash_pbkdf2("sha256", $post["Lozinka"], $salt, $iterations);
        $hashB64 = base64_encode(pack('H*',$hash));
        return [$saltB64,$hashB64];
    }

	public function checkPassword($post) {
        $event = $this->getCurrentEvent();
        $q = "SELECT LozinkaSalt, LozinkaHash, KrivePrijave FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();

        $iterations = 10000;

        $response["Forbidden"] = false;
        $response["KrivePrijave"] = 0;
        $response2 = null;

        if ($stmt["KrivePrijave"] > 0 && $stmt["KrivePrijave"]<= 5) {
            sleep($stmt["KrivePrijave"]);
        }
        elseif($stmt["KrivePrijave"]>5){
            sleep(5);
        }

        $salt = base64_decode($stmt["LozinkaSalt"]);
        $hashDB = $stmt["LozinkaHash"];

        $hash = hash_pbkdf2("sha256", $post["Lozinka"], $salt, $iterations);
        $hash = base64_encode(pack('H*', $hash));


        if ($hash == $hashDB) {
            $q = "SELECT k.Ime, k.Prezime, k.Email, k.KorisnickoIme, k.DozvolaUpravljanjeUlogama, k.DozvolaUpravljanjeStanjemRacuna, k.DozvolaPregledTransakcija, k.DozvolaUvidUStatistiku, k.Id_Uloge, u.Naziv, k.Jezik, k.PrijavaPotvrdena FROM Korisnik k JOIN Uloga u ON (k.Id_Uloge=u.Id) WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (k.Id_Eventa={$event["Id"]} OR k.Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_assoc();
            $response2["Ime"] = $stmt["Ime"];
            $response2["Prezime"] = $stmt["Prezime"];
            $response2["Email"] = $stmt["Email"];
            $response2["KorisnickoIme"] = $stmt["KorisnickoIme"];
            $response2["StanjeRacuna"] = $this->getBalance($post);
            $response2["DozvolaUpravljanjeUlogama"] = $stmt["DozvolaUpravljanjeUlogama"];
            $response2["DozvolaUpravljanjeStanjemRacuna"] = $stmt["DozvolaUpravljanjeStanjemRacuna"];
            $response2["DozvolaPregledTransakcija"] = $stmt["DozvolaPregledTransakcija"];
            $response2["DozvolaUvidUStatistiku"] = $stmt["DozvolaUvidUStatistiku"];
            $response2["Id_Uloge"] = $stmt["Id_Uloge"];
            $response2["Naziv_Uloge"] = $stmt["Naziv"];
            $response2["Jezik"] = $stmt["Jezik"];
            $response2["LoginTime"] = time();
            $response2["Token"] = $this->generateAuth($response2["KorisnickoIme"]);

            $response2 = json_encode($response2);

            $q = "UPDATE Korisnik SET KrivePrijave = 0 WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);

            return [$response, $response2];
        } else {
            $q = "UPDATE Korisnik SET KrivePrijave = KrivePrijave+1 WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            $q = "SELECT KrivePrijave FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_assoc();
            $response["KrivePrijave"] = $stmt["KrivePrijave"];
            return [$response, $response2];
        }
    }

    public function getAllProducts($post) {
        $event = $this->getCurrentEvent();
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $userId = $stmt["Id"];
        $roleId=$stmt["Id_Uloge"];
        $response[0] = $stmt["Id_Uloge"];
        
        if ($roleId==1){
            return $response;
        }
        
        $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = {$userId}";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $storeId=$stmt["Id_Trgovina"];
        
        
        
        if ($roleId == 3){ // ako je prodavac
            if (empty($storeId)){
                return false;
            }
            $q="SELECT fin.Id, fin.Naziv, fin.Opis, fin.Cijena, fin.Slika, fin.Kolicina FROM ("
                ."SELECT svi.*, tp.Id_Trgovine, tp.Kolicina "
                ."FROM (SELECT a.*, b.Cijena "
                ."FROM Item a "
                ."LEFT JOIN" 
                ."(SELECT c.Id_Proizvod, d.Cijena, c.UnixVrijeme "
                ."FROM "
                ."(SELECT Id_Proizvod, MAX(UnixVrijeme) UnixVrijeme "
                ."FROM Proizvod_Cijena "
                ."GROUP BY Id_Proizvod) c "
                ."JOIN Proizvod_Cijena d "
                ."ON c.Id_Proizvod = d.Id_Proizvod AND d.UnixVrijeme = c.UnixVrijeme ) b "
                ."ON a.Id = b.Id_Proizvod) svi "
                ."JOIN Trgovina_Item tp "
                ."ON tp.Id_Itema = svi.id "
                ."WHERE svi.Izbrisan=0) fin "
                ."JOIN Proizvod ON fin.Id = Proizvod.Id_Itema "
                ."WHERE Id_Trgovine={$storeId} ";
        }
        elseif ($roleId == 2){ // ako je admin
            $q="SELECT fin.Id, fin.Naziv, fin.Opis, fin.Cijena, fin.Slika, fin.Kolicina FROM ("
                ."SELECT svi.*, tp.Id_Trgovine, tp.Kolicina "
                ."FROM (SELECT a.*, b.Cijena "
                ."FROM Item a "
                ."LEFT JOIN" 
                ."(SELECT c.Id_Proizvod, d.Cijena, c.UnixVrijeme "
                ."FROM "
                ."(SELECT Id_Proizvod, MAX(UnixVrijeme) UnixVrijeme "
                ."FROM Proizvod_Cijena "
                ."GROUP BY Id_Proizvod) c "
                ."JOIN Proizvod_Cijena d "
                ."ON c.Id_Proizvod = d.Id_Proizvod AND d.UnixVrijeme = c.UnixVrijeme ) b "
                ."ON a.Id = b.Id_Proizvod) svi "
                ."JOIN Trgovina_Item tp "
                ."ON tp.Id_Itema = svi.id "
                ." WHERE svi.Izbrisan=0) fin "
                ."JOIN Proizvod ON fin.Id = Proizvod.Id_Itema ";
        }

        $stmt = $this->conn->query($q);
        $response[1] = $stmt->fetch_all(MYSQLI_ASSOC);
        return $response;
    }

    public function addNewProduct($post) {
        $event = $this->getCurrentEvent();
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $userId = $stmt["Id"];
        
        $response[0] = $stmt["Id_Uloge"];
        
        if ($response[0]==1){
            return $response;
        }
        $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = {$userId}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $storeId=$stmt["Id_Trgovina"];
        
        $q = "INSERT INTO Item (Id ,Naziv, Opis, Slika) ";
        if (!isset($_FILES['Slika'])) {
            $q .= "VALUES (null,'{$post["Naziv"]}','{$post["Opis"]}', 'https://cortex.foi.hr/pop/Slike/defaultPicture.png')";
        } else {
            $slika = $_FILES["Slika"];
            $uploadPath = 'Slike/';
            $uploadUrl = '/home/zlatko/public_html/pop/' . $uploadPath;
            $pictureUrl = 'https://cortex.foi.hr/pop/' . $uploadPath;
            $fileInfo = pathinfo($_FILES['Slika']['name']);
            $extension = $fileInfo['extension'];
            $name = bin2hex(random_bytes(32));

            $file_url = $uploadUrl . $name . '.' . $extension;
            $filePath = $uploadPath . $name . '.' . $extension;
            $pictureUrl = $pictureUrl . $name . '.' . $extension;
            move_uploaded_file($_FILES['Slika']['tmp_name'], $file_url);

            $q .= "VALUES (null,'{$post["Naziv"]}','{$post["Opis"]}', '$pictureUrl')";
        }
        $stmt = $this->conn->query($q);
        $productId=$this->conn->insert_id;
        
        $q = "INSERT INTO Proizvod (Id_Itema) VALUES ({$productId})";
        $stmt = $this->conn->query($q);
        
        $time = time();
        $q = "INSERT INTO Proizvod_Cijena (Id_Proizvod, UnixVrijeme, Cijena) VALUES "
                . "({$productId}, {$time}, {$post["Cijena"]})";
        $stmt = $this->conn->query($q);
        
        
        $q = "SELECT Naziv, Opis, Slika FROM Item WHERE Id={$productId}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $response[1]["Naziv"] = $stmt["Naziv"];
        $response[1]["Opis"] = $stmt["Opis"];
        $response[1]["Slika"] = $stmt["Slika"];
        
        $q = "SELECT Cijena FROM Proizvod_Cijena WHERE Id_Proizvod={$productId} ORDER BY UnixVrijeme DESC LIMIT 1";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $response[1]["Cijena"] = $stmt["Cijena"];
        
        
        $q="INSERT INTO Trgovina_Item (Id, Id_Trgovine, Id_Itema, Kolicina) VALUES "
                . "(null, {$storeId}, $productId, {$post["Kolicina"]})";
        $stmt = $this->conn->query($q);
        
        
        return $response;
    }

    public function deleteProduct($post) {
        $q = "UPDATE Item SET Izbrisan = 1 WHERE Id = {$post["Id"]}";
        $stmt = $this->conn->query($q);
        $response = null;
        return $response;
    }
	public function checkProductEmpty($post) {
         if(!isset($post["Naziv"])|| empty($post["Naziv"]) || !isset($post["Cijena"]) || empty($post["Cijena"]) || !isset($post["Opis"])||  empty($post["Opis"]) || isset($post["Id"])){
            return 0;
        }else{
            return 1;
        }    
    }
	public function isDelete($post) {
      if(!isset($post["Id"]) || isset($post["Naziv"]) || isset($post["Cijena"]) || isset($post["Opis"])){
            return 0;
        }else{
            return 1;
        }  
    }
    public function isSeller($post) {
        if(!isset($post["Id"])) {
            return 0;
        } else {
            $q = "SELECT Id_Uloge FROM Korisnik WHERE Id = '{$post["Id"]}'";
            $stmt = $this->conn->query($q);
            $id = $stmt;
            if ($id === 3) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    public function getSellerProducts($post) {
        $q = "SELECT Id FROM Trgovina WHERE Prodavac = '{$post["Id"]}'";
        $stmt = $this->conn->query($q);
        $id = $stmt->fetch_assoc();
        $id2 = $id['Id'];
        $q2 = "SELECT Proizvod.Id, Proizvod.Naziv, Proizvod.Cijena, Proizvod.Opis, Proizvod.Slika FROM Proizvod, Trgovina_Proizvod, Trgovina WHERE Proizvod.Id = Trgovina_Proizvod.Id_Proizvoda AND Trgovina_Proizvod.Id_Trgovine = Trgovina.Id AND Trgovina.Id = '$id2'";
        $stmt = $this->conn->query($q2);
         $json_array = array();
        while ($row = $stmt->fetch_assoc()) {
            $json_array[] = $row;
        }
        return $json_array;
    }
	public function isUpdate($post) {
        if(isset($post["Id"]) && isset($post["Naziv"]) && isset($post["Cijena"]) && isset($post["Opis"]) && isset($post["Slika"])){
            return 1;
        }else{
            return 0;
        }
    }
	public function updateProduct($post) {
        if (!isset($_FILES['Slika'])) {
            if ($post["Slika"]=="") $slika = 'https://cortex.foi.hr/pop/Slike/defaultPicture.png';
            else $slika = $post["Slika"];
        } else {
            $slika = $_FILES["Slika"];
            $uploadPath = 'Slike/';
            $uploadUrl = '/home/zlatko/public_html/pop/' . $uploadPath;
            $pictureUrl = 'https://cortex.foi.hr/pop/' . $uploadPath;
            $fileInfo = pathinfo($_FILES['Slika']['name']);
            $extension = $fileInfo['extension'];
            $name = bin2hex(random_bytes(32));

            $file_url = $uploadUrl . $name . '.' . $extension;
            $filePath = $uploadPath . $name . '.' . $extension;
            $pictureUrl = $pictureUrl . $name . '.' . $extension;
            move_uploaded_file($_FILES['Slika']['tmp_name'], $file_url);

            $slika=$pictureUrl;
        }
        $q = "UPDATE Item SET Naziv = '{$post["Naziv"]}', Opis = '{$post["Opis"]}', Slika = '{$slika}' WHERE Item.Id = '{$post["Id"]}'";
        $stmt = $this->conn->query($q);
        $time= time();
        $q = "INSERT INTO Proizvod_Cijena (Id_Proizvod, UnixVrijeme, Cijena) VALUES ({$post["Id"]}, {$time}, {$post["Cijena"]})";
        $stmt = $this->conn->query($q);
        $q = "UPDATE Trgovina_Item SET Kolicina = {$post["Kolicina"]} WHERE Id_Itema = {$post["Id"]}";
        $stmt = $this->conn->query($q);
        $response = "Proizvod je uspjesno azuriran!";
        return $response;
    }
	
	
    public function generateAuth($korIme) {
        $event = $this->getCurrentEvent();
        $q = "SELECT Id, KorisnickoIme FROM Korisnik WHERE KorisnickoIme='{$korIme}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt=$stmt->fetch_assoc();
        $id=$stmt["Id"];

        $auth = openssl_random_pseudo_bytes(128);
        $authString = base64_encode($auth);
        $time = time() + 6 * 60 * 60;
        $q = "INSERT INTO Tokeni (Token, UnixVrijemeIsteka, Id_Korisnika) VALUES ('{$authString}',{$time}, {$id});";
        $stmt = $this->conn->query($q);
        return $authString;
    }

    public function checkAuth($token, $korIme) {
        $time = time();
        $event = $this->getCurrentEvent();
        $q = "SELECT Id, KorisnickoIme FROM Korisnik WHERE KorisnickoIme='{$korIme}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt=$stmt->fetch_assoc();
        $id=$stmt["Id"];
        
        $q = "SELECT ID, Id_Korisnika FROM Tokeni WHERE Token = '{$token}' AND UnixVrijemeIsteka>{$time}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        if (sizeof($stmt) != 0)
        {
            if ($stmt["Id_Korisnika"]==$id || $stmt["Id_Korisnika"]==null)
                return true;
            else
                return false;
        }
        else
            return false;
    }
    public function addNewPackage($post) {
        $event = $this->getCurrentEvent();
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $userId = $stmt["Id"];
        
        $response2 = $stmt["Id_Uloge"];
        
        if ($response2==1){
            return $response2;
        }
        $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = {$userId}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $storeId=$stmt["Id_Trgovina"];
         
        $q = "INSERT INTO Item (Id, Naziv, Opis, Slika, Izbrisan) ";
         if (!isset($_FILES['Slika'])) {
            $q .= "VALUES (null,'{$post["Naziv"]}','{$post["Opis"]}', 'https://cortex.foi.hr/pop/Slike/defaultPicture.png',0)";
        } else {
            $slika = $_FILES["Slika"];
            $uploadPath = 'Slike/';
            $uploadUrl = '/home/zlatko/public_html/pop/' . $uploadPath;
            $pictureUrl = 'https://cortex.foi.hr/pop/' . $uploadPath;
            $fileInfo = pathinfo($_FILES['Slika']['name']);
            $extension = $fileInfo['extension'];
            $name = bin2hex(random_bytes(32));

            $file_url = $uploadUrl . $name . '.' . $extension;
            $filePath = $uploadPath . $name . '.' . $extension;
            $pictureUrl = $pictureUrl . $name . '.' . $extension;
            move_uploaded_file($_FILES['Slika']['tmp_name'], $file_url);

            $q .= "VALUES (null,'{$post["Naziv"]}','{$post["Opis"]}', '$pictureUrl', 0)";
        }
        $stmt = $this->conn->query($q);
        
        $itemId = $this->conn->insert_id;
       
        $q = "INSERT INTO Paket (Id_Itema) VALUES ('{$itemId}')";
        $stmt = $this->conn->query($q);
        
        $time = time();
        $q = "INSERT INTO Paket_Popust (Id_Paketa ,UnixVrijeme, Popust) VALUES ('{$itemId}','{$time}','{$post["Popust"]}')";
        $stmt = $this->conn->query($q);
        
        $q = "INSERT INTO Trgovina_Item (Id, Id_Trgovine, Id_Itema, Kolicina) VALUES (null, $storeId, $itemId, '{$post["KolicinaPaketa"]}')";
        $stmt = $this->conn->query($q);
      
        $q = "SELECT Naziv, Opis, Slika FROM Item WHERE Id={$itemId}";
        $stmt = $this->conn->query($q);
        $stmt4 = $stmt->fetch_assoc();
        $q = "SELECT Popust FROM Paket_Popust WHERE Id_Paketa={$itemId} ORDER BY UnixVrijeme DESC LIMIT 1";
        $stmt = $this->conn->query($q);
        $stmt3 = $stmt->fetch_assoc();
        
        $response["Id"]=$itemId;
        $response["Naziv"] = $stmt4["Naziv"];
        $response["Opis"] = $stmt4["Opis"];
        $response["Slika"] = $stmt4["Slika"];
        $response["Kolicina"] = $post["Kolicina"];
        $response["Popust"] = $stmt3["Popust"];
        $response["StavkePaketa"]=null;

        return $response;
    }
	
	public function addItemToPackage($post){
        
        $paket = $post["Id_Paket"];
        $proizvodi = $post["Id_Proizvod"];
        $kolicine = $post["Kolicina"];
        
        $q = "DELETE FROM Proizvod_Paket WHERE Id_Paketa = {$paket}";
        $stmt = $this->conn->query($q);
        $q = "INSERT INTO Proizvod_Paket (Id, Id_Paketa, Id_Proizvoda, Kolicina) VALUES ";
        for ($i=0;$i<sizeof($proizvodi);$i++){
            if ($kolicine[$i]!=0)
                $q.="(null, {$paket}, {$proizvodi[$i]},  {$kolicine[$i]}), ";
        }
        $q=substr($q, 0, -2);
        //echo $q;
        $stmt = $this->conn->query($q);
        return $post;
        //if (is_array($proizvodi)) echo "ARRAY";
    }
	
    public function checkPackageEmpty($post) {
        if (!isset($post["Naziv"]) || empty($post["Naziv"]) || !isset($post["Popust"]) || empty($post["Popust"]) || !isset($post["Kolicina"]) || empty($post["Kolicina"]) || !isset($post["Opis"]) || empty($post["Opis"])) {
            return 0;
        } else {
            return 1;
        }
    }
	
    public function updatePackage($post) {
       if (!isset($_FILES['Slika'])) {
            if ($post["Slika"]=="") $slika = 'https://cortex.foi.hr/pop/Slike/defaultPicture.png';
            else $slika = $post["Slika"];
        } else {
            $slika = $_FILES["Slika"];
            $uploadPath = 'Slike/';
            $uploadUrl = '/home/zlatko/public_html/pop/' . $uploadPath;
            $pictureUrl = 'https://cortex.foi.hr/pop/' . $uploadPath;
            $fileInfo = pathinfo($_FILES['Slika']['name']);
            $extension = $fileInfo['extension'];
            $name = bin2hex(random_bytes(32));

            $file_url = $uploadUrl . $name . '.' . $extension;
            $filePath = $uploadPath . $name . '.' . $extension;
            $pictureUrl = $pictureUrl . $name . '.' . $extension;
            move_uploaded_file($_FILES['Slika']['tmp_name'], $file_url);

            $slika=$pictureUrl;
        }
        $q = "UPDATE Item SET Naziv = '{$post["Naziv"]}', Opis = '{$post["Opis"]}', Slika = '{$slika}' WHERE Item.Id = {$post["Id"]}";
        $stmt = $this->conn->query($q);
        $time= time();
        $q = "INSERT INTO Paket_Popust (Id_Paketa, UnixVrijeme, Popust) VALUES ({$post["Id"]}, {$time}, {$post["Popust"]})";
        $stmt = $this->conn->query($q);
        $response["Naziv"]=$post["Naziv"];
        $response["Opis"]=$post["Opis"];
        $response["Popust"]=$post["Popust"];
        $response["Slika"]=$slika;
        return $response;
    }
	
	public function getContentsOfPackage($post){
        if (isset($post["UnixDatum"])){
            $q = "SELECT p.Id_Proizvoda Id, i.Naziv, i.Opis, i.Slika, p.Kolicina"
                    . " FROM Proizvod_Paket p"
                    . " JOIN Item i ON i.Id = p.Id_Proizvoda "
                    . " WHERE Id_Paketa = {$post["Id"]}";
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_all(MYSQLI_ASSOC);
            
            foreach ($stmt as &$i){
                $q = "SELECT Cijena FROM Proizvod_Cijena p"
                        . " WHERE p.Id_Proizvod = {$i["Id"]} AND UnixVrijeme < {$post["UnixDatum"]}"
                        . " ORDER BY UnixVrijeme DESC LIMIT 1";
                $stmt2 = $this->conn->query($q);
                $stmt2 = $stmt2->fetch_assoc();
                $i["Cijena"] = $stmt2["Cijena"];
            }
            
            return $stmt;
        }
        else {
            $q = "SELECT Popust FROM Paket_Popust WHERE Id_Paketa = {$post["Id"]}";
            $stmt3 = $this->conn->query($q);
            $stmt3 = $stmt3->fetch_assoc();
            $popust = $stmt3["Popust"];
            $q = "SELECT fin2.Id, fin2.Naziv, fin2.Opis, fin2.Cijena, fin2.Slika, fin2.Kolicina FROM "
                ."(SELECT fin.Id, fin.Naziv, fin.Opis, fin.Cijena, fin.Slika, Proizvod_Paket.Kolicina  "
                ."FROM  "
                ."(SELECT svi.*, tp.Id_Trgovine  "
                ."FROM  "
                ."(SELECT a.*, b.Cijena  "
                ."FROM Item a  "
                ."LEFT JOIN  "
                ."(SELECT c.Id_Proizvod, d.Cijena, c.UnixVrijeme  "
                ."FROM  "
                ."(SELECT Id_Proizvod, MAX(UnixVrijeme) UnixVrijeme  "
                ."FROM Proizvod_Cijena  "
                ."GROUP BY Id_Proizvod) c  "
                ."JOIN Proizvod_Cijena d  "
                ."ON c.Id_Proizvod = d.Id_Proizvod AND d.UnixVrijeme = c.UnixVrijeme ) b  "
                ."ON a.Id = b.Id_Proizvod) svi  "
                ."JOIN Trgovina_Item tp  "
                ."ON tp.Id_Itema = svi.id  "
                ."WHERE svi.Izbrisan=0) fin "
                ."JOIN Proizvod_Paket "
                ."ON Proizvod_Paket.Id_Proizvoda = fin.Id "
                ."WHERE Proizvod_Paket.Id_Paketa={$post["Id"]}) fin2 ";
        }
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_all(MYSQLI_ASSOC);
        foreach ($stmt as &$i){
            $i["CijenaStavke"]=strval($i["Cijena"]*$i["Kolicina"]*(100-$popust)/100);
        }
        return $stmt; 
    }

	public function getBalance($post) {
        $event = $this->getCurrentEvent();
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        if ($stmt->num_rows == 0){
            return -1;
        }
        $stmt = $stmt->fetch_assoc();
        $userId = $stmt["Id"];
        
        
        //echo "User id" . $userId;
        $q = "SELECT StanjeRacuna FROM Korisnik_StanjeRacuna WHERE Id_Korisnika = {$userId} ORDER BY UnixVrijeme DESC LIMIT 1";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $response = $stmt["StanjeRacuna"];
        
        return $response;
    }
    public function getBalanceStore($post) {
        //echo "wat";
        $event=$this->getCurrentEvent();
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $userId = $stmt["Id"];
        
        $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = {$userId}";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $storeId = $stmt["Id_Trgovina"];
        
        //echo "User id" . $userId;
        
        $q = "SELECT StanjeRacuna FROM Trgovina_StanjeRacuna WHERE Id_Trgovine = {$storeId} ORDER BY UnixVrijeme DESC LIMIT 1";
        //echo $q;
        $stmt = $this->conn->query($q);
        if ($stmt==false) return null;
        $stmt = $stmt->fetch_assoc();
        $response = $stmt["StanjeRacuna"];
        
        return $response;
    }
    
    public function setInitialBalance($post) {
        $event=$this->getCurrentEvent();
        $korisnici = $post["KorisnickoImeKorisnik"];
        $time= time();
        foreach ($korisnici as &$i){
            $q = "SELECT Id, Id_Uloge, KorisnickoIme FROM Korisnik WHERE KorisnickoIme = '{$i}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt=$this->conn->query($q);
            $stmt = $stmt->fetch_assoc();
            $userId = $stmt["Id"];
            $roleId=$stmt["Id_Uloge"];
            $username=$stmt["KorisnickoIme"];

            if($roleId==1){//kupac
                $q = "INSERT INTO Korisnik_StanjeRacuna (StanjeRacuna, UnixVrijeme, Id_Korisnika) VALUES ('{$post["StanjeRacuna"]}', '{$time}', '{$userId}')";
                $stmt = $this->conn->query($q);
            }
        }
        
        $response = $post["StanjeRacuna"];
        return $response;
    }
    //funckija za smanjenje količine proizvoda prilikom prodaje, funkcija također mijenja stanje novčanika kupca i prodavača
    public function sellItems($post) {
        $event = $this->getCurrentEvent();
        $popustRacuna = $post["PopustRacuna"];
        $vrijemeProdaje = date("Y-m-d H:i:s");
        $idItema = $post["Itemi"];
        $kolicine = $post["Kolicine"];
        $mjestoIzdavanja = "Fakultet organizacije i informatike";
        $idTrgovine=0;
        $idKupca="null";
        
        
        $q = "SELECT Id FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idProdavaca = $stmt["Id"];
        
        $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = {$idProdavaca}";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idTrgovine=$stmt["Id_Trgovina"];
        
        $q = "INSERT INTO Racun(Id, MjestoIzdavanja, DatumIzdavanja, Popust, Id_Trgovine, Kupac, Id_Eventa)"
                . " VALUES (null, '{$mjestoIzdavanja}', '{$vrijemeProdaje}', {$popustRacuna}, {$idTrgovine}, {$idKupca}, {$event["Id"]})";
        $stmt = $this->conn->query($q);
        $idRacuna=$this->conn->insert_id;
        
        $q = "INSERT INTO Item_Racun (Id, Id_Itema, Id_Racuna, Kolicina) VALUES ";
        
        for ($i=0;$i<sizeof($idItema);$i++){
            $q.= "(null, {$idItema[$i]}, {$idRacuna}, {$kolicine[$i]}), ";
        }
        $q = substr($q,0,-2);
        //echo "TESTksan".$q;
        $stmt = $this->conn->query($q);
        
        $p["Id_Racuna"] = $idRacuna;
        $p["KorisnickoIme"] = $post["KorisnickoIme"];
        $response = $this->getInvoice($p);
        //var_dump ($response);
        //return $response;
        
        $p["Id_Racuna"]=$idRacuna;
        $p["Id_Trgovine"]=$idTrgovine;
        $p["KorisnickoIme"]=$post["KorisnickoIme"];
        $goodAmounts = $this->checkAmounts($p);
        if ($goodAmounts==true) return $response;
        else{
            $q="DELETE FROM Item_Racun WHERE Id_Racuna = {$idRacuna}";
            $stmt = $this->conn->query($q);
            $q = "DELETE FROM Racun WHERE Id={$idRacuna}";
            $stmt = $this->conn->query($q);
            return false;
        }
    }
	
	public function deleteInvoice($post){
        $idRacuna = $post["Id_Racuna"];
        $q = "DELETE FROM Item_Racun WHERE Id_Racuna = {$idRacuna}";
        $stmt=$this->conn->query($q);
        $q = "DELETE FROM Racun WHERE Id = {$idRacuna}";
        $stmt=$this->conn->query($q);
        return null;
    }
	
	public function confirmSale($post){
        $event=$this->getCurrentEvent();
        $q = "SELECT Id FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idKupca = $stmt["Id"];
        
        $idRacuna = $post["Id_Racuna"];
        
        $q = "UPDATE Racun SET Kupac = {$idKupca} WHERE Id = {$idRacuna}";
        $stmt=$this->conn->query($q);
        
        $p["Id_Racuna"] = $idRacuna;
        $p["KorisnickoIme"] = $post["KorisnickoIme"];
        $response = $this->getInvoice($p);
        
        
        
        if ($this->reduceAmount($p)==false){
            $q = "DELETE FROM Item_Racun WHERE Id_Racuna = {$idRacuna}";
            $stmt=$this->conn->query($q);
            $q = "DELETE FROM Racun WHERE Id = {$idRacuna}";
            $stmt=$this->conn->query($q);
            return -1;
        }
        else{
            $clientBalance = $this->getBalance($post);
            if ($response["ZavrsnaCijena"]>$clientBalance){
                $q = "DELETE FROM Item_Racun WHERE Id_Racuna = {$idRacuna}";
                $stmt=$this->conn->query($q);
                $q = "DELETE FROM Racun WHERE Id = {$idRacuna}";
                $stmt=$this->conn->query($q);
                return -2;
            }
            $time = time();
            $this->updateBalance($response["Kupac"],$response["ZavrsnaCijena"],$time, false);
            $this->updateBalance($response["Id_Trgovine"], $response["ZavrsnaCijena"],$time, true);
            return $response;
        }
    }
	
    public function updateBalance($id, $kolicina, $time, $trgovina){
        if ($trgovina==false){
            $q = "SELECT StanjeRacuna FROM Korisnik_StanjeRacuna WHERE Id_Korisnika = {$id} ORDER BY UnixVrijeme DESC LIMIT 1";
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_assoc();
            $balance = $stmt["StanjeRacuna"];
            $newBalance = $balance - $kolicina;
            $q = "INSERT INTO Korisnik_StanjeRacuna (Id_Korisnika, UnixVrijeme, StanjeRacuna) VALUES ({$id}, {$time}, {$newBalance})";
            $stmt = $this->conn->query($q);
        }else{
            $q = "SELECT StanjeRacuna FROM Trgovina_StanjeRacuna WHERE Id_Trgovine = {$id} ORDER BY UnixVrijeme DESC LIMIT 1";
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_assoc();
            $balance = $stmt["StanjeRacuna"];
            $newBalance = $balance + $kolicina;
            $q = "INSERT INTO Trgovina_StanjeRacuna (Id_Trgovine, UnixVrijeme, StanjeRacuna) VALUES ({$id}, {$time}, {$newBalance})";
            $stmt = $this->conn->query($q);
        }
    }
	
	public function sellPackages($post) {
	$kolicinaProdanihPaketa = $post["Kolicina"];
        $q = "SELECT Kolicina FROM Trgovina_Item WHERE Id_Itema = '{$post["Id_Itema"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $kolicinaPaketaPrijeProdaje = $stmt["Kolicina"];
        $kolicinaPaketaNakonProdaje = $kolicinaPaketaPrijeProdaje - $kolicinaProdanihPaketa;
        $q = "UPDATE Trgovina_Item SET Kolicina = '$kolicinaPaketaNakonProdaje' WHERE Id_Itema = '{$post["Id_Itema"]}'";
        $stmt = $this->conn->query($q);
        $datum = date('Y-m-d H:i:s');
        
        $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = '{$post["Id_Prodavaca"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idTrgovine= $stmt["Id_Trgovina"];
        
        $q = "INSERT INTO Racun (Id, MjestoIzdavanja, DatumIzdavanja, Popust, Id_Trgovine, Kupac) VALUES (NULL, 'Fakultet Organizacije i informatike', '$datum', '{$post["Popust"]}', '$idTrgovine', '{$post["Id_Kupac"]}')";
        $stmt = $this->conn->query($q);
        $idRacuna = $this->conn->insert_id;
        
        echo "ID RACUNA:: " . $idRacuna;
        
        $q = "INSERT INTO Item_Racun (Id, Id_Itema, Id_Racuna, Kolicina) VALUES (NULL, '{$post["Id_Itema"]}', '$idRacuna', '$kolicinaProdanihPaketa')";
        $stmt = $this->conn->query($q);      
        
        $q = "SELECT MAX(UnixVrijeme) FROM Paket_Popust WHERE Id_Paketa = '{$post["Id_Itema"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $posljednjaIzmjena = $stmt["MAX(UnixVrijeme)"];
        $q = "SELECT Popust FROM Paket_Popust WHERE Id_Paketa = '{$post["Id_Itema"]}' AND UnixVrijeme = '$posljednjaIzmjena'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $popustPaketaZaIspisRacuna = $stmt["Popust"];
        $popustPaketa = 1 - $stmt["Popust"]/100;
        
        $q = "SELECT Id_Proizvoda FROM Proizvod_Paket WHERE Id_Paketa = '{$post["Id_Itema"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $IdProizvoda = $stmt["Id_Proizvoda"];
           
        $q = "SELECT Kolicina FROM Proizvod_Paket WHERE Id_Proizvoda = '$IdProizvoda' AND Id_Paketa = '{$post["Id_Itema"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $kolicinaProdanihProizvoda = $stmt["Kolicina"];
        $q = "SELECT Kolicina FROM Trgovina_Item WHERE Id_Itema = '$IdProizvoda'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $kolicinaProizvodaPrijeProdaje = $stmt["Kolicina"];
        $kolicinaProizvodaNakonProdaje = $kolicinaProizvodaPrijeProdaje - $kolicinaProdanihProizvoda;
        $q = "UPDATE Trgovina_Item SET Kolicina = '$kolicinaProizvodaNakonProdaje' WHERE Id_Itema = '$IdProizvoda'";
        $stmt = $this->conn->query($q);
        $datum = date('Y-m-d H:i:s');
        
        $q = "SELECT MAX(UnixVrijeme) FROM Proizvod_Cijena WHERE Id_Proizvod = '$IdProizvoda'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $posljednjaIzmjena = $stmt["MAX(UnixVrijeme)"];
        $q = "SELECT Cijena FROM Proizvod_Cijena WHERE Id_Proizvod = '$IdProizvoda' AND UnixVrijeme = '$posljednjaIzmjena'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $cijenaProizvoda = $stmt["Cijena"];
        $ukupnaCijena = ($cijenaProizvoda * $post["Kolicina"]) * $popustPaketa * (1 - ($post["Popust"]/100));
        
        $q = "SELECT StanjeRacuna FROM Korisnik_StanjeRacuna WHERE Id_Korisnika = '{$post["Id_Kupac"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $stanjePrijeKupnjeKupac = $stmt["StanjeRacuna"];
        $novoStanjeKupac = $stanjePrijeKupnjeKupac - $ukupnaCijena;
        $q = "UPDATE Korisnik_StanjeRacuna SET StanjeRacuna = '$novoStanjeKupac' WHERE Id_Korisnika = '{$post["Id_Kupac"]}'";
        $stmt = $this->conn->query($q);
        
        $q = "SELECT StanjeRacuna FROM Korisnik_StanjeRacuna WHERE Id_Korisnika = '{$post["Id_Prodavaca"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $stanjePrijeKupnjeProdavac = $stmt["StanjeRacuna"];
        $novoStanjeProdavac = $stanjePrijeKupnjeProdavac + $ukupnaCijena;
        $q = "UPDATE Korisnik_StanjeRacuna SET StanjeRacuna = '$novoStanjeProdavac' WHERE Id_Korisnika = '{$post["Id_Prodavaca"]}'";
        $stmt = $this->conn->query($q);
        
        $q = "SELECT Naziv FROM Item WHERE Id = '{$post["Id_Itema"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $nazivItema = $stmt["Naziv"];
        
        $q = "SELECT Naziv FROM Trgovina WHERE Id = '$idTrgovine'";
        $stmt = $this->conn->query($q);
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $nazivTrgovine = $stmt["Naziv"];
        
        $q = "SELECT MjestoIzdavanja, DatumIzdavanja FROM Racun WHERE Id = '$idRacuna'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $mjestoIzdavanja = $stmt["MjestoIzdavanja"];
        $vrijemeIzdavanja = $stmt["DatumIzdavanja"];
        
        $response["NovoStanjeKupac"] = $novoStanjeKupac;
        $response["NovoStanjeProdavac"] = $novoStanjeProdavac;
        
        $response["Trgovina"] = $nazivTrgovine;
        $response["MjestoIzdavanja"] = $mjestoIzdavanja;
        $response["VrijemeIzdavanja"] = $vrijemeIzdavanja;
        $response["NazivItema"] = $nazivItema;
        $response["Kolicina"] = $post["Kolicina"];
        $response["PopustNaRacunu"] = $post["Popust"];
        $response["PopustNaPaketu"] = $popustPaketaZaIspisRacuna;
        $response["UkupnaCijena"] = $ukupnaCijena;

        return $response;
    }
	
	public function checkAmounts($post){ 
        //$kolicine = array();
        $racun = $this->getInvoice($post);
        //var_dump ($racun);
        $q = "SELECT Id_Trgovine FROM Racun WHERE Id = {$post["Id_Racuna"]}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idTrgovine = $stmt["Id_Trgovine"];
        
        foreach ($racun["Stavke"] as &$i){
            if ($i["ItemType"]=="Proizvod"){
                if (isset($kolicine[$i["Id"]])) $kolicine[$i["Id"]]+=$i["Kolicina"];
                else $kolicine[$i["Id"]]=+$i["Kolicina"]*1;
            }
            else {
                $kolicinaPaketa = $i["Kolicina"];
                foreach ($i["StavkePaketa"] as &$j){
                    if (isset($kolicine[$j["Id"]])) $kolicine[$j["Id"]]+=$j["Kolicina"]*$kolicinaPaketa;
                    else $kolicine[$j["Id"]]=+$j["Kolicina"]*$kolicinaPaketa;
                }
            }
        }
        ksort($kolicine);
        //var_dump($kolicine);
        $q = "SELECT Id_Itema Id, Kolicina FROM Trgovina_Item WHERE Id_Trgovine = {$idTrgovine} AND Id_Itema IN (";
        //var_dump($kolicine);
        foreach ($kolicine as $k=>$v){
            $q.=$k.", ";
        }
        $q = substr($q,0,-2);
        $q.=") ORDER BY Id_Itema ASC";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_all(MYSQLI_ASSOC);
        //var_dump($stmt);
        $goodAmounts = true;
        foreach ($stmt as &$i){
            $dobiveneKolicine[$i["Id"]] = $i["Kolicina"];
        }
        
        foreach ($dobiveneKolicine as $k=>$v){
            if ($dobiveneKolicine[$k] < $kolicine[$k])
                $goodAmounts=false;
        }
        //echo "IS GOOD AMOUNT?".strval($goodAmounts);
        return $goodAmounts;
    }
    
    public function reduceAmount($post){
        $racun = $this->getInvoice($post);//Id_Racuna i KorisnickoIme
        //$idTrgovine = $post["Id_Trgovine"];
        if ($this->checkAmounts($post)==false) return false;
        
        $q = "SELECT Id_Trgovine FROM Racun WHERE Id = {$post["Id_Racuna"]}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idTrgovine = $stmt["Id_Trgovine"];
        
        
        foreach ($racun["Stavke"] as &$i){
            if ($i["ItemType"]=="Proizvod"){
                if (isset($kolicine[$i["Id"]])) $kolicine[$i["Id"]]+=$i["Kolicina"];
                else $kolicine[$i["Id"]]=+$i["Kolicina"]*1;
            }
            else {
                $kolicinaPaketa = $i["Kolicina"];
                foreach ($i["StavkePaketa"] as &$j){
                    if (isset($kolicine[$j["Id"]])) $kolicine[$j["Id"]]+=$j["Kolicina"]*$kolicinaPaketa;
                    else $kolicine[$j["Id"]]=+$j["Kolicina"]*$kolicinaPaketa;
                }
            }
        }
        
        ksort($kolicine);
        //var_dump($kolicine);
        $q = "SELECT Id_Itema Id, Kolicina FROM Trgovina_Item WHERE Id_Trgovine = {$idTrgovine} AND Id_Itema IN (";
        foreach ($kolicine as $k=>$v){
            $q.=$k.", ";
        }
        $q = substr($q,0,-2);
        $q.=") ORDER BY Id_Itema ASC";
        //secho $q;
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_all(MYSQLI_ASSOC);
        //var_dump($stmt);
        
        foreach ($stmt as &$i){
            $dobiveneKolicine[$i["Id"]] = $i["Kolicina"];
        }
        //var_dump($dobiveneKolicine);
        //var_dump($kolicine);
        $q = "UPDATE Trgovina_Item SET Kolicina = CASE Id_Itema ";
        foreach ($dobiveneKolicine as $k=>$v){
            $novaKolicina = $v - $kolicine[$k];
            $q.="WHEN {$k} THEN {$novaKolicina} ";
        }
        $q.="END WHERE Id_Trgovine={$idTrgovine}";
        //echo $q;
        $stmt = $this->conn->query($q);
        return true;
    }
	
	public function getInvoice($post) {
        $q = "SELECT Id, Id_Uloge From Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND Obrisan=0";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idKorisnika = $stmt["Id"];
        
        
        $q = "SELECT r.Id, r.MjestoIzdavanja, r.DatumIzdavanja, r.Popust, r.Id_Trgovine, t.Naziv Trgovina, r.Kupac, k.Ime Ime_Klijenta, k.Prezime Prezime_Klijenta, k.KorisnickoIme"
                    . " FROM Racun r"
                    . " JOIN Trgovina t ON r.Id_Trgovine = t.Id"
                    . " LEFT JOIN Korisnik k ON r.Kupac = k.Id"
                    . " WHERE r.Id = {$post["Id_Racuna"]}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        
        $response["Id"] = $stmt["Id"];
        $response["MjestoIzdavanja"] = $stmt["MjestoIzdavanja"];
        $response["DatumIzdavanja"] = $stmt["DatumIzdavanja"];
        $unixDatum = strtotime($response["DatumIzdavanja"]);
        
        //echo ($unixDatum);
        $response["Id_Trgovine"] = $stmt["Id_Trgovine"];
        $response["Trgovina"] = $stmt["Trgovina"];
        $response["Kupac"] = $stmt["Kupac"];
        $response["Ime_Klijenta"] = $stmt["Ime_Klijenta"];
        $response["Prezime_Klijenta"] = $stmt["Prezime_Klijenta"];
        $response["KorisnickoIme"] = $stmt["KorisnickoIme"];
        $response["CijenaRacuna"] = 0;
        $response["PopustRacuna"] = $stmt["Popust"];
        $response["IznosPopustaRacuna"]=0;
        $response["ZavrsnaCijena"] = 0;
        
        
        $q = "SELECT r.Id_Itema Id, i.Naziv, i.Opis, i.Slika, r.Kolicina"
                . " FROM Item_Racun r"
                . " JOIN Item i"
                . " ON r.Id_Itema = i.Id"
                . " WHERE Id_Racuna = {$post["Id_Racuna"]}";
                
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_all(MYSQLI_ASSOC);
        //echo ($q);
        //var_dump($stmt);
        
        foreach ($stmt as &$i){
            
            $q = "SELECT Id_Itema Id FROM Proizvod WHERE Id_Itema = {$i["Id"]}";
            $stmt2 = $this->conn->query($q);
            $stmt2 = $stmt2->fetch_assoc();
            $k = $stmt2["Id"];
            //echo $i;
            if ($k==NULL){ // ako je paket
                $i["ItemType"] = "Paket";
                $i["Cijena"]=0;
                $i["CijenaNakonPopusta"]=0;
                $i["CijenaStavke"]=0;
                $i["Popust"]=0;
                $i["IznosPopusta"]=0;
                $i["CijenaStavkeNakonPopusta"]=0;
                
                $cijenaPaketa = 0;
                $p["Id"] = $i["Id"];
                $p["UnixDatum"] = $unixDatum;
                
                $stmt3 = $this->getContentsOfPackage($p);
                $i["StavkePaketa"] = $stmt3;
                foreach ($i["StavkePaketa"] as &$j){
                    $j["CijenaStavke"] = strval($j["Cijena"] * $j["Kolicina"]);
                    $cijenaPaketa+=$j["Cijena"] * $j["Kolicina"];
                }
                $i["Cijena"] = strval($cijenaPaketa);
                $i["CijenaStavke"] = strval($cijenaPaketa * $i["Kolicina"]);
                $q="SELECT Popust FROM Paket_Popust WHERE Id_Paketa = {$i["Id"]} AND UnixVrijeme < {$unixDatum} ORDER BY UnixVrijeme DESC LIMIT 1";
                $stmt3 = $this->conn->query($q);
                $stmt3 = $stmt3->fetch_assoc();
                $i["Popust"] = $stmt3["Popust"];
                $i["CijenaNakonPopusta"] = strval(round($cijenaPaketa*$i["Popust"]/100,2));
                $i["IznosPopusta"] = strval(round($i["Popust"] * $i["CijenaStavke"] / 100,2));
                $i["CijenaStavkeNakonPopusta"] = strval($i["CijenaStavke"] - $i["IznosPopusta"]);
                $response["CijenaRacuna"] += $i["CijenaStavkeNakonPopusta"];
            }
            else{ // ako je proizvod
                $q="SELECT Cijena FROM Proizvod_Cijena WHERE Id_Proizvod = {$i["Id"]} AND UnixVrijeme < {$unixDatum} ORDER BY UnixVrijeme DESC LIMIT 1";
                $i["ItemType"] = "Proizvod";
                $stmt3 = $this->conn->query($q);
                $stmt3 = $stmt3->fetch_assoc();
                $i["Cijena"] = $stmt3["Cijena"];
                $i["CijenaStavke"] = strval($i["Cijena"] * $i["Kolicina"]);
                $response["CijenaRacuna"] += $i["CijenaStavke"];
            }
            
        }
        $response["CijenaRacuna"] = strval($response["CijenaRacuna"]);
        $response["IznosPopustaRacuna"] = strval(round($response["CijenaRacuna"] * $response["PopustRacuna"]/100,2));
        $response["ZavrsnaCijena"] = strval($response["CijenaRacuna"] - $response["IznosPopustaRacuna"]);
        //var_dump($stmt);
        $response["Stavke"]=$stmt;
        return $response;
        
    }
    
    public function getAllInvoices($post){
        $event = $this->getCurrentEvent();
        $q = "SELECT Id, Id_Uloge From Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idKorisnika = $stmt["Id"];
        $idUloge = $stmt["Id_Uloge"];
        if ($idUloge == 1){ //kupac
            $q = "SELECT r.Id"
                    . " FROM Racun r"
                    . " JOIN Trgovina t ON r.Id_Trgovine = t.Id"
                    . " JOIN Korisnik k ON r.Kupac = k.Id"
                    . " WHERE Kupac = {$idKorisnika} AND k.Obrisan=0 AND (k.Id_Eventa={$event["Id"]} OR k.Id_Eventa IS NULL) AND (r.Id_Eventa={$event["Id"]}) ORDER BY DatumIzdavanja ASC";
        }
        elseif ($idUloge == 2){ //admin
            $q = "SELECT r.Id"
                    . " FROM Racun r"
                    . " JOIN Trgovina t ON r.Id_Trgovine = t.Id"
                    . " JOIN Korisnik k ON r.Kupac = k.Id AND (k.Id_Eventa={$event["Id"]} OR k.Id_Eventa IS NULL) AND (r.Id_Eventa={$event["Id"]}) ORDER BY DatumIzdavanja ASC";
        }
        elseif ($idUloge == 3){ //prodavac
            $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = {$idKorisnika}";
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_assoc();
            $idTrgovine = $stmt["Id_Trgovina"];
            if (empty($idTrgovine)){
                return false;
            }
            $q = "SELECT r.Id"
                    . " FROM Racun r"
                    . " JOIN Trgovina t ON r.Id_Trgovine = t.Id"
                    . " JOIN Korisnik k ON r.Kupac = k.Id"
                    . " WHERE Id_Trgovine = {$idTrgovine} AND (k.Id_Eventa={$event["Id"]} OR k.Id_Eventa IS NULL) AND (r.Id_Eventa={$event["Id"]}) ORDER BY DatumIzdavanja ASC";
        }
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_all(MYSQLI_ASSOC);
        
        $response=[];
        foreach ($stmt as &$i){
            $p["Id_Racuna"] = $i["Id"];
            $p["KorisnickoIme"] = $post["KorisnickoIme"];
            array_push($response, $this->getInvoice($p));
        }
        
        return $response;
    }
	
	public function getPacketsWithProducts($post){
        $event=$this->getCurrentEvent();
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $userId = $stmt["Id"];
        $roleId=$stmt["Id_Uloge"];
        $response[0]= $stmt["Id_Uloge"];
        if ($roleId==1){
            return $response;
        }
        $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = {$userId}";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $storeId=$stmt["Id_Trgovina"];
        
        //echo "Store id: " . $storeId;
        
        if ($roleId == 3){ // ako je prodavac
            /*$q = "SELECT fin2.Id, fin2.Naziv, fin2.Opis, fin2.Popust, fin2.Slika FROM "
                    . "(SELECT fin.Id, fin.Naziv, fin.Opis, fin.Popust, fin.Slika "
                    ."FROM "
                    ."(SELECT svi.*, tp.Id_Trgovine "
                    ."FROM "
                    ."(SELECT a.*, b.Popust "
                    ."FROM Item a "
                    ."LEFT JOIN "
                    ."(SELECT c.Id_Paketa, d.Popust, c.UnixVrijeme "
                    ."FROM "
                    ."(SELECT Id_Paketa, MAX(UnixVrijeme) UnixVrijeme "
                    ."FROM Paket_Popust "
                    ."GROUP BY Id_Paketa) c "
                    ."JOIN Paket_Popust d "
                    ."ON c.Id_Paketa = d.Id_Paketa AND d.UnixVrijeme = c.UnixVrijeme ) b "
                    ."ON a.Id = b.Id_Paketa) svi "
                    ."JOIN Trgovina_Item tp "
                    ."ON tp.Id_Itema = svi.id "
                    ."WHERE svi.Izbrisan=0) fin "
                    ."WHERE Id_Trgovine = {$storeId}) fin2 "
                    ."JOIN Paket ON Paket.Id_Itema=fin2.Id ";*/
            $q = "SELECT fin.Id, fin.Naziv, fin.Opis, fin.Popust, fin.Slika FROM (SELECT svi.*, tp.Id_Trgovine, tp.Kolicina FROM (SELECT a.*, b.Popust FROM Item a LEFT JOIN (SELECT c.Id_Paketa, d.Popust, c.UnixVrijeme FROM (SELECT Id_Paketa, MAX(UnixVrijeme) UnixVrijeme FROM Paket_Popust GROUP BY Id_Paketa) c JOIN Paket_Popust d ON c.Id_Paketa = d.Id_Paketa AND d.UnixVrijeme = c.UnixVrijeme ) b ON a.Id = b.Id_Paketa) svi JOIN Trgovina_Item tp ON tp.Id_Itema = svi.id WHERE svi.Izbrisan=0) fin JOIN Paket ON fin.Id = Paket.Id_Itema WHERE Id_Trgovine = '{$storeId}'";
            
        } elseif ($roleId == 2) { // ako je admin
            /*$q = "SELECT fin.Id, fin.Naziv, fin.Opis, fin.Popust, fin.Slika "
                    . "FROM "
                    . "(SELECT svi.*, tp.Id_Trgovine "
                    . "FROM "
                    . "(SELECT a.*, b.Popust "
                    . "FROM Item a "
                    . "LEFT JOIN "
                    . "(SELECT c.Id_Paketa, d.Popust, c.UnixVrijeme "
                    . "FROM "
                    . "(SELECT Id_Paketa, MAX(UnixVrijeme) UnixVrijeme "
                    . "FROM Paket_Popust "
                    . "GROUP BY Id_Paketa) c "
                    . "JOIN Paket_Popust d "
                    . "ON c.Id_Paketa = d.Id_Paketa AND d.UnixVrijeme = c.UnixVrijeme ) b "
                    . "ON a.Id = b.Id_Paketa) svi "
                    . "JOIN Trgovina_Item tp "
                    . "ON tp.Id_Itema = svi.id "
                    . "WHERE svi.Izbrisan=0) fin "
                    . "JOIN Paket ON Paket.Id_Itema=fin.Id ";*/
            $q = "SELECT fin.Id, fin.Naziv, fin.Opis, fin.Popust, fin.Slika FROM (SELECT svi.*, tp.Id_Trgovine, tp.Kolicina FROM (SELECT a.*, b.Popust FROM Item a LEFT JOIN (SELECT c.Id_Paketa, d.Popust, c.UnixVrijeme FROM (SELECT Id_Paketa, MAX(UnixVrijeme) UnixVrijeme FROM Paket_Popust GROUP BY Id_Paketa) c JOIN Paket_Popust d ON c.Id_Paketa = d.Id_Paketa AND d.UnixVrijeme = c.UnixVrijeme ) b ON a.Id = b.Id_Paketa) svi JOIN Trgovina_Item tp ON tp.Id_Itema = svi.id WHERE svi.Izbrisan=0) fin JOIN Paket ON fin.Id = Paket.Id_Itema ";
        }
        $stmt = $this->conn->query($q);
        
        $vv=[];
        $gottenPackages = $stmt->fetch_all(MYSQLI_ASSOC);
        foreach ($gottenPackages as &$i){
            $i["CijenaStavkeNakonPopusta"]=0;
            $i["StavkePaketa"] = $this->getContentsOfPackage($i);
            
            foreach ($i["StavkePaketa"] as &$j){
                //echo $j["CijenaStavke"]." ";
                //echo $i["CijenaStavkeNakonPopusta"]." ";
                $i["CijenaStavkeNakonPopusta"]=$i["CijenaStavkeNakonPopusta"]+$j["CijenaStavke"];
            }
            $i["CijenaStavke"] = strval($i["CijenaStavkeNakonPopusta"]);
            $i["CijenaStavkeNakonPopusta"] = strval(round($i["CijenaStavkeNakonPopusta"]*(100-$i["Popust"])/100,2));
            array_push($vv,$i);
        }
        
        //var_dump($vv);
        //$response[1] = $stmt->fetch_all(MYSQLI_ASSOC);
        $response[1]=$vv;
        return $response;
    }
	
	public function getSumOfInvoices($post){
        $event=$this->getCurrentEvent();
        $vv=[];
        $q = "SELECT Id, Id_Uloge From Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}' AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $idKorisnika = $stmt["Id"];

        $q = "SELECT Id FROM Racun WHERE Kupac = '$idKorisnika'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_all(MYSQLI_ASSOC);
        foreach ($stmt as $i) {
            $idRacuna = $i["Id"];

            $q = "SELECT r.Id, r.MjestoIzdavanja, r.DatumIzdavanja, r.Popust, r.Id_Trgovine, t.Naziv Trgovina, r.Kupac, k.Ime Ime_Klijenta, k.Prezime Prezime_Klijenta, k.KorisnickoIme"
                    . " FROM Racun r"
                    . " JOIN Trgovina t ON r.Id_Trgovine = t.Id"
                    . " LEFT JOIN Korisnik k ON r.Kupac = k.Id"
                    . " WHERE r.Id = '$idRacuna' AND (k.Id_Eventa = {$event["Id"]} OR k.Id_Eventa IS NULL)";//MAYBE
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_assoc();


            $datumIzdavanja = $stmt["DatumIzdavanja"];
            $unixDatum = strtotime($datumIzdavanja);

            $response1["ZavrsnaCijena"] = 0;


            $q = "SELECT r.Id_Itema Id, i.Naziv, i.Opis, i.Slika, r.Kolicina"
                    . " FROM Item_Racun r"
                    . " JOIN Item i"
                    . " ON r.Id_Itema = i.Id"
                    . " WHERE Id_Racuna = '$idRacuna'";

            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_all(MYSQLI_ASSOC);

            foreach ($stmt as &$i) {

                $q = "SELECT Id_Itema Id FROM Proizvod WHERE Id_Itema = {$i["Id"]}";
                $stmt2 = $this->conn->query($q);
                $stmt2 = $stmt2->fetch_assoc();
                $k = $stmt2["Id"];
                //echo $i;
                if ($k == NULL) { // ako je paket
                    $i["ItemType"] = "Paket";
                    $i["Cijena"] = 0;
                    $i["CijenaNakonPopusta"] = 0;
                    $i["CijenaStavke"] = 0;
                    $i["Popust"] = 0;
                    $i["IznosPopusta"] = 0;
                    $i["CijenaStavkeNakonPopusta"] = 0;

                    $cijenaPaketa = 0;
                    $p["Id"] = $i["Id"];
                    $p["UnixDatum"] = $unixDatum;

                    $stmt3 = $this->getContentsOfPackage($p);
                    $i["StavkePaketa"] = $stmt3;
                    foreach ($i["StavkePaketa"] as &$j) {
                        $j["CijenaStavke"] = strval($j["Cijena"] * $j["Kolicina"]);
                        $cijenaPaketa += $j["Cijena"] * $j["Kolicina"];
                    }
                    $i["Cijena"] = strval($cijenaPaketa);
                    $i["CijenaStavke"] = strval($cijenaPaketa * $i["Kolicina"]);
                    $q = "SELECT Popust FROM Paket_Popust WHERE Id_Paketa = {$i["Id"]} AND UnixVrijeme < {$unixDatum} ORDER BY UnixVrijeme DESC LIMIT 1";
                    $stmt3 = $this->conn->query($q);
                    $stmt3 = $stmt3->fetch_assoc();
                    $i["Popust"] = $stmt3["Popust"];
                    $i["CijenaNakonPopusta"] = strval(round($cijenaPaketa * $i["Popust"] / 100, 2));
                    $i["IznosPopusta"] = strval(round($i["Popust"] * $i["CijenaStavke"] / 100, 2));
                    $i["CijenaStavkeNakonPopusta"] = strval($i["CijenaStavke"] - $i["IznosPopusta"]);
                    $cijenaRacuna += $i["CijenaStavkeNakonPopusta"];
                } else { // ako je proizvod
                    $q = "SELECT Cijena FROM Proizvod_Cijena WHERE Id_Proizvod = {$i["Id"]} AND UnixVrijeme < {$unixDatum} ORDER BY UnixVrijeme DESC LIMIT 1";
                    $i["ItemType"] = "Proizvod";
                    $stmt3 = $this->conn->query($q);
                    $stmt3 = $stmt3->fetch_assoc();
                    $i["Cijena"] = $stmt3["Cijena"];
                    $i["CijenaStavke"] = strval($i["Cijena"] * $i["Kolicina"]);
                    $cijenaRacuna += $i["CijenaStavke"];
                }
            }

            $response1["ZavrsnaCijena"] = strval($cijenaRacuna - $response1["IznosPopustaRacuna"]);
            array_push($vv,$response1);
        }
        
        $response=$vv;
        return $response;
    }
	
	public function confirmRegistration($post){
        $event=$this->getCurrentEvent();
        if ($post["CONFIRM"]=="true"){
            $q = "SELECT k.KorisnickoIme FROM Korisnik k WHERE KorisnickoIme='{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            $user = $stmt->fetch_assoc();
            if (!empty($user)){
                $q = "UPDATE Korisnik SET PrijavaPotvrdena = 1 WHERE KorisnickoIme = '{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
                $stmt = $this->conn->query($q);
                return true;
            }
            else
                return false;
        }
        elseif ($post["CONFIRM"]=="false"){
            $q = "SELECT k.KorisnickoIme FROM Korisnik k WHERE KorisnickoIme='{$post["KorisnickoImeKorisnik"]}' AND k.Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            $user = $stmt->fetch_assoc();
            if (!empty($user)){
                $q = "UPDATE Korisnik SET PrijavaPotvrdena = 0 WHERE KorisnickoIme = '{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
                $stmt = $this->conn->query($q);
                return true;
            }
            else
                return false;
        }
    }
    
    public function userConfirmed($post){
        $event = $this->getCurrentEvent();
        $q = "SELECT KorisnickoIme, PrijavaPotvrdena FROM Korisnik k WHERE KorisnickoIme='{$post["KorisnickoIme"]}' AND Obrisan=0  AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $user = $stmt->fetch_assoc();
        if ($user["PrijavaPotvrdena"]==0) return 0;
        else return 1;
    }
    
    public function getAllUsers($post){
        $event = $this->getCurrentEvent();
        $q = "SELECT k.Id, k.Ime, k.Prezime, k.Email, k.KorisnickoIme, k.DozvolaUpravljanjeUlogama, k.DozvolaUpravljanjeStanjemRacuna, k.DozvolaPregledTransakcija, k.DozvolaUvidUStatistiku, k.Id_Uloge, u.Naziv, k.Jezik, k.PrijavaPotvrdena "
                . "FROM Korisnik k JOIN Uloga u ON (k.Id_Uloge=u.Id) WHERE k.Obrisan=0 AND (k.Id_Eventa={$event["Id"]} OR k.Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $user = $stmt->fetch_all(MYSQLI_ASSOC);
        foreach ($user as &$i){
            if ($i["Id_Uloge"]==3){
                $q = "SELECT t.Id, t.Naziv FROM Trgovina_Korisnik k JOIN Trgovina t ON k.Id_Trgovina = t.Id WHERE k.Id_Korisnik = {$i["Id"]}";
                $stmt2 = $this->conn->query($q);
                $stmt2 = $stmt2->fetch_assoc();
                $i["Id_Trgovine"]=$stmt2["Id"];
                $i["Naziv_Trgovine"]=$stmt2["Naziv"];
                
                $p["KorisnickoIme"]= $i["KorisnickoIme"];
                $balance = $this->getBalanceStore($p);
                $i["StanjeRacunaTrgovine"]=$balance;
            }
            if ($i["Id_Uloge"]==1){
                $p["KorisnickoIme"]= $i["KorisnickoIme"];
                $balance = $this->getBalance($p);
                $i["StanjeRacuna"]=$balance;
            }
            //var_dump($i);
        }
        return $user;
    }
    
    public function isAdmin($korIme){
        $event = $this->getCurrentEvent();
        $q = "SELECT KorisnickoIme, Id_Uloge FROM Korisnik k WHERE KorisnickoIme = '{$korIme}' AND Obrisan=0 AND (k.Id_Eventa={$event["Id"]} OR k.Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $user = $stmt->fetch_assoc();
        if ($user["Id_Uloge"]!=2){
            return false;
        }
        else return true;
    }
    
    public function setRole($post){
        $event=$this->getCurrentEvent();
        $q = "SELECT Id, KorisnickoIme FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $user = $stmt->fetch_assoc();
        $id=$user["Id"];
        
        if ($post["RoleId"]==3){ //Prodavač
            $q = "SELECT * FROM Trgovina_Korisnik WHERE Id_Korisnik = {$id}";
            $stmt = $this->conn->query($q);

            $isInStore = $stmt->fetch_assoc();
            if ($isInStore!=false){
                $q = "DELETE FROM Trgovina_Korisnik WHERE  Id_Korisnik = {$id}";
                $stmt = $this->conn->query($q);
            }

            //$q = "INSERT INTO Trgovina_Korisnik (Id, Id_Trgovina, Id_Korisnik) VALUES (null, {$post["Id_Trgovine"]}, {$id})";
            //$stmt = $this->conn->query($q);
            $q = "UPDATE Korisnik SET Id_Uloge=3 WHERE KorisnickoIme = '{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
        }
        elseif($post["RoleId"]==1){ //Kupac
            $q = "UPDATE Korisnik SET Id_Uloge=1 WHERE KorisnickoIme = '{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);

            $q = "DELETE FROM Trgovina_Korisnik WHERE Id_Korisnik = {$id}";
            $stmt = $this->conn->query($q);
        }
        elseif($post["RoleId"]==2){ //Admin
            $q = "UPDATE Korisnik SET Id_Uloge=2 WHERE KorisnickoIme = '{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);

            $q = "DELETE FROM Trgovina_Korisnik WHERE Id_Korisnik = {$id}";
            $stmt = $this->conn->query($q);
        }
        $q = "SELECT Naziv FROM Uloga WHERE Id={$post["RoleId"]}";
        $stmt = $this->conn->query($q);
        $role = $stmt->fetch_assoc();
        return $role["Naziv"];
    }
    
    public function deleteUser($post){
        $event=$this->getCurrentEvent();
        $q = "DELETE FROM Trgovina_Korisnik WHERE Id_Korisnik = {$id}";
        $stmt = $this->conn->query($q);
        
        $q = "SELECT Id, KorisnickoIme FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $user = $stmt->fetch_assoc();
        $id=$user["Id"];
        
        $q = "UPDATE Korisnik SET Obrisan=1 WHERE Id={$id}";
        $stmt = $this->conn->query($q);
    }
    
    public function getRoles(){
        $q = "SELECT Id, Naziv FROM Uloga";
        $stmt = $this->conn->query($q);
        $roles = $stmt->fetch_all(MYSQLI_ASSOC);
        return $roles;
    }
	
	public function getStores(){
        $event = $this->getCurrentEvent();
        $q = "SELECT t.Id Id_Trgovine, t.Naziv Naziv_Trgovine, stanje.StanjeRacuna FROM (
		SELECT c.Id_Trgovine, d.StanjeRacuna, c.UnixVrijeme FROM (
			SELECT Id_Trgovine, MAX(UnixVrijeme) UnixVrijeme FROM Trgovina_StanjeRacuna GROUP BY Id_Trgovine
		) c JOIN Trgovina_StanjeRacuna d ON c.Id_Trgovine = d.Id_Trgovine AND d.UnixVrijeme = c.UnixVrijeme
	) stanje JOIN Trgovina t ON stanje.Id_Trgovine=t.id WHERE t.Obrisan=0  AND (t.Id_Eventa={$event["Id"]})";
        $stmt = $this->conn->query($q);
        $stores = $stmt->fetch_all(MYSQLI_ASSOC);
        foreach ($stores as &$i){
            $q = "SELECT k.Ime, k.Prezime, k.Email, k.KorisnickoIme FROM Trgovina_Korisnik tk JOIN Korisnik k ON k.Id=tk.Id_Korisnik JOIN Trgovina t ON t.Id=tk.Id_Trgovina WHERE t.Obrisan=0 AND t.Id={$i["Id_Trgovine"]}";
            $stmt = $this->conn->query($q);
            $i["BrojZaposlenika"]=$stmt->num_rows;
            $i["Zaposlenici"]=$stmt->fetch_all(MYSQLI_ASSOC);
        }
        return $stores;
    }
    
    public function assignStore($post){
        $event=$this->getCurrentEvent();
        $q = "SELECT Id, KorisnickoIme FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoImeKorisnik"]}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt = $this->conn->query($q);
        $user = $stmt->fetch_assoc();
        $id=$user["Id"];
        
        $q = "DELETE FROM Trgovina_Korisnik WHERE  Id_Korisnik = {$id}";
        $stmt = $this->conn->query($q);
        
        if ($post["ASSIGNSTORE"]=="true"){
            $q = "SELECT Naziv FROM Trgovina WHERE Id={$post["Id_Trgovine"]} AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            if ($stmt->num_rows==0){
                return -1;
            }
            $q = "INSERT INTO Trgovina_Korisnik (Id, Id_Trgovina, Id_Korisnik) VALUES (null, {$post["Id_Trgovine"]}, {$id})";
            $stmt = $this->conn->query($q);
            return 1;
        }
        return 0;
    }
    
    public function getStoreName($storeId){
        $q = "SELECT Naziv FROM Trgovina WHERE Id={$storeId} AND Obrisan=0";
        $stmt=$this->conn->query($q);
        $name=$stmt->fetch_assoc();
        return $name["Naziv"];
    }
	
	public function createStore($post){
        $event=$this->getCurrentEvent();
        $q = "SELECT Naziv FROM Trgovina WHERE Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt= $this->conn->query($q);
        $storeNames = $stmt->fetch_all(MYSQLI_ASSOC);
        foreach ($storeNames as &$i){
            if ($i["Naziv"]==$post["NazivTrgovine"]){
                return false;
            }
        }
        $q = "INSERT INTO Trgovina (Id, Naziv, Id_Eventa) VALUES (null, '{$post["NazivTrgovine"]}', {$event["Id"]})";
        $stmt = $this->conn->query($q);
        $id=$this->conn->insert_id;
        $time = time();
        $q = "INSERT INTO Trgovina_StanjeRacuna (Id_Trgovine, UnixVrijeme, StanjeRacuna) VALUES ({$id}, {$time}, 0)";
        $stmt = $this->conn->query($q);
        return $id;
    }
    
    public function deleteStore($post){
        $event=$this->getCurrentEvent();
        $q = "SELECT Naziv FROM Trgovina WHERE Id={$post["Id_Trgovine"]} AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
        $stmt=$this->conn->query($q);
        if ($stmt->num_rows==0){
            return false;
        }
        $q = "UPDATE Trgovina SET Obrisan=1 WHERE Id={$post["Id_Trgovine"]}";
        $stmt2 = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        return $stmt["Naziv"];
    }
    
    public function createStoresInBulk($post){
        $event=$this->getCurrentEvent();
        $ids=[];
        $offset=1;
        $time = time();
        for($i=0;$i<$post["BrojTrgovina"];$i++){
            again:
            $num = $i+$offset;
            $name = "Trgovina {$num}{$post["Sufiks"]}";
            $q = "SELECT * FROM Trgovina WHERE Naziv='{$name}' AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            if ($stmt->num_rows==0){
                //$qI.="(null, '{$name}'), ";
                $q = "INSERT INTO Trgovina (Id, Naziv, Id_Eventa) VALUES (null, '{$name}', {$event["Id"]})";
                $stmt = $this->conn->query($q);
                $store["Id_Trgovine"]=$this->conn->insert_id;
                $store["Naziv_Trgovine"]=$name;
                array_push($ids, $store);
                $q = "INSERT INTO Trgovina_StanjeRacuna (Id_Trgovine, UnixVrijeme, StanjeRacuna) VALUES ({$store["Id_Trgovine"]}, {$time}, 0)";
                $stmt = $this->conn->query($q);
            }
            else {
                $offset+=1;
                goto again;
            }
        }
        return $ids;
    }
    
    public function editStore($post){
        $event=$this->getCurrentEvent();
        if (isset($post["NazivTrgovine"])){
            $q = "SELECT Naziv FROM Trgovina WHERE Id = {$post["Id_Trgovine"]} AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            if ($stmt->num_rows==0){
                return false;
            }
            else {
                $q = "UPDATE Trgovina SET Naziv = '{$post["NazivTrgovine"]}' WHERE Id={$post["Id_Trgovine"]} AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
                $stmt = $this->conn->query($q);
                //return true;
            }
        }
        if (isset($post["StanjeRacuna"])){
            $q = "SELECT Naziv FROM Trgovina WHERE Id = {$post["Id_Trgovine"]} AND Obrisan=0 AND (Id_Eventa = {$event["Id"]} OR Id_Eventa IS NULL)";
            $stmt = $this->conn->query($q);
            if ($stmt->num_rows==0){
                return false;
            }
            else {
                $time=time();
                $q = "INSERT INTO Trgovina_StanjeRacuna (Id_Trgovine, UnixVrijeme, StanjeRacuna) VALUES ({$post["Id_Trgovine"]}, {$time}, {$_POST["StanjeRacuna"]})";
                $stmt = $this->conn->query($q);
                //return true;
            }
        }
        return true;
    }
    

}
?>