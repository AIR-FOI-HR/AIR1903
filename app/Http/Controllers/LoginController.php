<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;

class LoginController extends Controller
{
    protected $redirectTo = '/';
	public $api_url = "https://localhost/pop/api/v1/";

    public function index()
    {
        return view('login');
    }

    public function authenticate()
    {
		
        $_POST['KorisnickoIme'] = request('username');
        $_POST['Lozinka'] = request('password');

        $ch = curl_init($this->api_url . 'login.php');
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
		curl_setopt($ch, CURLOPT_POST, count($_POST));
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		
		//TODO-Ukloniti kada se riješi problem sa certifikatom na serveru
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); 
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);

        $result = json_decode(curl_exec($ch), true);
		
        curl_close($ch);
        
        if($result['STATUS'] == true)
        {
            session(['authenticated' => time()]);
            session(['token' => $result['DATA']['Token']]);
            session(['korisnickoIme' => $result['DATA']['KorisnickoIme']]);

            session()->flash('success', "Uspješna prijava korisnika.");
            return redirect()->intended('/dashboard');
        }

        session()->flash('error', "Greška prilikom prijave korisnika.");
        
        return view('login');
    }
}
