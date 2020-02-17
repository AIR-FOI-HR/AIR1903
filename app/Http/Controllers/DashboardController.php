<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Timelog;
use App\User;

use Session;
use Carbon\Carbon;

class DashboardController extends Controller
{
    public function index()
    {
        return view('dashboard');
    }
    
    public function logout()
    {
        session()->flush();
        return redirect('/');
    }

}
