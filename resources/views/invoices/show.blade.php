@extends('layouts.master')

@section('title', 'Detalji računa')


@section('css')
@endsection


@section('content')
	<div class="container col-lg-4 col-md-6">
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-6 col-sm-12 app-header__title">
					<h3>Detalji računa</h3>
				</div>

				<div class="col-md-6 col-sm-12 app-header__add-btn">
					<a class="btn custom-button mr-2" href="">
						<i class="far fa-trash-alt fa-fw"></i>
					</a>
					<a class="btn custom-button" href="{{ route('invoices.index') }}">
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
					{{ $invoice['Id'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Trgovina
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $invoice['Trgovina'] }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Kupac
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $invoice['Ime_Klijenta'] }} {{ $invoice['Prezime_Klijenta'] }} ({{ $invoice['KorisnickoIme'] }})
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Broj stavki
				</div>
				<div class="col-md-6 resource-content-row__data">
					@if(array_key_exists('Stavke',$invoice))
						{{ count($invoice['Stavke']) }}
					@else
						0
					@endif
				</div>
			</div>
			
			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Iznos
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ number_format($invoice['CijenaRacuna'], 2, '.', '') }}
				</div>
			</div>

			<div class="row resource-content-row">
				<div class="col-md-4 resource-content-row__label">
					Popust na cijenu
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ $invoice['IznosPopustaRacuna'] }} ({{ $invoice['PopustRacuna'] }}%)
				</div>
			</div>

			<div class="row resource-content-row  pb-4">
				<div class="col-md-4 resource-content-row__label">
					Konačni iznos
				</div>
				<div class="col-md-6 resource-content-row__data">
					{{ number_format($invoice['ZavrsnaCijena'], 2, '.', '') }}
				</div>
			</div>
			<div id="details" class="row resource-content-row">
				<a id="details-invoice-btn" class="custom-button">
					Stavke
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
											Naziv
										</th>
										<th class="text-center">
											Vrsta
										</th>
										<th class="text-center">
											Količina
										</th>
										<th class="text-center">
											Cijena
										</th>
										<th class="text-center">
											Popust
										</th>
										<th class="text-center">
											Iznos
										</th>
									</tr>
								</thead>
								<tbody>
									@if(array_key_exists('Stavke',$invoice))
										@foreach ($invoice['Stavke'] as $item)
											<tr>
												<td class="text-center">
													{{ $item['Naziv'] }}
												</td>
												<td class="text-center">
													{{ $item['ItemType'] }}
												</td>
												<td class="text-center">
													{{ $item['Kolicina'] }}
												</td>
												<td class="text-center">
													{{ number_format($item['Cijena'], 2, '.', '') }}
												</td>
												<td class="text-center">
													@if(array_key_exists ('IznosPopusta', $item))
													{{ $item['IznosPopusta'] }}%
													@else
														-
													@endif
												</td>
												<td class="text-center">
													@if(array_key_exists ('CijenaStavkeNakonPopusta', $item))
														{{ number_format($item['CijenaStavkeNakonPopusta'], 2, '.', '') }}
													@else
														{{ number_format($item['CijenaStavke'], 2, '.', '') }}
													@endif
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
	
	var formInvoiceDetailsBtn = $('#details-invoice-btn');
	var formInvoiceDetails = $('#details-form');
	var table = $('#items');
	formInvoiceDetails.hide();

	formInvoiceDetailsBtn.click(function(){
		formInvoiceDetails.slideToggle(600);
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