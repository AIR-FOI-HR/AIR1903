@extends('layouts.master')

@section('title', 'Detalji korisnika')


@section('css')
	<link rel="stylesheet" type="text/css" href="{{ asset('assets/css/flatpickr/flatpickr.min.css') }}">
@endsection


@section('content')

	<div class="modal fade custom-modal" id="event-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="exampleModalLabel">Detalji izvještaja</h4>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<ul class="list-unstyled pl-3">
						<li>
							<strong>Status:</strong> <span id="status"></span></li>
						<li>
							<strong>Vrijeme:</strong>
							<span id="arrival-time"></span>
							-
							<span id="departure-time"></span>
						</li>
					</ul>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">Zatvori</button>
				</div>
			</div>
		</div>
	</div>

	<div class="container col-lg-4">
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-6 col-sm-12 app-header__title">
					<h3>Detalji korisnika</h3>
				</div>

				<div class="col-md-6 col-sm-12 app-header__add-btn">
				{{-- 	@if ($user->id != auth()->user()->id)
					<button class="btn custom-button custom-button-danger mr-2" data-toggle="modal" data-target="#single-resource-delete-modal">
						<i class="far fa-trash-alt"></i>
					</button>
					@endif --}}
					<a class="btn custom-button mr-2" href="{{ route('users.index') }}">
						<i class="fas fa-pencil-alt"></i>
					</a>
					<a class="btn custom-button" href="{{ route('users.index') }}">
						<i class="fas fa-chevron-left"></i>
					</a>
				</div>
			</div>
		</div>

		<div class="app-content resource-content">
			<div class="row resource-content-row">
				<div class="col-md-3 resource-content-row__label">
					Ime
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['Ime'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-3 resource-content-row__label">
					Prezime
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['Prezime'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-3 resource-content-row__label">
					Email
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['Email'] ?? '-' }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-3 resource-content-row__label">
					Uloga
				</div>

				<div class="col-md-6 resource-content-row__data">
					{{ $user['Naziv'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-3 resource-content-row__label">
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
	<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
	<script type="text/javascript" src="{{ asset('assets/js/flatpickr/flatpickr.min.js') }}"></script>
	<script>
		var dateInput = $('#date');
		var initialLocaleCode = 'bs';
		var eventModal = $('#event-modal');

		function displayEventModal(){
			eventModal.modal('show');
		}
	</script>
@endsection
