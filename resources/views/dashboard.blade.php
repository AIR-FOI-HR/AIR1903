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
				<div class="col-lg-6 col-md-6 mb-4">
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
							    @case(0)
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

				<div class="col-lg-6 col-md-6 mb-4">
					<div class="statistic-card bg-white rounded shadow-sm p-3">
						<div class="statistic-card__title">
							<h5 class="text-muted mb-0">Trenutno stanje</h5>
						</div>

						<div class="statistic-card__data">
							<ul>
								<li>
									Događaj: <span>{{ $event }}</span>
								</li>
								<li>
									Aktivirani korisnici: <span class="text-primary">{{ $activated }}</span>
								</li>
								<li>
									Deaktivirani korisnici: <span class="text-danger">{{ $deactivated }}</span>
								</li>
								<li>
									Preostali novac: <span class="text-primary">{{ number_format($money, 2, '.', '') }}</span>
								</li>
								<li>
									Potrošeni novac: <span class="text-danger">{{ number_format($spentMoney, 2, '.', '') }}</span>
								</li>
							</ul>
						</div>
					</div>
				</div>
				
				<div id="card-report" class="col-lg-6 col-md-6 mb-0">
					<div class="statistic-card bg-white rounded shadow-sm p-3">
						<div class="statistic-card-title mb-4">
							<h5 class="text-muted mb-0">Ljestvica najuspješnijih trgovina</h5>
						</div>
						<div class="statistic-card__data col-lg-12 text-center">
							@if (!empty($stores) && count($stores) > 4)
								<ul>
									<li  class="text-left">
										<i class="fas fa-medal fa-lg mr-3" style="color: gold"></i>  {{ $stores[0]['Naziv_Trgovine'] }}  <span class="text-success">{{ $stores[0]['StanjeRacuna'] }}</span>
									</li>
									<li class="text-left">
										<i class="fas fa-medal fa-lg mr-3" style="color: silver"></i> {{ $stores[1]['Naziv_Trgovine'] }} <span class="text-success">{{ $stores[1]['StanjeRacuna'] }}</span>  
									</li>
									<li class="text-left">
										<i class="fas fa-medal fa-lg mr-3" style="color: saddlebrown"></i>  {{ $stores[2]['Naziv_Trgovine'] }}  <span class="text-success">{{ $stores[2]['StanjeRacuna'] }}</span> 
									</li>
									<li class="text-left">
										<i class="fas fa-medal fa-lg mr-3"></i> {{ $stores[3]['Naziv_Trgovine'] }} <span class="text-success">{{ $stores[3]['StanjeRacuna'] }}</span>
									</li>
									<li class="text-left">
										<i class="fas fa-medal fa-lg mr-3"></i> {{ $stores[4]['Naziv_Trgovine'] }} <span class="text-success">{{ $stores[4]['StanjeRacuna'] }}</span>
									</li>
								</ul>
							@else
								<ul>
									<li class="chart-no-data">
										Trenutno ne postoji dovoljno trgovina!
									</li>
								<ul>
							@endif
						</div>
					</div>
				</div>
				<div id="card-report" class="col-lg-6 col-md-6 mb-0">
					<div class="statistic-card bg-white rounded shadow-sm p-3">
						<div class="statistic-card-title">
							<h5 class="text-muted mb-0">Stanje trgovina</h5>
						</div>
						<div class="statistic-card__data col-lg-12 text-center">
							<ul>
								<li class="chart-no-data">
								Trenutno ne postoji dovoljno trgovina!
								</li>
							<ul>
							<div id="canvas-holder" style="display: block; height: 249px; width: 100%;">
								<canvas id="doughnut-chart"></canvas>
							</div>
						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
@endsection

@section('js')
	<script type="text/javascript" src="{{ asset('assets/js/bootstrap-select/bootstrap-select.min.js') }}"></script>
	<script type="text/javascript" src="{{ asset('assets/js/chart-js/Chart.min.js') }}"></script>
	<script type="text/javascript">
   
	var textNoData = $('.chart-no-data');
	textNoData.hide();
	
	var stores = @json($stores);
	var storesData = [];
	var storesLabels = [];

	for (store in stores) {
		storesData.push(stores[store]["StanjeRacuna"]);
		storesLabels.push(stores[store]["Naziv_Trgovine"]);
	}

	if(!storesData.length){
		textNoData.fadeToggle(3000);	
	}

	var configDoughnutChart = {
			type: 'doughnut',
			options: {
				responsive: true,
				maintainAspectRatio: false
			},
			data: {
				datasets: [{
					data: storesData,
					backgroundColor: [
						'#CE003D', '#BF360C', '#FF6F00', '#FFB300', '#FF7043', '#8BC34A', '#4CAF50', '#64DD17',	'#3F51B5', '#5D4037', '#263238',
						'#455A64', '#78909C', '#90A4AE', '#006064', '#004D40', '#4DB6AC', '#00E5FF', '#00B0FF',	'#2979FF', '#3D5AFE', '#EC407A',
						'#F06292', '#f44336', '#E91E63', '#7B1FA2',	'#1A237E', '#EA80FC', '#E040FB', '#7C4DFF',
					],
					label: 'Dataset 1'
				}],
				labels:storesLabels
			},
			options: {
				responsive: true,
				maintainAspectRatio: false,
				legend: {
					position: 'left',
					display: true,
				},
				title: {
					display: false,
					text: 'Novac trgovina'
				},
				animation: {
					animateScale: true,
					animateRotate: true
				}
			}
		};
		
		var configLineChart = {
			type: 'line',
			options: {
				responsive: true,
				maintainAspectRatio: false
			},
			data: {
				labels: [new Date("2020-3-10").toLocaleString(), new Date("2020-3-12").toLocaleString(), new Date("2020-3-15").toLocaleString(), new Date("2020-3-21").toLocaleString()],
				datasets: [{
				label: 'Novčani tijek',
				data: [{
					x: new Date("2020-2-10 13:3"),
					y: 12
					},
					{
					x: new Date("2020-2-12 13:2"),
					y: 20
					},
					{
					x: new Date("2020-2-12 13:15"),
					y: 17
					},
					{
					x: new Date("2020-2-15 14:2"),
					y: 21
					},
					{
					x: new Date("2020-2-21 14:12"),
					y: 18
					}
				],
				backgroundColor: [
					'rgba(255, 99, 132, 0.2)',
					'rgba(54, 162, 235, 0.2)',
					'rgba(255, 206, 86, 0.2)',
					'rgba(75, 192, 192, 0.2)',
					'rgba(153, 102, 255, 0.2)',
					'rgba(255, 159, 64, 0.2)'
				],
				borderColor: [
					'rgba(255,99,132,1)',
					'rgba(54, 162, 235, 1)',
					'rgba(255, 206, 86, 1)',
					'rgba(75, 192, 192, 1)',
					'rgba(153, 102, 255, 1)',
					'rgba(255, 159, 64, 1)'
				],
				borderWidth: 1
				}]
			}
			};
	
		var ctxDoughnutChart = document.getElementById('doughnut-chart').getContext('2d');
		window.myDoughnutChart = new Chart(ctxDoughnutChart, configDoughnutChart);
		//var ctxLineChart = document.getElementById("line-chart").getContext("2d");
		//window.myLineChart = new Chart(ctxLineChart, configLineChart);
</script>

@endsection
