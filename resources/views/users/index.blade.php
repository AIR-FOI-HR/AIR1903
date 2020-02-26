@extends('layouts.master')

@section('title', 'Korisnici')

@section('css')
	<link rel="stylesheet" type="text/css" href="{{ asset('assets/css/bootstrap-select/bootstrap-select.min.css') }}">
@endsection

@section('content')

	<div class="modal fade custom-modal" id="single-resource-delete-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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

	<div class="container col-lg-8 col-md-11">
		
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-md-6 col-sm-12 app-header__title">
					<h3>Korisnici</h3>
				</div>	

				<div class="col-md-6 col-sm-12 app-header__add-btn">
					<input type="number" min="0.00" max="99999.00"  id="set-money-input" class="form-header-data-input" placeholder="Novčani iznos" aria-controls="resources">
					<a id="set-money-btn" class="custom-button">
						Postavi novac
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
											Aktiviran
										</th>
										<th class="text-center">
											Korisničko ime
										</th>
										<th class="text-center">
											Uloga
										</th>
										<th class="text-center">
											Trgovina
										</th>
										<th class="text-center">
											Novac
										</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									@if(!empty($users))
										@foreach ($users as $user)
											@if ($user['KorisnickoIme'] == session('korisnickoIme'))
											<tr style="background: #b7b7b76b;">
											@else
											<tr>
											@endif
											<td class="text-center">
												@if ($user['KorisnickoIme'] != session('korisnickoIme'))
													<div class="custom-control custom-checkbox">
														@if ($user['PrijavaPotvrdena'] != "0")
															<input type="checkbox" checked="checked" class="custom-control-input resource-checkboxes single-resource-activate-checkbox" id="resource_{{ $user['KorisnickoIme'] }}" value="{{ $user['KorisnickoIme'] }}">
														@else
															<input type="checkbox" class="custom-control-input resource-checkboxes single-resource-activate-checkbox" id="resource_{{ $user['KorisnickoIme'] }}" value="{{ $user['KorisnickoIme'] }}">
														@endif
															<label class="custom-control-label" for="resource_{{ $user['KorisnickoIme'] }}"></label>
													</div>
												@else
													<i class="fas fa-ban fa-lg text-center fa-fw"></i>
												@endif
											</td>
											<td class="text-center">
												{{ $user['KorisnickoIme'] }}
											</td>
											<td class="text-center">
												<select class="selectpicker show-tick single-resource-role-select" data-resource-name="{{ $user['KorisnickoIme'] }}" data-width="100%" title="Uloga">
													@foreach ($roles as $role)
														@if ($role['Id'] == $user['Id_Uloge'])
															<option value="{{ $role['Id'] }}" selected>{{ $role['Naziv'] }}</option>
														@else
															<option value="{{ $role['Id'] }}" >{{ $role['Naziv'] }}</option>
														@endif
													@endforeach
												</select>
											</td>
											<td class="text-center">
												@if (array_key_exists ('Naziv_Trgovine', $user ) && !empty($stores))
													<select class="selectpicker show-tick single-resource-store-select" data-resource-name="{{ $user['KorisnickoIme'] }}" data-width="100%" title="Trgovina">
													@foreach ($stores as $store)
														@if ($store['Id_Trgovine'] == $user['Id_Trgovine'])
															<option value="{{ $store['Id_Trgovine'] }}" selected>{{ $store['Naziv_Trgovine'] }}</option>
														@else
															<option value="{{ $store['Id_Trgovine'] }}">{{ $store['Naziv_Trgovine'] }}</option>
														@endif
													@endforeach
													</select>
												@else
														-
												@endif
											</td>
											<td class="text-center">
												@if(array_key_exists ('StanjeRacuna', $user ))
													<input type="number" class="input-money form-control single-resource-money-input" min="0.00" max="99999.00" value="{{ number_format($user['StanjeRacuna'], 2, '.', '') }}" data-resource-name="{{ $user['KorisnickoIme'] }}" />
												@elseif(array_key_exists ('StanjeRacunaTrgovine', $user ))
													<input type="number" class="input-money form-control single-resource-money-input" min="0.00" max="99999.00" value="{{ number_format($user['StanjeRacunaTrgovine'], 2, '.', '') }}" data-resource-name="{{ $user['KorisnickoIme'] }}" />
												@else
													-
												@endif
											</td>
											<td class="text-right">
												<a class="action-icon action-icon-primary mr-2" href="{{ route('users.show', $user) }}">
													<i class="far fa-eye fa-fw"></i>
												</a>
												<a class="action-icon action-icon-primary mr-2 ml-2" href="{{ route('users.edit', $user) }}">
													<i class="fas fa-pencil-alt fa-fw"></i>
												</a>
												@if ($user['KorisnickoIme'] != session('korisnickoIme'))
												<button class="action-icon action-icon-danger single-resource-delete-btn" data-resource-name="{{ $user['KorisnickoIme'] }}" data-toggle="modal" data-target="#single-resource-delete-modal" type="button">
													<i class="far fa-trash-alt fa-fw"></i>
												</button> 
												@else
												<button disabled="disabled" class="action-icon action-icon-danger single-resource-delete-btn" data-resource-name="{{ $user['KorisnickoIme'] }}" data-toggle="modal" data-target="#single-resource-delete-modal" type="button">
													<i class="fas fa-ban fa-fw"></i>
												</button> 
												@endif
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
	<script type="text/javascript" src="{{ asset('assets/js/bootstrap-select/bootstrap-select.min.js') }}"></script>
	<script type="text/javascript">

		var table = $('#resources');

		var singleResourceDeleteBtn = $('.single-resource-delete-btn');
		var singleResourceActivateCheckbox = $('.single-resource-activate-checkbox');
		var singleResourceRoleSelect = $('.single-resource-role-select');
		var singleResourceStoreSelect = $('.single-resource-store-select'); 
		var singleResourceMoneyInput = $('.single-resource-money-input');
		var resourcesMoneySetInput = $('#set-money-input');
		var resourcesMoneySetBtn = $('#set-money-btn');

		var singleResourceDeleteForm = $('#single-resource-delete-form');
		var singleResourceUpdateForm = $('#single-resource-update-form');
		
		$('.selectpicker').selectpicker('refresh');
	
		var table = $('#resources');
		table.DataTable({
			'pageLength': 500,
			'lengthMenu': [ 10, 25, 50, 100, 500 ],
			'aaSorting': [],
			'order': [[ 1, 'asc' ]],
		 	'columnDefs': [
				{ 
					'orderable': false, 
					'targets': 0 
				},{ 
					'orderable': false, 
					'targets': 4 
				},{ 
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

		singleResourceDeleteBtn.on('click', function() {
			var resourceName = $(this).attr('data-resource-name');

            var url = '{{ route("users.destroy", ":resourceName") }}';
			url = url.replace(':resourceName', resourceName);
			
            singleResourceDeleteForm.attr('action', url);
		});

		singleResourceActivateCheckbox.on('click', function() {
			var resourceName = $(this).attr('value');
			var resourceStatus = $(this).is(":checked");

			if(resourceStatus)
				var url = "{{ route('users.updateStatus', ['user' => 'resourceName', 'value' => 'true']) }}";
			else
				var url = "{{ route('users.updateStatus', ['user' => 'resourceName', 'value' => 'false']) }}";
			
			url = url.replace('resourceName', resourceName);

			singleResourceUpdateForm.attr('action', url);
			singleResourceUpdateForm.submit();
		});

		singleResourceRoleSelect.on('change', function() {
			var resourceName = $(this).attr('data-resource-name');
			var roleId = this.value;
			
			var url = "{{ route('users.updateRole', ['user' => 'resourceName', 'value' => 'roleId']) }}";
			url = url.replace('resourceName', resourceName);
			url = url.replace('roleId', roleId);
			
			singleResourceUpdateForm.attr('action', url);
			singleResourceUpdateForm.submit();
		});

		singleResourceStoreSelect.on('change', function() {
			var resourceName = $(this).attr('data-resource-name');
			var storeId = this.value;
			
			var url = "{{ route('users.updateStore', ['user' => 'resourceName', 'value' => 'storeId']) }}";
			url = url.replace('resourceName', resourceName);
			url = url.replace('storeId', storeId);

			singleResourceUpdateForm.attr('action', url);
			singleResourceUpdateForm.submit();
		});

		singleResourceMoneyInput.focusout(function() {
			var resourceName = $(this).attr('data-resource-name');
			if(!this.value)
				this.value = 0.00
			this.value = parseFloat(this.value).toFixed(2)
			var resourceMoney = this.value;
			
			var url = "{{ route('users.updateMoney', ['user' => 'resourceName', 'value' => 'resourceMoney']) }}";
			url = url.replace('resourceName', resourceName);
			url = url.replace('resourceMoney', resourceMoney);

			singleResourceUpdateForm.attr('action', url);
			singleResourceUpdateForm.submit();
		});

		resourcesMoneySetBtn.on('click', function() {
			var resourcesMoney = resourcesMoneySetInput.val();
			if(!resourcesMoney)
				resourcesMoney = 0;
			resourcesMoney = parseFloat(resourcesMoney).toFixed(2);

			var url = "{{ route('users.updateMoneyAll', ['value' => 'resourcesMoney']) }}";
			url = url.replace('resourcesMoney', resourcesMoney);

			singleResourceUpdateForm.attr('action', url);
			singleResourceUpdateForm.submit();
		});

	</script>
@endsection
