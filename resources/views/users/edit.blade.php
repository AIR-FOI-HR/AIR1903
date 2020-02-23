@extends('layouts.master')

@section('title', 'Izmjena korisnika')

@section('css')
<link rel="stylesheet" type="text/css" href="{{ asset('assets/css/bootstrap-select/bootstrap-select.min.css') }}">
@endsection

@section('content')
	
	<div class="modal fade custom-modal" id="resource-delete-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="exampleModalLabel">Brisanje korisnika</h4>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Jeste li sigurni da želite obrisati odabranog korisnika ?
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">Zatvori</button>
					<form action="" id="resource-delete-form" method="POST">
						@csrf
						@method('DELETE')
						<button type="submit" class="btn btn-danger">Obriši</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="container col-lg-4 col-md-6 col-sm-12">
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-6 col-sm-12 app-header__title">
					<h3>Izmjena korisnika</h3>
				</div>

				<div class="col-md-6 col-sm-12 app-header__add-btn">
					@if ($user['KorisnickoIme'] != session('korisnickoIme'))
						<a class="btn custom-button mr-2 resource-delete-btn" data-resource-name="{{ $user['KorisnickoIme'] }}" data-toggle="modal" data-target="#resource-delete-modal">
							<i class="far fa-trash-alt fa-fw"></i>
						</a>
					@endif
						<a class="btn custom-button" href="{{ route('users.index') }}">
							<i class="fas fa-chevron-left fa-fw"></i>
						</a>
				</div>
			</div>
		</div>

		
		<div class="app-content resource-content">

			<form method="POST" action="{{ route('users.update', $user) }}">

				@csrf
				@method('PUT')
				<div class="row resource-content-row pb-0 pt-4">
					<h4>{{ $user['Ime'] }} {{ $user['Prezime'] }}</h4>
				</div>
				<div class="row resource-content-row row-space">
					<div class="col-sm-6">
					  <label for="Ime" >Ime</label>
					  <input class="edit-form-input form-control input-group-lg" type="text" name="Ime" title="Unesite ime" placeholder="Unesite ime korisnika" value="{{ $user['Ime'] }}">
					</div>       
					<div class="col-sm-6">
					  <label for="Prezime" >Prezime</label>
					  <input class="edit-form-input form-control input-group-lg" type="text" name="Prezime" title="Unesite prezime" placeholder="Unesite prezime korisnika" value="{{ $user['Prezime'] }}">
					</div>
				</div>
				<div class="row resource-content-row row-space">
					<div class="col-sm-6">
					  <label for="Email" >E-mail</label>
					  <input class="edit-form-input form-control input-group-lg" type="email" name="Email" title="Unesite e-mail korisnika" placeholder="Unesite e-mail korisnika" value="{{ $user['Email'] }}">
					</div>       
					<div class="col-sm-6">
					  <label for="KorisnickoIme" >Korisničko ime</label>
					  <input class="edit-form-input form-control input-group-lg" type="text" name="KorisnickoIme" title="Unesite korisničko ime" placeholder="Unesite korisničko ime" value="{{ $user['KorisnickoIme'] }}">
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
<script type="text/javascript" src="{{ asset('assets/js/bootstrap-select/bootstrap-select.min.js') }}"></script>
<script type="text/javascript">

	var resourceDeleteBtn = $('.resource-delete-btn');
	var resourceDeleteForm = $('#resource-delete-form');

	resourceDeleteBtn.on('click', function() {
		var resourceName = $(this).attr('data-resource-name');
		var url = '{{ route("stores.destroy", ":resourceName") }}';
		url = url.replace(':resourceName', resourceName);
		resourceDeleteForm.attr('action', url);
	});

</script>
@endsection