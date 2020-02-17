<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;

class LoginController extends Controller
{
    protected $redirectTo = '/';

    public function index()
    {
        return view('login');
    }

    public function authenticate()
    {
        $_POST['KorisnickoIme'] = request('username');
        $_POST['Lozinka'] = request('password');
        
        $ch = curl_init("http://cortex.foi.hr/pop/login.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        if($result['STATUS'] == true)
        {
            session()->put('authenticated', time());
            return redirect()->intended('/dashboard');
        }
        
        return view('login');
    }
}
