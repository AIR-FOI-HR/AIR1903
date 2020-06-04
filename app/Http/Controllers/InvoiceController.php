<?php

namespace App\Http\Controllers;

class InvoiceController extends Controller
{
	public $api_url = "https://localhost/pop/api/v1/";
	
    public function index()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['Readall'] = 'true';

        $ch = curl_init($this->api_url . "racuni.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		
		//TODO-Ukloniti kada se riješi problem sa certifikatom na serveru
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); 
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
		
        $result = json_decode(curl_exec($ch), true);
        $invoices = $result['DATA'];
        curl_close($ch);
        
        return view('invoices.index', compact('invoices'));
    }

    public function show($id)
    {
        $invoice = request()->all();
        $invoice['Id'] = $id;
        return view('invoices.show', compact('invoice'));
    }

}