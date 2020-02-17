<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/
Route::get('/', 'LoginController@index')->name('login');
Route::post('/', 'LoginController@authenticate')->name('authenticate');

Route::group(['middleware' => ['web', 'custom_auth']], function () {
    Route::get('dashboard', 'DashboardController@index')->name('dashboard');
    Route::get('logout', 'DashboardController@logout')->name('logout');
});