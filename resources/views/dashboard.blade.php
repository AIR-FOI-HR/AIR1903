@extends('layouts.master')

@section('title', 'Kontrolna ploča')

@section('css')
	<link rel="stylesheet" type="text/css" href="{{ asset('assets/css/bootstrap-select/bootstrap-select.min.css') }}">
@endsection

@section('content')
	<div class="container-fluid">
		<div class="app-header">
			<div class="row d-flex align-items-center">
				<div class="col-lg-6 col-md-12 col-sm-12 app-header__title">
					<h4>Naslovnica</h4>
				</div>
			</div>
		</div>

		<div class="app-content">
			<div class="row">
				<div class="col-lg-6 col-md-12 mb-3">
					<div class="statistic-card bg-white rounded shadow-sm p-3 text-center">
						<div class="statistic-card__title">
							<h1 class="statistic-card__title--clock">
								{{ \Carbon\Carbon::now()->format('H:i') }}
							</h1>
						</div>
						<div class="statistic-card__day">
							<p>
							@switch(\Carbon\Carbon::now()->dayOfWeek)
							    @case(1)
							        Ponedjeljak
							        @break
							    @case(2)
							        Utorak
							        @break
							    @case(3)
							        Srijeda
							        @break
							    @case(4)
							        Četvrtak
							        @break
							    @case(5)
							        Petak
							        @break
							    @case(6)
							        Subota
							        @break
							    @case(7)
							        Nedjelja
							        @break
							@endswitch
							, {{ \Carbon\Carbon::now()->format('d.m.Y') }}
							</p>
						</div>
						<div class="statistic-card__forecast">
							<a class="weatherwidget-io" href="https://forecast7.com/hr/46d3116d34/varazdin/" data-label_1="Varaždin" data-label_2="Vrijeme" data-mode="Current" data-days="3" data-theme="original" data-basecolor="#ffffff" data-textcolor="#000000" data-mooncolor="#000000" data-cloudcolor="#000000" data-cloudfill="rgba(255, 255, 255, 0.14)" ></a>
							<script>
								!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src='https://weatherwidget.io/js/widget.min.js';fjs.parentNode.insertBefore(js,fjs);}}(document,'script','weatherwidget-io-js');
							</script>
						</div>
					</div>
				</div>

				<div class="col-lg-6 col-md-12 mb-3">
					<div class="statistic-card bg-white rounded shadow-sm p-3">
						<div class="statistic-card__title">
							<h5 class="text-muted mb-0">Trenutno stanje</h5>
						</div>

						<div class="statistic-card__data">
							<ul>
								<li>
									Potvrđeni korisnici: <span class="text-primary"></span>
								</li>
								<li>
									Nepotvrđeni korisnici: <span class="text-danger"></span>
								</li>
							</ul>
						</div>
					</div>
				</div>
				
				<div class="col-lg-12 col-md-12 mb-3">
					<div class="statistic-card bg-white rounded shadow-sm p-3">
						<div class="statistic-card-title">
							<h5 class="text-muted mb-0">Izvještaji</h5>
						</div>
						<div class="statistic-card__data">
							<ul class="list-group list-group-flush pl-0">

							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
@endsection

@section('js')
	<script type="text/javascript" src="{{ asset('assets/js/bootstrap-select/bootstrap-select.min.js') }}"></script>
	<script type="text/javascript" src="{{ asset('assets/js/bootstrap-select/bootstrap-select.min.js') }}"></script>
@endsection
