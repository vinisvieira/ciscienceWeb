$(window).on('scroll', function(){

    if( $(this).scrollTop() > 120 ){
    	$("#navbar").addClass('onScroll');
    	$("body").addClass('onScroll');
    	$("#topTitle").addClass('onScroll');
    	$("#menu-lateral-primario").addClass('onScroll');
    	$("#menu-lateral-secundario").addClass('onScroll');
    }else{
		$("#navbar").removeClass('onScroll');
		$("body").removeClass('onScroll');
		$("#topTitle").removeClass('onScroll');
		$("#menu-lateral-primario").removeClass('onScroll');
		$("#menu-lateral-secundario").removeClass('onScroll');
    }

});