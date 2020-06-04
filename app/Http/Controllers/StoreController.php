<?php

namespace App\Http\Controllers;

class StoreController extends Controller
{
	public $api_url = "https://localhost/pop/api/v1/";
	
    public function index()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['Readall'] = 'true';

        $ch = curl_init($this->api_url . "trgovine.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		
		//TODO-Ukloniti kada se riješi problem sa certifikatom na serveru
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); 
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
		
        $result = json_decode(curl_exec($ch), true);
        $stores = $result['DATA'];
        curl_close($ch);

        return view('stores.index', compact('stores'));
    }

    public function createMultiple($sufixValue, $numberValue)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['BULKCREATESTORE'] = 'true';
        $_POST['Sufiks'] = $sufixValue;
        $_POST['BrojTrgovina'] = $numberValue;

        $ch = curl_init($this->api_url . "trgovine.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		
		//TODO-Ukloniti kada se riješi problem sa certifikatom na serveru
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); 
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
		
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);
        
        if($result['STATUSMESSAGE'] == "$numberValue STORES CREATED" )
            session()->flash('success', "Uspješno dodano $numberValue trgovina.");
        else
            session()->flash('error', "Greška prilikom dodavanja trgovina.");

        return redirect()->route('stores.index');
    }
    
    public function show($id)
    {
        $store = request()->all();
        $store['Id_Trgovine'] = $id;
        return view('stores.show', compact('store'));
    }

    public function edit($id)
    {
        $store = request()->all();
        $store['Id_Trgovine'] = $id;
        return view('stores.edit', compact('store'));
    }

    public function update()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['EDITSTORE'] = 'true';
        $_POST['NazivTrgovine'] = request()->input('Naziv_Trgovine');
        $_POST['Id_Trgovine'] = request()->input('Id_Trgovine');
        $_POST['StanjeRacuna'] = request()->input('StanjeRacuna');

        $ch = curl_init($this->api_url . "trgovine.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		
		//TODO-Ukloniti kada se riješi problem sa certifikatom na serveru
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); 
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
		
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);
        
        if($result['STATUSMESSAGE'] == "STORE NAME AND BALANCE EDITED" )
            session()->flash('success', "Uspješno izmijenjena trgovina.");
        else
            session()->flash('error', "Greška prilikom izmjene trgovine.");

        return redirect()->route('stores.index');
    }

    public function destroy($store)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['DELETESTORE'] = 'true';
        $_POST['Id_Trgovine'] = $store;

        $ch = curl_init($this->api_url . "trgovine.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		
		//TODO-Ukloniti kada se riješi problem sa certifikatom na serveru
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); 
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
		
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        if($result['STATUSMESSAGE'] == "STORE DELETED")
            session()->flash('success', "Trgovina uspješno obrisana.");
        else
            session()->flash('error', "Greška prilikom brisanja trgovine.");
        
        return redirect()->route('stores.index');
    }
}