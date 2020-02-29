@extends('layouts.master')

@section('title', 'Računi')

@section('css')
@endsection

@section('content')

	<div class="modal fade custom-modal" id="single-resource-delete-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="exampleModalLabel">Brisanje računa</h4>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Jeste li sigurni da želite obrisati odabrani račun ?
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
	
	<div class="container col-lg-6 col-md-10">
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-6 col-sm-12 app-header__title">
					<h3>Računi</h3>
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
											Šifra
										</th>
										<th class="text-center">
											Trgovina
										</th>
										<th class="text-center">
											Kupac
										</th>
										<th class="text-center">
											Datum izdavanja
										</th>
										<th class="text-center">
											Stavke
										</th>
										<th class="text-center">
											Konačni iznos
										</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									@if(!empty($invoices))
										@foreach ($invoices as $invoice)
											<tr>
											<td class="text-center">
												{{ $invoice['Id'] }}
											</td>
											<td class="text-center">
												{{ $invoice['Trgovina'] }}
											</td>
											<td class="text-center">
												{{ $invoice['KorisnickoIme'] }}
											</td>
											<td class="text-center">
												{{ $invoice['DatumIzdavanja'] }}
											</td>
											<td class="text-center">
												{{ count($invoice['Stavke']) ?? '0' }}
											</td>
											<td class="text-center">
												{{ number_format($invoice['ZavrsnaCijena'], 2, '.', '') }}
											</td>
											<td class="text-right">
												<a class="action-icon action-icon-primary mr-2" href="{{ route('invoices.show', $invoice) }}">
													<i class="far fa-eye fa-fw"></i>
												</a>
												<button disabled="disabled" class="action-icon action-icon-danger single-resource-delete-btn" data-toggle="modal" data-target="#single-resource-delete-modal" type="button">
													<i class="fas fa-ban fa-fw"></i>
												</button> 
												<button disabled="disabled" class="action-icon action-icon-danger single-resource-delete-btn" data-toggle="modal" data-target="#single-resource-delete-modal" type="button">
													<i class="fas fa-ban fa-fw"></i>
												</button> 
											<!--	<a class="action-icon action-icon-primary mr-2 ml-2" href="{{ route('users.edit', $invoice) }}">
													<i class="fas fa-pencil-alt fa-fw"></i>
												</a>
												<button class="action-icon action-icon-danger single-resource-delete-btn" data-resource-name="{{ $invoice['KorisnickoIme'] }}" data-toggle="modal" data-target="#single-resource-delete-modal" type="button">
													<i class="far fa-trash-alt fa-fw"></i>
												</button> -->
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

		table.DataTable({
			'pageLength': 500,
			'lengthMenu': [ 10, 25, 50, 100, 500, 1000 ],
		 	'aaSorting': [],
		 	'columnDefs': [
				{ 
					'orderable': false, 
					'targets': 5 
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
