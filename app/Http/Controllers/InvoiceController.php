<?php

namespace App\Http\Controllers;

class InvoiceController extends Controller
{
    public function index()
    {
        $_POST['Token'] = session('token');
        $_POST['KorisnickoIme'] = session('korisnickoIme');
        $_POST['Readall'] = 'true';

        $ch = curl_init("http://cortex.foi.hr/pop/racuni.php");
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
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