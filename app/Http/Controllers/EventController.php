<?php

namespace App\Http\Controllers;

class EventController extends Controller
{
	public $api_url = "https://localhost/pop/api/v1/";
	
    public function index()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['Readall'] = 'true';
        $ch = curl_init($this->api_url . "meta.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        $events = $result['DATA'];
        curl_close($ch);

        return view('events.index', compact('events'));
    }

    public function create()
    {
        return view('events.create');
    }
    
    public function store()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['CREATEEVENT'] = 'true';
        $_POST['NazivEventa'] = request()->input('Naziv');

        $ch = curl_init($this->api_url . "meta.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);
        
        if($result['STATUSMESSAGE'] == "SUCCESS" )
            session()->flash('success', "Uspješno dodan novi događaj.");
        else
            session()->flash('error', "Greška prilikom dodavanja događaja.");

        return redirect()->route('events.index');
    }

    public function show($id)
    {
        $event = request()->all();
        $event['Id'] = $id;
        return view('events.show', compact('event'));
    }

    public function edit($id)
    {
        $event = request()->all();
        $event['Id'] = $id;
        return view('events.edit', compact('event'));
    }

    public function update()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['EDITEVENT'] = 'true';
        $_POST['Naziv_Eventa'] = request()->input('Naziv');
        $_POST['Id_Eventa'] = request()->input('Id');

        $ch = curl_init($this->api_url . "meta.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);
        
        if($result['STATUSMESSAGE'] == "EVENT EDITED" )
            session()->flash('success', "Uspješno izmijenjen događaj.");
        else
            session()->flash('error', "Greška prilikom izmjene događaja.");

        return redirect()->route('events.index');
    }

    public function updateStatus($event)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['SETCURRENTEVENT'] = 'true';
        $_POST['Id_Eventa'] = $event;

        $ch = curl_init($this->api_url . "meta.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        if($result['STATUSMESSAGE'] == "SUCCESS")
            session()->flash('success', "Događaj je uspješno aktiviran.");
        else
            session()->flash('error', "Greška prilikom aktiviranja događaja");
        
        return redirect()->route('events.index');
    }

    public function destroy($event)
    {
       
    }
}