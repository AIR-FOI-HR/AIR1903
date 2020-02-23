@extends('layouts.master')

@section('title', 'Izmjena događaja')

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
						@method('PUT')
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
					<h3>Izmjena događaja</h3>
				</div>

				<div class="col-md-6 col-sm-12 app-header__add-btn">
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

			<form method="POST" action="{{ route('events.update', $event) }}">

				@csrf
				@method('PUT')
				
				<div class="row resource-content-row pb-0 pt-4">
				<h4>{{ $event['Naziv'] }}</h4>
				</div>
				
				<div class="row resource-content-row row-space">
					<div class="col-sm-12">
					  <label for="Naziv" >Naziv</label>
					<input class="edit-form-input form-control input-group-lg" type="text" name="Naziv" title="Unesite naziv događaja" placeholder="Unesite naziv događaja" value="{{ $event['Naziv'] }}">
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