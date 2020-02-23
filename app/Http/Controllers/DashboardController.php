<?php

namespace App\Http\Controllers;

class DashboardController extends Controller
{
    public function index()
    {
        $activated = 0;
        $deactivated = 0;
        $money = 0;
        $spentMoney = 0;

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

        if(!empty($users))
        {
            foreach ($users as $user){
                if($user['PrijavaPotvrdena'] == 1)
                    $activated++;
                else
                    $deactivated++;
    
                if(array_key_exists ('StanjeRacuna', $user ))
                    $money += $user['StanjeRacuna'];
            }
        }
        
        $ch = curl_init("http://cortex.foi.hr/pop/racuni.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        $invoices = $result['DATA'];
        curl_close($ch);
        
        if(!empty($invoices))
        {
            foreach ($invoices as $invoice)
                $spentMoney += $invoice['ZavrsnaCijena'];
        }
    
        $ch = curl_init("http://cortex.foi.hr/pop/trgovine.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = json_decode(curl_exec($ch), true);
        $stores = $result['DATA'];
        curl_close($ch);

        if(!empty($stores))
        {
            usort($stores, function($a, $b){
                return $b['StanjeRacuna'] - $a['StanjeRacuna'];
            });
        }
        
        return view('dashboard', compact('activated','deactivated','money','spentMoney','stores'));
    }
    


    public function logout()
    {
        session()->flush();
        return redirect('/');
    }

}
