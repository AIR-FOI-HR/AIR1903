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
public function getAllProducts() {
        $q = "SELECT Id, Naziv, Cijena, Opis, Slika FROM Proizvod";
        $stmt = $this->conn->query($q);
        $json_array = array();
        
        while($row = $stmt->fetch_assoc()){
            $json_array[] = $row;
        }
        return $json_array;
    }
public function addNewProduct($post) {
        $q = "INSERT INTO Proizvod (Id ,Naziv, Cijena, Opis, Slika) ";
        $q.= "VALUES (null,'{$post["Naziv"]}', '{$post["Cijena"]}','{$post["Opis"]}', '{$post["Slika"]}')";
        $stmt = $this->conn->query($q);
        return $post["Naziv"];
    }
 public function deleteProduct($post) {
        $q = "DELETE FROM Proizvod WHERE Id = '{$post["Id"]}'";
        $stmt = $this->conn->query($q);
        $response = "Proizvod je uspjesno obrisan!";
        return $response;
    }
public function checkProductEmpty($post) {
      if(!isset($post["Naziv"])|| empty($post["Naziv"]) || !isset($post["Cijena"]) || empty($post["Cijena"]) || !isset($post["Opis"])||  empty($post["Opis"]) || !isset($post["Slika"])|| empty($post["Slika"])){
            return 0;
        }else{
            return 1;
        }    
    }
public function isDelete($post) {
      
    }

}
?>