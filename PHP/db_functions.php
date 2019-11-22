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
	
	public function checkRegister($post){
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

    public function hashPassword($post) {
        $salt = (32);
        //echo "Hashing\n";
        $saltB64= base64_encode($salt);
        //echo $saltB64."\n";
        $iterations = 10000;
        $hash = hash_pbkdf2("sha256", $post["Lozinka"], $salt, $iterations);
        $hashB64 = base64_encode(pack('H*',$hash));
        //echo $hashB64."\n";
        return [$saltB64,$hashB64];
    }

    public function checkPassword($post) {
        $q="SELECT LozinkaSalt, LozinkaHash FROM Korisnik WHERE KorisnickoIme='{$post["KorisnickoIme"]}'";
        $stmt = $this->conn->query($q);
        $stmt = $stmt->fetch_assoc();
        
        $iterations = 10000;
        
        $salt = base64_decode($stmt["LozinkaSalt"]);
        $hashDB = $stmt["LozinkaHash"];
        
        $hash = hash_pbkdf2("sha256", $post["Lozinka"], $salt, $iterations);
        $hash= base64_encode(pack('H*',$hash));
        
        if($hash==$hashDB){
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
            
            $response = json_encode($response);
            return $response;
        }
        else return 0;
        
    }
}
?>