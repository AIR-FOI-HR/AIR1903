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
    Route::resource('users', 'UserController')->except(['update']);
    Route::put('/users/{user}/update/status/{value}', 'UserController@updateStatus')->name('users.updateStatus');
    Route::put('/users/{user}/update/role/{value}', 'UserController@updateRole')->name('users.updateRole');
    Route::put('/users/{user}/update/store/{value}', 'UserController@updateStore')->name('users.updateStore');
    Route::put('/users/{user}/update/money/{value}', 'UserController@updateMoney')->name('users.updateMoney');
});
