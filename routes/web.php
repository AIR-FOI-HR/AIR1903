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

    Route::get('invoices', 'InvoiceController@index')->name('invoices.index');
    Route::get('invoices/{value}', 'InvoiceController@show')->name('invoices.show');
   
    Route::resource('users', 'UserController')->except(['update']);
    Route::put('/{user}/update/status/{value}', 'UserController@updateStatus')->name('users.updateStatus');
    Route::put('/{user}/update/role/{value}', 'UserController@updateRole')->name('users.updateRole');
    Route::put('/{user}/update/store/{value}', 'UserController@updateStore')->name('users.updateStore');
    Route::put('/{user}/update/money/{value}', 'UserController@updateMoney')->name('users.updateMoney');
    Route::post('/users/update/money-multiple/', 'UserController@updateMoneyMultiple')->name('users.updateMoneyMultiple');
    Route::post('/users/update/status-multiple/', 'UserController@updateStatusMultiple')->name('users.updateStatusMultiple');
    Route::put('/{user}/update', 'UserController@update')->name('users.update');

    Route::resource('stores', 'StoreController');
    Route::put('/stores/create/{sufixValue}/{numberValue}', 'StoreController@createMultiple')->name('stores.createMultiple');

    Route::resource('events', 'EventController');
    Route::put('/{event}/update/status/', 'EventController@updateStatus')->name('events.updateStatus');
});