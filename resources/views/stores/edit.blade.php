@extends('layouts.master')

@section('title', 'Izmjena trgovine')

@section('css')
@endsection

@section('content')
	
	<div class="modal fade custom-modal" id="resource-delete-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="exampleModalLabel">Brisanje trgovine</h4>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Jeste li sigurni da želite obrisati odabranu trgovinu ?
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
					<h3>Izmjena trgovine</h3>
				</div>

				<div class="col-md-6 col-sm-12 app-header__add-btn">
					<a class="btn custom-button mr-2 resource-delete-btn" data-resource-id="{{ $store['Id_Trgovine'] }}" data-toggle="modal" data-target="#resource-delete-modal">
						<i class="far fa-trash-alt fa-fw"></i>
					</a>
					<a class="btn custom-button" href="{{ route('stores.index') }}">
						<i class="fas fa-chevron-left fa-fw"></i>
					</a>
				</div>
			</div>
		</div>

		
		<div class="app-content resource-content">

			<form method="POST" action="{{ route('stores.update', $store) }}">

				@csrf
				@method('PUT')
				
				<div class="row resource-content-row pb-0 pt-4">
					<h4>{{ $store['Naziv_Trgovine'] }}</h4>
				</div>
				
				<div class="row resource-content-row row-space">
					<div class="col-sm-12">
					  <label for="Naziv_Trgovine" >Ime</label>
					  <input class="edit-form-input form-control input-group-lg" type="text" name="Naziv_Trgovine" title="Unesite naziv trgovine" placeholder="Unesite naziv trgovine" value="{{ $store['Naziv_Trgovine'] }}">
					</div>       
				</div>
				
				<div class="row resource-content-row row-space">
					<div class="col-sm-12">
					  <label for="StanjeRacuna" >Blagajna</label>
					  <input id="resource-money-input" class="edit-form-input form-control input-group-lg" type="number" min="0.00" max= "99999.00" name="StanjeRacuna" title="Unesite iznos novca trgovine" placeholder="Unesite iznos novca trgovine" value="{{ number_format($store['StanjeRacuna'], 2, '.', '') }}">
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
	var resourceMoneyInput = $('#resource-money-input');

	resourceDeleteBtn.on('click', function() {
		var resourceId = $(this).attr('data-resource-id');
		
		var url = '{{ route("stores.destroy", ":resourceId") }}';
		url = url.replace(':resourceId', resourceId);
		resourceDeleteForm.attr('action', url);
	});


	resourceMoneyInput.focusout(function() {
		if(!this.value)
			this.value = 0.00
		this.value = parseFloat(this.value).toFixed(2)
	});

</script>
@endsection