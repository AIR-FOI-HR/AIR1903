<?php

namespace App\Http\Middleware;

use Closure;

class CustomAuth
{
    public function handle($request, Closure $next)
    {
        if (!empty(session('authenticated'))) {
            $request->session()->put('authenticated', time());
            return $next($request);
        }
    
        return redirect('/');
    }
}