<?php

namespace App\Http\Controllers;


class UserController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['Readall'] = 'true';

        $ch = curl_init("http://cortex.foi.hr/pop/korisnici.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        $users = $result['DATA'];
        curl_close($ch);

        $ch = curl_init("http://cortex.foi.hr/pop/trgovine.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        $stores = $result['DATA'];
        curl_close($ch);

        unset($_POST['Readall']);
        $_POST['GETROLES'] = 'true';
        $ch = curl_init("http://cortex.foi.hr/pop/korisnici.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        $roles = $result['DATA'];
        curl_close($ch);
        
        return view('users.index', compact('users','stores','roles'));
    }


    public function store()
    {

    }


    public function show()
    {
        $user = request()->all();
        return view('users.show', compact('user'));
    }


    public function edit($id)
    {
        $user = request()->all();
        $user['Id'] = $id;
        return view('users.edit', compact('user'));
    }


    public function update()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['EDITUSER'] = 'true';
        $_POST['Id_Korisnika'] = request()->input('Id');
        $_POST['KorisnickoImeKorisnik'] = request()->input('KorisnickoIme');
        $_POST['Ime'] = request()->input('Ime');
        $_POST['Prezime'] = request()->input('Prezime');
        $_POST['Email'] = request()->input('Email');

        $ch = curl_init("http://cortex.foi.hr/pop/korisnici.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);
        
        if($result['STATUSMESSAGE'] == "USER EDITED" )
            session()->flash('success', "Uspješno izmijenjen korisnik.");
        else
            session()->flash('error', "Greška prilikom izmjene korisnika.");

        return redirect()->route('users.index');
    }


    public function updateStatus($user, $value)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['KorisnickoImeKorisnik[]'] = $user;
        $_POST['CONFIRM'] = $value;

        $ch = curl_init("http://cortex.foi.hr/pop/registracija.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        if($result['STATUSMESSAGE'] == "Account confirmed")
            session()->flash('success', "Korisnik $user uspješno aktiviran.");
        
        else if($result['STATUSMESSAGE'] == "Account unconfirmed")
            session()->flash('success', "Korisnik $user uspješno deaktiviran.");
        
        else
            session()->flash('error', "Greška prilikom aktiviranja korisnika $user.");
        
        return redirect()->route('users.index');
    }

    public function updateStatusMultiple(){
        $users = request('users');
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['CONFIRM'] = 'true';

        foreach ($users as $user)
            $_POST['KorisnickoImeKorisnik'][] = $user; 

        $_POST = http_build_query($_POST);
        $ch = curl_init("http://cortex.foi.hr/pop/registracija.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        return redirect()->route('users.index');
    }

    public function updateRole($user, $value)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['SETROLE'] = 'true';
        $_POST['KorisnickoImeKorisnik'] = $user;
        $_POST['RoleId'] = $value;


        $ch = curl_init("http://cortex.foi.hr/pop/korisnici.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        if($result['STATUSMESSAGE'] == "ROLE SET")
            session()->flash('success', "Uloga korisnika $user uspješno je promijenjena.");
        else
            session()->flash('error', "Greška prilikom promjene uloge korisnika $user.");
        
        return redirect()->route('users.index');
    }


    public function updateStore($user, $value)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['ASSIGNSTORE'] = 'true';
        $_POST['KorisnickoImeKorisnik'] = $user;
        $_POST['Id_Trgovine'] = $value;

        $ch = curl_init("http://cortex.foi.hr/pop/trgovine.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        if($result['STATUSMESSAGE'] == "STORE ASSIGNED")
            session()->flash('success', "Korisnik $user uspješno je dodijeljen trgovini.");
        else
            session()->flash('error', "Greška prilikom dodjele korisnika $user trgovini.");
        
        return redirect()->route('users.index');
    }


    public function updateMoney($user, $value)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['SET'] = 'true';
        $_POST['KorisnickoImeKorisnik[]'] = $user;
        $_POST['StanjeRacuna'] = (string)$value;

        $ch = curl_init("http://cortex.foi.hr/pop/novcanik.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        if($result['STATUSMESSAGE'] == "BALANCE SET")
            session()->flash('success', "Korisniku $user uspješno je promijenjeno novčano stanje.");
        else
            session()->flash('error', "Greška prilikom promjene novčanog stanja korisnika $user.");
        
        return redirect()->route('users.index');
    }


    public function updateMoneyMultiple()
    {
        $users = request('users');
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['SET'] = 'true';
        $_POST['StanjeRacuna'] = (string)request('value');

        foreach ($users as $user)
            $_POST['KorisnickoImeKorisnik'][] = $user; 

        $_POST = http_build_query($_POST);
        $ch = curl_init("http://cortex.foi.hr/pop/novcanik.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);
        
        return redirect()->route('users.index');
    }


    public function destroy($user)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['DELETE'] = 'true';
        $_POST['KorisnickoImeKorisnik'] = $user;

        $ch = curl_init("http://cortex.foi.hr/pop/korisnici.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        curl_close($ch);

        if($result['STATUSMESSAGE'] == "DELETED")
            session()->flash('success', "Korisnik $user uspješno obrisan.");
        else
            session()->flash('error', "Greška prilikom brisanja korisnika $user.");

        return redirect()->route('users.index');
    }
}
