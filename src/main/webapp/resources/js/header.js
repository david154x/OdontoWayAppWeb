document.addEventListener('DOMContentLoaded', function() {

	var menu = document.getElementById('pnlMenu');
	var btnMenu = document.getElementById('btnMenu');
	var iconMenu = document.getElementById('iconMenu');
	var tituloLogo = document.getElementById('tituloLogo');
	var logo = document.getElementById('logo');
	var btnLogout = document.getElementById('btnLogout');
	
	btnLogout.style.color = 'black';

	function toggleMenu() {
		
		var isMenuHidden = menu.classList.contains('hide');
		
		if ( isMenuHidden ) {
			
			menu.classList.remove('hide');
			menu.classList.add('show');
			iconMenu.classList.remove('rotate-right');
			iconMenu.style.color = 'black';
			btnMenu.style.background = '#ADBBDA';
			logo.style.background = '#ADBBDA';
			tituloLogo.style.color = 'black';
			btnLogout.style.color = 'black';
			
		} else {
			
			menu.classList.remove('show');
			menu.classList.add('hide');
			iconMenu.classList.add('rotate-right');
			iconMenu.style.color = 'white';
			btnMenu.style.background = '#7091E6';
			logo.style.background = '#7091E6';
			tituloLogo.style.color = 'white';
			btnLogout.style.color = 'white';
						
		}
		
	}
	
	btnMenu.addEventListener('click', toggleMenu);

});