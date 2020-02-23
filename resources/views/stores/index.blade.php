@extends('layouts.master')

@section('title', 'Trgovine')

@section('css')
@endsection

@section('content')

	<div class="modal fade custom-modal" id="single-resource-delete-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
					<form action="" id="single-resource-delete-form" method="POST">
						@csrf
						@method('DELETE')
						<button type="submit" class="btn btn-danger">Obriši</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	<form action="" id="single-resource-update-form" method="POST">
		@csrf
		@method('PUT')
	</form>

	<div class="container col-lg-8 col-md-10 col-sm-12">
		
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-4 col-sm-12 app-header__title">
					<h3>Trgovine</h3>
				</div>
				
				<div class="col-md-8 col-sm-12 app-header__add-btn">
					<input type="text"  id="add-stores-sufix-input" class="form-header-data-input" placeholder="Sufiks trgovina" aria-controls="resources">
					<input type="number" min="0" max="300"  id="add-stores-number-input" class="form-header-data-input" placeholder="Broj trgovina" aria-controls="resources">
					<a id="add-stores-btn" class="custom-button">
						Dodaj trgovine
					</a>
				</div>
			</div>
		</div>
		
		@if (session()->has('error') || session()->has('success'))
		<div class="app-notifications">
			@include('partials.error')
			@include('partials.success')
		</div>
		@endif

		<div class="app-content">
				<div class="row">
					<div class="col-md-12">
						<div class="table-responsive bg-white shadow">
							<table id="resources" class="table " style="width: 100%">
								<thead>
									<tr>
										<th class="text-center">
											Naziv
										</th>
										<th class="text-center">
											Zaposlenici
										</th>
										<th class="text-center">
											Blagajna
										</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									@if(!empty($stores))
										@foreach ($stores as $store)
											<tr>
											<td class="text-center">
												{{ $store['Naziv_Trgovine'] }}
											</td>
											<td class="text-center">
												{{ $store['BrojZaposlenika'] }}
											</td>
											<td class="text-center">
												{{ number_format($store['StanjeRacuna'], 2, '.', '') }}
											</td>
											<td class="text-right">
												<a class="action-icon action-icon-primary mr-2" href="{{ route('stores.show', $store) }}">
													<i class="far fa-eye fa-fw"></i>
												</a>
												<a class="action-icon action-icon-primary mr-2 ml-2" href="{{ route('stores.edit', $store) }}">
													<i class="fas fa-pencil-alt fa-fw"></i>
												</a>
												<button class="action-icon action-icon-danger single-resource-delete-btn" data-resource-id="{{ $store['Id_Trgovine'] }}" data-toggle="modal" data-target="#single-resource-delete-modal" type="button">
													<i class="far fa-trash-alt fa-fw"></i>
												</button> 
											</td>
										</tr>
										@endforeach
									@endif
								</tbody>
							</table>
						</div>
					</div>
				</div>
		</div>
	</div>
@endsection

@section('js')
	<script type="text/javascript">

		var table = $('#resources');
		var addResourcesBtn = $('#add-stores-btn');
		var addResourcesNumberInput = $('#add-stores-number-input');
		var addResourcesSufixInput = $('#add-stores-sufix-input');
		var singleResourceDeleteBtn = $('.single-resource-delete-btn');
		var singleResourceDeleteForm = $('#single-resource-delete-form');
		var singleResourceUpdateForm = $('#single-resource-update-form');
		
		addResourcesSufixInput.keydown(function( event ) {
			if (!(/[a-zA-Z0-9-_ ]/.test(event.key)))
				event.preventDefault();
		});

		singleResourceDeleteBtn.on('click', function() {
			var resourceId = $(this).attr('data-resource-id');

            var url = '{{ route("stores.destroy", ":resourceId") }}';
			url = url.replace(':resourceId', resourceId);
			
			singleResourceDeleteForm.attr('action', url);
		});

		addResourcesBtn.on('click', function() {
			var resourcesNumber = addResourcesNumberInput.val();
			var resourcesSufix = addResourcesSufixInput.val();

			if(!resourcesNumber)
				resourcesNumber = 0;
			resourcesNumber = parseInt(resourcesNumber);

			var url = "{{ route('stores.createMultiple', ['sufixValue' => 'resourcesSufix', 'numberValue' => 'resourcesNumber']) }}";
			url = url.replace('resourcesNumber', resourcesNumber);
			url = url.replace('resourcesSufix', resourcesSufix);

			singleResourceUpdateForm.attr('action', url);
			singleResourceUpdateForm.submit();
		});

		table.DataTable({
			'aaSorting': [],
			'order': [[ 2, 'desc' ]],
		 	'columnDefs': [
				{ 
					'orderable': false, 
					'targets':  3
				},
			],
			language: {
               searchPlaceholder: "{{ __('global.search') }}",
                search: "",
                "info": "Prikazujem _START_ do _END_ od _TOTAL_ unosa",
                "lengthMenu":     "{{ __('global.showentries') }}",
                "infoEmpty":      "Prikazujem 0 do 0 od 0 unosa",
                "infoFiltered":   "(filtrirano od _MAX_ ukupnih unosa)",
                "zeroRecords":    "{{ __('global.noresult') }}",
                "paginate": {
                    "next":       "{{ __('global.next') }}",
                    "previous":   "{{ __('global.previous') }}"
                },
            }
		 });

	</script>
@endsection
