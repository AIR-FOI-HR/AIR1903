<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="csrf-token" content="{{ csrf_token() }}">
    <!-- CSRF Token -->
    <meta name="csrf-token" content="{{ csrf_token() }}">

	<title>{{ env('APP_NAME') }} - Administracija</title>

	<meta name="robots" content="noindex" />
	<meta name="robots" content="nofollow" />

	<!-- Fonts -->
    <link rel="dns-prefetch" href="//fonts.gstatic.com">

    <!-- Favicon -->
    <link rel="shortcut icon" href="{{ asset('assets/images/favicon/favicon.ico') }}" type="image/x-icon">

	<!-- Styles -->
    <link rel="stylesheet" href="{{ asset('assets/css/master.css') }}">

    <!-- Animations -->
    <link rel="stylesheet" href="{{ asset('assets/css/animate/animate.css') }}">

	<!-- Font awesome icons -->
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">

	<!-- Datatable style -->
	<link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/dataTables.bootstrap4.min.css">

	<!-- Placeholder for optional css -->
	@yield('css')

	<!-- jQuery and bootstrap notify library -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
	<script src="{{ asset('assets/js/bootstrap-notify/bootstrap-notify.min.js') }}"></script>

</head>
<body>

	<div id="app" class="app">

		@include('partials.side-navigation')

		<main>
			@include('partials.top-navigation')

			@yield('content')

			@include('partials.footer')
		</main>

	</div>

	<!-- Bootstrap scripts -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	<!-- Datatable scripts -->
	<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
	<script src="https://cdn.datatables.net/1.10.19/js/dataTables.bootstrap4.min.js"></script>

	<!-- Placeholder for optional js -->
	<script>
	</script>

	@yield('js')

</body>
</html>
