<div class="side-navigation">
	<div class="side-navigation__header desktop-header d-flex justify-content-center align-items-center">
		<span class="dashboard-title">Poduzetnički pothvat</span>
	</div>
	<div class="side-navigation__header d-none justify-content-center align-items-center mobile-header">
		<span class="dashboard-title">Poduzetnički pothvat</span>
	</div>

	<div class="side-navigation__body pt-4">
		<nav>
			<ul>
				<li class="{{ Request::is('dashboard') ? 'active' : '' }}">
					<a href="{{ route('dashboard') }}">
						<i class="fas fa-home fa-fw"></i>
						<span>Naslovnica</span>
					</a>
				</li>
				<li class="{{ Request::is('users') ? 'active' : '' }}">
					<a href="{{ route('users.index') }}">
						<i class="fas fa-users fa-fw"></i>
						<span>Korisnici</span>
					</a>
				</li>
				<li class="{{ Request::is('invoices') ? 'active' : '' }}">
					<a href="{{ route('invoices.index') }}">
						<i class="fas fa-file-invoice-dollar fa-fw"></i>
						<span>Računi</span>
					</a>
				</li>
				<li class="{{ Request::is('stores') ? 'active' : '' }}">
					<a href="{{ route('stores.index') }}">
						<i class="fas fa-store fa-fw"></i>
						<span>Trgovine</span>
					</a>
				</li>
				<li class="{{ Request::is('events') ? 'active' : '' }}">
					<a href="{{ route('events.index') }}">
						<i class="fas fa-hourglass-start fa-fw"></i>
						<span>Događaji</span>
					</a>
				</li>
			</ul>
		</nav>
	</div>
</div>
