@extends('layouts.master')

@section('title', 'Detalji trgovine')

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

	<div class="container col-lg-4 col-md-6 col-sm-12">
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-6 col-sm-12 app-header__title">
					<h3>Detalji trgovine</h3>
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
			<div class="row resource-content-row pt-4">
				<div class="col-md-4 resource-content-row__label">
					Šifra
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $store['Id_Trgovine'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Naziv
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $store['Naziv_Trgovine'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Stanje blagajne
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $store['StanjeRacuna'] ?? '-' }}
				</div>
			</div>

			
			<div class="row resource-content-row pb-4">
				<div class="col-md-4 resource-content-row__label">
					Broj zaposlenika
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $store['BrojZaposlenika'] ?? '0' }}
				</div>
			</div>

			<div id="details" class="row resource-content-row">
				<a id="details-store-btn" class="custom-button">
					Zaposlenici
				</a>
			</div>
		</div>

		<div id="details-form" class="app-content">
				<div class="row">
					<div class="col-md-12">
						<div class="table-responsive bg-white shadow">
							<table id="items" class="table " style="width: 100%">
								<thead>
									<tr>
										<th class="text-center">
											Korisničko ime
										</th>
										<th class="text-center">
											Ime
										</th>
										<th class="text-center">
											Prezime
										</th>
										<th class="text-center">
											E-mail
										</th>
									</tr>
								</thead>
								<tbody>
									@if(!empty($store['Zaposlenici']))
										@foreach ($store['Zaposlenici'] as $employee)
											<tr>
												<td class="text-center">
													{{ $employee['KorisnickoIme'] }}
												</td>
												<td class="text-center">
													{{ $employee['Ime'] }}
												</td>
												<td class="text-center">
													{{ $employee['Prezime'] }}
												</td>
												<td class="text-center">
													{{ $employee['Email'] }}
												</td>
											</tr>
										@endforeach
									@else
										<td class="text-center">
											-
										</td>
										<td class="text-center">
											-
										</td>
										<td class="text-center">
											-
										</td>
										<td class="text-center">
											-
										</td>
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
	
	var formStoreDetailsBtn = $('#details-store-btn');
	var formStoreDetails = $('#details-form');
	var resourceDeleteBtn = $('.resource-delete-btn');
	var resourceDeleteForm = $('#resource-delete-form');
	var table = $('#items');

	formStoreDetails.hide();

	formStoreDetailsBtn.click(function(){
		formStoreDetails.slideToggle(600);
	});

	resourceDeleteBtn.on('click', function() {
		var resourceId = $(this).attr('data-resource-id');
		
		var url = '{{ route("stores.destroy", ":resourceId") }}';
		url = url.replace(':resourceId', resourceId);
		resourceDeleteForm.attr('action', url);
	});

	/*table.DataTable({
			'aaSorting': [],
			"order": [[ 0, "desc" ]],
		 	'columnDefs': [
				{ 
					'orderable': false, 
					'targets': 0 
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
	 });*/

</script>
@endsection