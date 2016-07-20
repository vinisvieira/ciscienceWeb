'use strict';
app.run(function($rootScope, $location, loginService){

	var paginasDoAdministrador = [];
		paginasDoAdministrador.push('/home');
		paginasDoAdministrador.push('/list-contest');
		paginasDoAdministrador.push('/list-level');
		paginasDoAdministrador.push('/list-question');
		paginasDoAdministrador.push('/list-quiz');
		paginasDoAdministrador.push('/list-student');
		

		


	$rootScope.$on('$routeChangeStart', function(){

		if( $location.path() == '/login' ){

			loginService.logout();

		}else{

			//Capturar o Perfil
			loginService.loggedUser().then(function(response) {

				if( response.profile == "administrator" ){

					if( ! ( paginasDoAdministrador.indexOf($location.path() ) != -1 ) ){
						window.location.href = "#login";
					}
				}

			}, function(response) {
				window.location.href = "#login";
			});

		}

	});

});