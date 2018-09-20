<?php
session_start();
include("util.php");
include("db.php");
?>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ORSIT</title>
<link rel="shortcut icon" type="image/ico" href="orsit.ico" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" >
<link href="https://fonts.googleapis.com/css?family=Oswald" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Oxygen" rel="stylesheet">
<link href="itrs.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<style>
#overlay {
    position: fixed;
    display: none;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0,0,0,0.5);
    z-index: 2;
}
#loader {
	display:none;
	position:absolute;
	z-index:9999;
	top:50%;
	left:50%;
	margin-top:-24px;
	margin-left:-24px;
	overflow:hidden;
	width:32px;height:32px;
	border: 12px solid #C1E0F4; /* Light grey */
    border-top: 12px solid #fa0; /* Blue */
    border-radius: 50%;   
    animation: spin 2s linear infinite;
    -webkit-animation: spin 2s linear infinite;
}
@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
@-webkit-keyframes spin {
    0% { -webkit-transform: rotate(0deg); }
    100% { -webkit-transform: rotate(360deg); }
}
</style>
</head>
<body>
	<div class='menuContainer'>
		<div class='menuTitleLocation'>
			<div class='oswald f18 white'>Onderhoud Registratie Systeem<br/>Installatie Techniek</div>
		</div>
		<div class='menuitem' data='dashboard'>		<div class='menuIconLocation f16 white fa fa-cog' aria-hidden="true"></div>			<span class='oswald f14 white'>Dashboard</span></div>
		<div class='menuitem' data='medewerkers'>	<div class='menuIconLocation f16 white fa fa-group' aria-hidden="true"></div>		<span class='oswald f14 white'>Medewerkers</span></div>
		<div class='menuitem' data='klanten'>		<div class='menuIconLocation f16 white fa fa-address-book' aria-hidden="true"></div><span class='oswald f14 white'>Klanten</span></div>
		<div class='menuitem' data='objecten'>		<div class='menuIconLocation f16 white fa fa-clipboard' aria-hidden="true"></div>	<span class='oswald f14 white'>Objecten</span></div>
		<div class='menuitem' data='logs'>			<div class='menuIconLocation f16 white fa fa-list' aria-hidden="true"></div>		<span class='oswald f14 white'>Logs</span></div>
		<div class='menuitem' data='stickers'>		<div class='menuIconLocation f16 white fa fa-qrcode' aria-hidden="true"></div>		<span class='oswald f14 white'>QR Stickers/Kaarten</span></div>
		<div class='menuitem' data='administratie'>	<div class='menuIconLocation f16 white fa fa-user-circle' aria-hidden="true"></div>	<span class='oswald f14 white'>Administratie</span></div>
		<div class='menuitem' data='filters'>		<div class='menuIconLocation f16 white fa fa-wrench' aria-hidden="true"></div>		<span class='oswald f14 white'>Instellingen</span></div>
		<div class='menuitem' data='uitloggen'>		<div class='menuIconLocation f16 white fa fa-sign-out' aria-hidden="true"></div>	<span class='oswald f14 white'>Uitloggen</span></div>		
	</div>
	<div class='content'>
		<div class='contentmain'></div>
		<div id='loader'></div>
	</div>
	<div id="overlay">
	  <div class='logonLocation'>
		<div class='logonContainer'>
			<form id="form">
				<div class="but1">
					<input name='email' class='inv' type='text' placeholder='e-mailadres' />
				</div>
				<div class="but1">
					<input name='password' class='inv' type='password' placeholder='wachtwoord' />
				</div>
				<center>
				<div class="webbutton" style='float:none;' id="inloggen">INLOGGEN</div>
				</center>
			</form>
		</div>
	  </div>
	</div>	
<script>
jQuery.ajaxSetup({
	beforeSend: function() {	
		$('#loader').show();
	},
	complete: function(){
		$('#loader').hide();
	},
	success: function() {}
});
$(document).ready(function() { 
	$(".menuitem").on('click',function() {
		var naam = $(this).attr('data');
		loadPage(naam,'');
	});
});
//loadPage('dashboard','');
function loadPage(naam,datax) {
		$.ajax({
			url:"website/" + naam + ".php",
			type:"POST",
			data:datax +"&",
			success:function(d) {
				var ret = JSON.parse(d);
				if (ret.forbidden != "") {
					$("#overlay").css("display", "block");
				} else {
					$("#overlay").css("display", "none");
					if (ret.error != "") {
						alert(ret.error);
					} else {
						$(".contentmain").html(ret.html);
					}
				}
			},
			error:function(request, status, error) {
				alert('Pagina is momenteel niet beschikbaar: ' + naam);
			}
		});
}
$("#inloggen").on('click',function() {
	$.ajax({
		url:"actions/login.php",
		data:$("#form").serialize(),
		type:"post",
		success:function(d) {
			var ret = JSON.parse(d);
			if (ret.error == "") {
				loadPage("dashboard", "");
			} else {
				alert("U bent niet ingelogd:\n"+ret.error);
			}
		},
		error:function(response, status, error) {
			alert('Server is niet bereikbaar: ' + status);
		}
	});
});
</script>
</body>
</html>
