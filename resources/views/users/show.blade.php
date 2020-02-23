@extends('layouts.master')

@section('title', 'Detalji korisnika')

@section('css')
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

	<div class="container col-lg-4 col-md-6">
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-6 col-sm-12 app-header__title">
					<h3>Detalji korisnika</h3>
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
			<div class="row resource-content-row pt-4">
				<div class="col-md-4 resource-content-row__label">
					Korisničko ime
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['KorisnickoIme'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Ime i prezime
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['Ime'] }} {{ $user['Prezime'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Email
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['Email'] ?? '-' }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Uloga
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['Naziv'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Novac
				</div>

				<div class="col-md-6 resource-content-row__data">
					@if(array_key_exists ('StanjeRacuna', $user ))
						{{ number_format($user['StanjeRacuna'], 2, '.', '') }}
					@elseif(array_key_exists ('StanjeRacunaTrgovine', $user ))
						{{ number_format($user['StanjeRacunaTrgovine'], 2, '.', '') }}
					@else
						-
					@endif
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Trgovina
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['Naziv_Trgovine'] ?? '-'}}
				</div>
			</div>

			<div class="row resource-content-row pb-4">
				<div class="col-md-4 resource-content-row__label">
					Status
				</div>

				<div class="col-md-6 resource-content-row__data">
					@if($user['PrijavaPotvrdena'] == 1)
						Aktiviran
						<span class="green-dot dot"></span>
					@else
						Deaktiviran
						<span class="red-dot dot"></span>
					@endif
				</div>
			</div>

		</div>
	</div>
@endsection

@section('js')
<script type="text/javascript">

	var resourceDeleteBtn = $('.resource-delete-btn');
	var resourceDeleteForm = $('#resource-delete-form');

	resourceDeleteBtn.on('click', function() {
		var resourceName = $(this).attr('data-resource-name');
		
		var url = '{{ route("users.destroy", ":resourceName") }}';
		url = url.replace(':resourceName', resourceName);
		resourceDeleteForm.attr('action', url);
	});

</script>
@endsection
