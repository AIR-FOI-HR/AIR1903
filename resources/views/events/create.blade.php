@extends('layouts.master')

@section('title', 'Dodavanje događaja')

@section('css')
@endsection

@section('content')

<div class="container col-lg-4 col-md-6">
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-6 col-sm-12 app-header__title">
					<h3>Dodavanje događaja</h3>
				</div>

				<div class="col-md-6 col-sm-12 app-header__add-btn">
					<a class="btn custom-button" href="{{ route('events.index') }}">
						<i class="fas fa-chevron-left fa-fw"></i>
					</a>
				</div>
			</div>
		</div>

		
		<div class="app-content resource-content">

			<form method="POST" action="{{ route('events.store') }}">
 
				@csrf
				<div class="row resource-content-row pb-0 pt-4">
				<h4> Novi događaj </h4>
				</div>
				
				<div class="row resource-content-row row-space">
					<div class="col-sm-12">
					  <label for="Naziv" >Naziv</label>
					<input class="edit-form-input form-control input-group-lg" type="text" name="Naziv" title="Unesite naziv događaja" placeholder="Unesite naziv događaja">
					</div>       
				</div>
				
                <div class="form-group btn-form-group text-right">
				    <button type="submit" class="btn custom-button mr-6 mb-5 mt-6" id="">
						Spremi
					</button>
				</div>
			</form>
		</div>
	</div>
@endsection

@section('js')
@endsection