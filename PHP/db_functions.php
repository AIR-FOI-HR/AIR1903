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
    
    public function checkLogin($post){
        if(!isset($post["KorisnickoIme"])|| ctype_space($post["KorisnickoIme"]) || empty($post["KorisnickoIme"])) return "KorisnickoIme";
        if(!isset($post["Lozinka"])|| ctype_space($post["Lozinka"]) || empty($post["Lozinka"])) return "Lozinka";
        return 1;
    }

    public function storeUser($post, $hash) {
        
        $q = "INSERT INTO Korisnik (Id ,Ime, Prezime, Email, Id_Uloge, KorisnickoIme, StanjeRacuna, LozinkaSalt, LozinkaHash, DozvolaUpravljanjeUlogama, DozvolaUpravljanjeStanjemRacuna, DozvolaPregledTransakcija, DozvolaUvidUStatistiku) ";
        $q.="VALUES (null,'{$post["Ime"]}', '{$post["Prezime"]}','{$post["Email"]}', 1, '{$post["KorisnickoIme"]}', 0, '{$hash[0]}', '{$hash[1]}', 0, 0, 0, 0)";
        $stmt = $this->conn->query($q);
        $q = "SELECT k.Ime, k.Prezime, k.Email, k.KorisnickoIme, k.StanjeRacuna, k.DozvolaUpravljanjeUlogama, k.DozvolaUpravljanjeStanjemRacuna, k.DozvolaPregledTransakcija, k.DozvolaUvidUStatistiku, k.Id_Uloge, u.Naziv FROM Korisnik k JOIN Uloga u ON (k.Id_Uloge=u.Id) WHERE KorisnickoIme='{$post["KorisnickoIme"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $response["Ime"] = $stmt["Ime"];
        $response["Prezime"] = $stmt["Prezime"];
        $response["Email"] = $stmt["Email"];
        $response["KorisnickoIme"] = $stmt["KorisnickoIme"];
        $response["StanjeRacuna"] = $stmt["StanjeRacuna"];
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
        $q="SELECT KorisnickoIme FROM Korisnik WHERE KorisnickoIme='".$post["KorisnickoIme"]."'";
        $stmt = $this->conn->query($q);
        
        if ($stmt->num_rows > 0) {
            $stmt->close();
            return "KorisnickoIme";
        }
        
        $q="SELECT Email FROM Korisnik WHERE Email='".$post["Email"]."'";
        $stmt = $this->conn->query($q);
        
        if ($stmt->num_rows > 0) {
            $stmt->close();
            return "Email";
        }
        return 0;
    }
    
    public function userExistsLogin($post) {
        $q="SELECT KorisnickoIme FROM Korisnik WHERE KorisnickoIme='".$post["KorisnickoIme"]."'";
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
        $q="SELECT LozinkaSalt, LozinkaHash, KrivePrijave FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        
        $iterations = 10000;
        
        $response["Forbidden"]=false;
        $response["KrivePrijave"]=0;
        $response2=null;
        
        if ($stmt["KrivePrijave"]>=3){
            $response["Forbidden"]=true;
            return [$response, $response2];
        }
        
        $salt = base64_decode($stmt["LozinkaSalt"]);
        $hashDB = $stmt["LozinkaHash"];
        
        $hash = hash_pbkdf2("sha256", $post["Lozinka"], $salt, $iterations);
        $hash= base64_encode(pack('H*',$hash));
        
        
        if($hash==$hashDB){
            $q = "SELECT k.Ime, k.Prezime, k.Email, k.KorisnickoIme, k.StanjeRacuna, k.DozvolaUpravljanjeUlogama, k.DozvolaUpravljanjeStanjemRacuna, k.DozvolaPregledTransakcija, k.DozvolaUvidUStatistiku, k.Id_Uloge, u.Naziv FROM Korisnik k JOIN Uloga u ON (k.Id_Uloge=u.Id) WHERE KorisnickoIme='{$post["KorisnickoIme"]}'";
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_assoc();
            $response2["Ime"] = $stmt["Ime"];
            $response2["Prezime"] = $stmt["Prezime"];
            $response2["Email"] = $stmt["Email"];
            $response2["KorisnickoIme"] = $stmt["KorisnickoIme"];
            $response2["StanjeRacuna"] = $stmt["StanjeRacuna"];
            $response2["DozvolaUpravljanjeUlogama"] = $stmt["DozvolaUpravljanjeUlogama"];
            $response2["DozvolaUpravljanjeStanjemRacuna"] = $stmt["DozvolaUpravljanjeStanjemRacuna"];
            $response2["DozvolaPregledTransakcija"] = $stmt["DozvolaPregledTransakcija"];
            $response2["DozvolaUvidUStatistiku"] = $stmt["DozvolaUvidUStatistiku"];
            $response2["Id_Uloge"] = $stmt["Id_Uloge"];
            $response2["Naziv_Uloge"] = $stmt["Naziv"];
            $response2["LoginTime"] = time();
            $response2["Token"]=$this->generateAuth();
            
            $response2 = json_encode($response2);
            
            $q = "UPDATE Korisnik SET KrivePrijave = 0 WHERE KorisnickoIme='{$post["KorisnickoIme"]}'";
            $stmt = $this->conn->query($q);
            
            return [$response, $response2];
        }
        else{
            $q = "UPDATE Korisnik SET KrivePrijave = KrivePrijave+1 WHERE KorisnickoIme='{$post["KorisnickoIme"]}'";
            $stmt = $this->conn->query($q);
            $q = "SELECT KrivePrijave FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}'";
            $stmt = $this->conn->query($q);
            $stmt=$stmt->fetch_assoc();
            $response["KrivePrijave"]=$stmt["KrivePrijave"];
            return [$response, $response2];
        }
        
    }
	public function getAllProducts($post) {
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}'";
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
            $q="SELECT fin.Id, fin.Naziv, fin.Opis, fin.Cijena, fin.Slika, fin.Kolicina FROM ("
                ."SELECT svi.*, tp.Id_Trgovine, tp.Kolicina "
                ."FROM (SELECT a.*, b.Cijena "
                ."FROM Proizvod a "
                ."LEFT JOIN" 
                ."(SELECT c.Id_Proizvod, d.Cijena, c.UnixVrijeme "
                ."FROM "
                ."(SELECT Id_Proizvod, MAX(UnixVrijeme) UnixVrijeme "
                ."FROM Proizvod_Cijena "
                ."GROUP BY Id_Proizvod) c "
                ."JOIN Proizvod_Cijena d "
                ."ON c.Id_Proizvod = d.Id_Proizvod AND d.UnixVrijeme = c.UnixVrijeme ) b "
                ."ON a.Id = b.Id_Proizvod) svi "
                ."JOIN Trgovina_Proizvod tp "
                ."ON tp.Id_Proizvoda = svi.id "
                ." WHERE svi.Izbrisan=0) fin "
                ."WHERE Id_Trgovine={$storeId}";
        }
        elseif ($roleId == 2){ // ako je admin
            $q="SELECT fin.Id, fin.Naziv, fin.Opis, fin.Cijena, fin.Slika, fin.Kolicina FROM ("
                ."SELECT svi.*, tp.Id_Trgovine, tp.Kolicina "
                ."FROM (SELECT a.*, b.Cijena "
                ."FROM Proizvod a "
                ."LEFT JOIN" 
                ."(SELECT c.Id_Proizvod, d.Cijena, c.UnixVrijeme "
                ."FROM "
                ."(SELECT Id_Proizvod, MAX(UnixVrijeme) UnixVrijeme "
                ."FROM Proizvod_Cijena "
                ."GROUP BY Id_Proizvod) c "
                ."JOIN Proizvod_Cijena d "
                ."ON c.Id_Proizvod = d.Id_Proizvod AND d.UnixVrijeme = c.UnixVrijeme ) b "
                ."ON a.Id = b.Id_Proizvod) svi "
                ."JOIN Trgovina_Proizvod tp "
                ."ON tp.Id_Proizvoda = svi.id "
                ." WHERE svi.Izbrisan=0) fin ";
        }
        

        
        $stmt = $this->conn->query($q);
        $response[1] = $stmt->fetch_all(MYSQLI_ASSOC);
        return $response;
    }
	public function addNewProduct($post) {
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}'";
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
        
        $q = "INSERT INTO Proizvod (Id ,Naziv, Opis, Slika) ";
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
        $time = time();
        $q = "INSERT INTO Proizvod_Cijena (Id_Proizvod, UnixVrijeme, Cijena) VALUES "
                . "({$productId}, {$time}, {$post["Cijena"]})";
        $stmt = $this->conn->query($q);
        
        
        $q = "SELECT Naziv, Opis, Slika FROM Proizvod WHERE Id={$productId}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $response[1]["Naziv"] = $stmt["Naziv"];
        $response[1]["Opis"] = $stmt["Opis"];
        $response[1]["Slika"] = $stmt["Slika"];
        
        $q = "SELECT Cijena FROM Proizvod_Cijena WHERE Id_Proizvod={$productId} ORDER BY UnixVrijeme DESC LIMIT 1";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $response[1]["Cijena"] = $stmt["Cijena"];
        
        
        $q="INSERT INTO Trgovina_Proizvod (Id, Id_Trgovine, Id_Proizvoda, Kolicina) VALUES "
                . "(null, {$storeId}, $productId, {$post["Kolicina"]})";
        $stmt = $this->conn->query($q);
        
        
        return $response;
    }
	public function deleteProduct($post) {
        $q = "UPDATE Proizvod SET Izbrisan = 1 WHERE Id = {$post["Id"]}"; //promijenjeno
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
        $q = "UPDATE Proizvod SET Naziv = '{$post["Naziv"]}', Opis = '{$post["Opis"]}', Slika = '{$slika}' WHERE Proizvod.Id = '{$post["Id"]}'";
        $stmt = $this->conn->query($q);
        $time= time();
        $q = "INSERT INTO Proizvod_Cijena (Id_Proizvod, UnixVrijeme, Cijena) VALUES ({$post["Id"]}, {$time}, {$post["Cijena"]})";
        $stmt = $this->conn->query($q);
        $q = "UPDATE Trgovina_Proizvod SET Kolicina = {$post["Kolicina"]} WHERE Id_Proizvoda = {$post["Id"]}";
        $stmt = $this->conn->query($q);
        $response = "Proizvod je uspjesno azuriran!";
        return $response;
    }
	
	
    public function generateAuth(){
        
        $auth = openssl_random_pseudo_bytes(128);
        $authString = base64_encode($auth);
        $time=time()+6*60*60;
        $q = "INSERT INTO Tokeni (Token, UnixVrijemeIsteka) VALUES ('{$authString}',{$time});";
        $stmt = $this->conn->query($q);
        return $authString;
    }
    
    public function checkAuth($token){
        $time=time();
        $q = "SELECT ID FROM Tokeni WHERE Token = '{$token}' AND UnixVrijemeIsteka>{$time}";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        if (sizeof($stmt)!=0) return true;
        else return false;
        
    }
    public function addNewPackage($post) {

        $q = "INSERT INTO Paket (Id ,NazivPaketa, Popust) VALUES (null,'{$post["NazivPaketa"]}','{$post["Popust"]}')";
        $stmt = $this->conn->query($q);

        $q = "SELECT Id FROM Paket WHERE NazivPaketa = '{$post["NazivPaketa"]}'"; //ili ukoliko naziv paketa nije jedinstven SELECT MAX(Id) FROM Paket WHERE NAzivPaketa='{$post["NazivPaketa"]}'
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $packageId = $stmt["Id"];

        $q = "INSERT INTO Proizvod_Paket (Id, Id_Paketa, Id_Proizvoda, Kolicina) VALUES (null, '$packageId', '{$post["Id_Proizvoda"]}','{$post["Kolicina"]}')";
        $stmt2 = $this->conn->query($q);
        $q = "SELECT MAX(Id) FROM Proizvod_Paket";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $packageProductId = $stmt["MAX(Id)"];

        $q = "SELECT Kolicina, Id_Paketa, Id_Proizvoda FROM Proizvod_Paket WHERE Id='{$packageProductId}'";
        $stmt = $this->conn->query($q);
        $stmt2 = $stmt->fetch_assoc();
        $q = "SELECT NazivPaketa, Popust FROM Paket WHERE Id='{$packageId}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $response["NazivPaketa"] = $stmt["NazivPaketa"];
        $response["Popust"] = $stmt["Popust"];
        $response["Id_Paketa"] = $stmt2["Id_Paketa"];
        $response["Id_Proizvoda"] = $stmt2["Id_Paketa"];
        $response["Kolicina"] = $stmt2["Kolicina"];

        return $response;
    }
    public function checkPackageEmpty($post) {
        if (!isset($post["NazivPaketa"]) || empty($post["NazivPaketa"]) || !isset($post["Popust"]) || empty($post["Popust"]) || !isset($post["Id_Proizvoda"]) || empty($post["Id_Proizvoda"]) || !isset($post["Kolicina"]) || empty($post["Kolicina"])) {
            return 0;
        } else {
            return 1;
        }
    }
    public function deletePackage($post) {
        $q = "SELECT Id FROM Proizvod_Paket WHERE Id_Paketa = '{$post["Id"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $productPackageId = $stmt["Id"];
        $q = "DELETE FROM Proizvod_Paket WHERE Id = '$productPackageId'";
        $stmt = $this->conn->query($q);
       
        $q = "DELETE FROM Paket WHERE Id= '{$post["Id"]}'";
        $stmt = $this->conn->query($q);
        
        $response = null;
        return $response;
        
    }
    public function updatePackage($post) {
        $q = "SELECT Id FROM Proizvod_Paket WHERE Id_Paketa = '{$post["Id"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $productPackageId = $stmt["Id"];
        $q = "UPDATE Paket SET NazivPaketa = '{$post["NazivPaketa"]}', Popust = '{$post["Popust"]}' WHERE Id = '{$post["Id"]}'";
        $stmt = $this->conn->query($q);
        $q = "UPDATE Proizvod_Paket SET Id_Proizvoda = '{$post["Id_Proizvoda"]}', Kolicina = '{$post["Kolicina"]}' WHERE Id = '$productPackageId'";
        $stmt = $this->conn->query($q);
        $response = "Proizvod je uspjesno azuriran!";
        return $response;
    }
    public function getAllPackeges($post) {
        $q = "SELECT Id, Id_Uloge FROM Korisnik WHERE KorisnickoIme = '{$post["KorisnickoIme"]}'";
        $stmt=$this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        $userId = $stmt["Id"];
        $roleId=$stmt["Id_Uloge"];
        $response[0] = $stmt["Id_Uloge"];
        
        if ($roleId==1){
            return $response;
        }
         if ($roleId == 3) { // ako je prodavac
            $q = "SELECT Id_Trgovina FROM Trgovina_Korisnik WHERE Id_Korisnik = '$userId'";
            $stmt = $this->conn->query($q);
            $stmt = $stmt->fetch_assoc();
            $storeId = $stmt["Id_Trgovina"];
            $q = "SELECT Proizvod_Paket.Id_Proizvoda, Proizvod_Paket.Kolicina, Paket.Id, Paket.NazivPaketa, Paket.Popust FROM Proizvod_Paket LEFT OUTER JOIN Paket ON Paket.Id = Proizvod_Paket.Id_Paketa WHERE Paket.Id IN (SELECT Id_Paketa FROM Proizvod_Paket WHERE Id_Proizvoda IN (SELECT Id_Proizvoda FROM Trgovina_Proizvod WHERE Id_Trgovine = '$storeId'))";
            
        }elseif ($roleId == 2) {
            $q = "SELECT Proizvod_Paket.Id_Proizvoda, Proizvod_Paket.Kolicina, Paket.Id, Paket.NazivPaketa, Paket.Popust FROM Proizvod_Paket LEFT OUTER JOIN Paket ON Paket.Id = Proizvod_Paket.Id_Paketa WHERE Paket.Id IN (SELECT Id_Paketa FROM Proizvod_Paket WHERE Id_Proizvoda IN (SELECT Id_Proizvoda FROM Trgovina_Proizvod))";
        }
        $stmt = $this->conn->query($q);
        $response[1] = $stmt->fetch_all(MYSQLI_ASSOC);
        return $response;
    }

}
?>