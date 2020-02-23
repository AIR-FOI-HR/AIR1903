@extends('layouts.master')

@section('title', 'Događaji')

@section('css')
@endsection

@section('content')

	<div class="modal fade custom-modal" id="single-resource-delete-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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

	<div class="container col-lg-6 col-md-8">
		
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-4 col-sm-12 app-header__title">
					<h3>Događaji</h3>
				</div>
				<div class="col-md-8 col-sm-12 app-header__add-btn">
					<a id="add-events-btn" class="custom-button" href=" {{route('events.create')}} ">
						Novi događaj
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
											Aktivan
										</th>
										<th class="text-center">
											Naziv
										</th>
										<th class="text-center">
											Datum
										</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									@if(!empty($events))
										@foreach ($events as $event)
											<tr>
												<td class="text-center">
													<div class="custom-control custom-checkbox">
														@if ($event['Aktivan'] != "0")
															<input id="resource_{{ $event['Id'] }}" type="checkbox" checked="checked" class="custom-control-input resource-checkboxes single-resource-activate-checkbox" data-resource-id="{{ $event['Id'] }}" name="resource_{{ $event['Id'] }}">
														@else
															<input id="resource_{{ $event['Id'] }}" type="checkbox" class="custom-control-input resource-checkboxes single-resource-activate-checkbox" data-resource-id="{{ $event['Id'] }}" name="resource_{{ $event['Id'] }}">
														@endif
															<label class="custom-control-label" for="resource_{{ $event['Id'] }}"></label>
													</div>
												</td>
												<td class="text-center">
													{{ $event['Naziv'] }}
												</td>
												<td class="text-center">
													{{ $event['DatumStvoren'] }}
												</td>
												<td class="text-right">
													<a class="action-icon action-icon-primary mr-2" href="{{ route('events.show', $event) }}">
														<i class="far fa-eye fa-fw"></i>
													</a>
													<a class="action-icon action-icon-primary mr-2 ml-2" href="{{ route('events.edit', $event) }}">
														<i class="fas fa-pencil-alt fa-fw"></i>
													</a>
													<button disabled="disabled" class="action-icon action-icon-danger single-resource-delete-btn" data-toggle="modal" data-target="#single-resource-delete-modal" type="button">
														<i class="fas fa-ban fa-fw"></i>
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
		var singleResourceUpdateForm = $('#single-resource-update-form');
		var singleResourceDeleteBtn = $('.single-resource-delete-btn');
		var singleResourceDeleteForm = $('#single-resource-delete-form');
		var singleResourceActivateCheckbox = $('.single-resource-activate-checkbox');
		
		singleResourceDeleteBtn.on('click', function() {
			var resourceId = $(this).attr('data-resource-id');
            var url = '{{ route("stores.destroy", ":resourceId") }}';
			url = url.replace(':resourceId', resourceId);
			
			singleResourceDeleteForm.attr('action', url);
		});

		singleResourceActivateCheckbox.on('click', function() {
			var resourceId = $(this).attr('data-resource-id');
			console.log(resourceId);
			var url = "{{ route('events.updateStatus', ['event' => 'resourceId']) }}";
			url = url.replace('resourceId', resourceId);

			singleResourceUpdateForm.attr('action', url);
			singleResourceUpdateForm.submit();
		});

		table.DataTable({
			'aaSorting': [],
			'order': [[ 1, 'desc' ]],
		 	'columnDefs': [
				{ 
					'orderable': false, 
					'targets':  0
				},
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
