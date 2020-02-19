<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\File;

use App\User;
use App\UserRole;
use App\UserStatus;
use App\Permission;
use App\Position;
use App\Department;
use App\Timelog;

use Hash;
use Session;
use Carbon\Carbon;

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

        var_dump($users);

        return view('users.index', compact('users','stores','roles'));
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {

    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show(Request $request)
    {
        $user = $request->all();
        return view('users.show', compact('user'));
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit(User $user)
    {
       
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */

    public function updateStatus($user, $value)
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['KorisnickoImeKorisnik'] = $user;
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
        $_POST['KorisnickoImeKorisnik'] = $user;
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

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($user)
    {
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['Token'] = session('token');
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
