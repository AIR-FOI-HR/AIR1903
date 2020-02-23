@extends('layouts.master')

@section('title', 'Detalji događaja')

@section('css')
@endsection

@section('content')
	
	<div class="modal fade custom-modal" id="resource-delete-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="exampleModalLabel">Brisanje događaja</h4>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Jeste li sigurni da želite obrisati odabrani događaj ?
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
				<div class="col-lg-6 col-md-6 col-sm-12 app-header__title">
					<h3>Detalji događaja</h3>
				</div>

				<div class="col-lg-6 col-md-6 col-sm-12 app-header__add-btn">
					<a class="btn custom-button mr-2 resource-delete-btn" data-resource-id="{{ $event['Id'] }}" data-toggle="modal" data-target="#resource-delete-modal">
						<i class="far fa-trash-alt fa-fw"></i>
					</a>
					<a class="btn custom-button" href="{{ route('events.index') }}">
						<i class="fas fa-chevron-left fa-fw"></i>
					</a>
				</div>
			</div>
		</div>

		<div class="app-content resource-content">
			<div class="row resource-content-row pt-4">
				<div class="col-md-4 resource-content-row__label">
					Naziv
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $event['Naziv'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Datum stvaranja
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $event['DatumStvoren'] }}
				</div>
			</div>

			<div class="row resource-content-row pb-4">
				<div class="col-md-4 resource-content-row__label">
					Status
				</div>

				<div class="col-md-6 resource-content-row__data">
					@if($event['Aktivan'] == 1)
						Aktivan
						<span class="green-dot dot"></span>
					@else
						Neaktivan
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
	var table = $('#items');

	resourceDeleteBtn.on('click', function() {
		var resourceId = $(this).attr('data-resource-id');
		
		var url = '{{ route("events.destroy", ":resourceId") }}';
		url = url.replace(':resourceId', resourceId);
		resourceDeleteForm.attr('action', url);
	});

</script>
@endsection