<?php
session_start();
include("../db.php");
include("../util.php");

$data = (object) array();
$data->error = "";
$data->forbidden = "";
$data->html = "";
if (ready()) {		
	$bid = $_SESSION['bid'];
	$level = $_SESSION['level'];
	$r = getBedrijf($db, $bid);
	$searchMedewerker = "";
	if (in_array("searchMedewerker", array_keys($_POST))) {
		$searchMedewerker = $_POST["searchMedewerker"];
		$_SESSION["searchMedewerker"] = $searchMedewerker;
	}
	if (in_array("searchMedewerker", array_keys($_SESSION))) {
		$searchMedewerker = $_SESSION["searchMedewerker"];
	}
	$data->html .= "<div class='box1' style='background:#f0f0f0;border-top:1px solid #fff;border-bottom:1px solid #ddd'>";
	$data->html .= "	<div>";
	$data->html .= "		<span class='oswald f24 bold blue'>MEDEWERKERS</span><span style='float:right;padding-top:8px' onclick='showadd()' class='mouse fa fa-plus-circle grey f40'></span>";
	$data->html .= "	</div>";
	$data->html .= "		<input id='searchMedewerker' value='".$searchMedewerker."' type='text' placeholder='zoeken op naam, email of telefoon' style='border:1px solid #ccc;border-radius:5px;margin:10px 0px 0px 0px;padding:7px 10px;outline:none;width:400px' class='oxygen f14' />"; 
	$data->html .= "	<div class='butbox'>";
	$data->html .= "		<div onclick='resetSearch();' class='webbutton'>reset</div>";
	$data->html .= "		<div class='spacer'>&nbsp;</div>";
	$data->html .= "		<div onclick='fireSearch();' class='webbutton'>zoek</div>";
	$data->html .= "	</div>";
	$data->html .= "</div>";
	$data->html .= "<div class='box1 addnew' style='display:none;border-top:1px dotted #ccc;padding-top:10px;'>";
	$data->html .= "	<form id='formp'>";
	$data->html .= "		<input name='mid' type='hidden' id='mid' value='' />"; // ID of the medewerker
	$data->html .= "		<span class='addChange oxygen f20 lgrey bold'>XXX</span><br/>"; // add or change
	$data->html .= "		<table border='0'><tr><td>Naam</td><td>";
	$data->html .= "			<input name='naam' type='text' placeholder='naam' style='border:1px solid #ccc;border-radius:5px;margin:10px 10px 5px 0px;padding:7px 10px;outline:none;width:392px' class='oxygen f14' />"; 
	$data->html .= "		</td></tr><tr><td>Email-adres</td><td>";
	$data->html .= "			<input name='email' type='text' placeholder='email' style='border:1px solid #ccc;border-radius:5px;margin:0px 10px 5px 0px;padding:7px 10px;outline:none;width:392px' class='oxygen f14' />"; 
	$data->html .= "		</td></tr><tr><td>Telefoonnummer</td><td>";
	$data->html .= "			<input name='tel' type='text' placeholder='telefoon' style='border:1px solid #ccc;border-radius:5px;margin:0px 10px 5px 0px;padding:7px 10px;outline:none;width:392px' class='oxygen f14' /><br/>"; 
	$data->html .= "		</td></tr><tr><td>Toegangsniveau</td><td>";
	if (isAdmin()) {
		$data->html .= "		<select name='level' style='border:1px solid #ccc;border-radius:5px;margin:0px 10px 5px 0px;padding:7px 10px;outline:none;width:392px' class='oxygen f14' >";
		$data->html .= "			<option value='normaal' selected>normaal</option><option value='admin'>admin</option>";
		$data->html .= "		</select>"; 
	} else {
		$data->html .= "		<input name='level' type='text' placeholder='normaal' disabled style='border:1px solid #ccc;border-radius:5px;margin:0px 10px 5px 0px;padding:7px 10px;outline:none;width:392px' class='oxygen f14' />";
	}
	$data->html .= "		</td></tr></table><br/>";
	$data->html .= "		<div class='butbox'>";
	$data->html .= "			<div id='annubus' onclick=\"$('.addnew').slideToggle(200);\" class='webbutton'>Annuleren</div>";
	$data->html .= "			<div class='spacer'>&nbsp;</div>";
	$data->html .= "			<div id='savebus' class='webbutton'>Opslaan</div>";
	$data->html .= "		</div>";
	$data->html .= "	</form>";
	$data->html .= "</div>";
	$data->html .= "<div class='box1'>";
	$dv = date('Y-m-d H:i:s',mktime(0,0,0,date('m'),date('d'),date('Y')));
	$dt = date('Y-m-d H:i:s',mktime(23,59,59,date('m'),date('d'),date('Y')));
	if (!$searchMedewerker) {
		$data->html .= "	<span class='oxygen f20 lgrey bold'>Medewerkers</span><br/>";
	} else {
		$data->html .= "	<span class='oxygen f20 lgrey bold'>Gevonden medewerkers</span><br/>";
	}
	$data->html .= "	<table cellpadding='6' cellspacing='0' border='0' width='100%' style='margin-top:15px;'>";
	$data->html .= "	<tr>";
	$data->html .= "		<td width='190' class='f14 oxygen bold' style='background:#0af;color:#fff;border-bottom:1px solid #eee'>Naam</td>";
	$data->html .= "		<td width='140' class='f14 oxygen bold' style='background:#0af;color:#fff;border-bottom:1px solid #eee'>E-mailadres</td>";
	$data->html .= "		<td width='140' class='f14 oxygen bold' style='background:#0af;color:#fff;border-bottom:1px solid #eee'>Telefoon</td>";
	$data->html .= "		<td width='40' class='f14 oxygen bold' style='background:#0af;color:#fff;border-bottom:1px solid #eee'>Toegangsniveau</td>";
	$data->html .= "		<td width='30' class='f14 oxygen bold' style='background:#0af;color:#fff;border-bottom:1px solid #eee'></td>";
	$data->html .= "		<td width='16' class='f14 oxygen bold' style='background:#0af;color:#fff;border-bottom:1px solid #eee'></td>";
	$data->html .= "	</tr>";
	$r = searchMedewerkers($db, $bid, $searchMedewerker);
	if ($r && count($r) > 0) {
		$tots = count($r);
		foreach($r as $row) {
			$naam = $row['naam'];
			$mid = $row['email'];
			$tel= $row['tel'];
			$email = $row['email'];
			$tel = $row['tel'];
			$level = $row['level'];
			$data->html .= "<tr class='row' data='".$mid."'>";
			$data->html .= "	<td data='".$mid."' class='rowsx f14 oxygen' style='border-bottom:1px solid #eee'>".$naam."</td>";
			$data->html .= "	<td data='".$mid."' class='rowsx f14 oxygen' style='border-bottom:1px solid #eee'>".$email."</td>";		
			$data->html .= "	<td data='".$mid."' class='rowsx f14 oxygen' style='border-bottom:1px solid #eee'>".$tel."</td>";		
			$data->html .= "	<td data='".$mid."' class='f14 oxygen' style='border-bottom:1px solid #eee'>".$level."</td>";
			
			$data->html .= "	<td class='f14 oxygen' style='border-bottom:1px solid #eee'>";
			if (isAdmin()) {
				$data->html .= "		<span data='".$mid."' class='f14 fa fa-pencil'></span>";
			}
			$data->html .= "	</td>";
			$data->html .= "	<td class='f12 oxygen' style='border-bottom:1px solid #eee'>";
			if ($level != 'admin' || ($level == 'admin' && isAdmin())) {
				$data->html .= "	<span data='".$mid."' class='f14 fa fa-trash' style=''></span>";
			}
			$data->html .= "	</td>";
			$data->html .= "</tr>";										
		}
	}
	$data->html .= "	</table>";
	$data->html .= "</div>";		
	$data->html .= "<Script>";
	$data->html .= "function showadd() {";
	$data->html .= "	document.getElementById('formp').reset();";
	$data->html .= "	$('.addChange').text('Medewerker toevoegen');";
	$data->html .= "	$('input[name=email]').prop('disabled', false);";
	$data->html .= "	$('.addnew').slideDown(200);";
	$data->html .= "}";
	$data->html .= "function resetSearch() {";
	$data->html .= "	$('#searchMedewerker').val('');";
	$data->html .= "}";
	$data->html .= "function fireSearch() {";
	$data->html .= "    var s = $('#searchMedewerker').val();";
	$data->html .= "	loadPage('medewerkers','searchMedewerker='+s);";
	$data->html .= "}";
	$data->html .= "$('#searchMedewerker').on('keyup',function(e) {";
	$data->html .= "	if (e.keyCode == 13) {";
	$data->html .= "        var s = $(this).val();";
	$data->html .= "		loadPage('medewerkers','searchMedewerker='+s);";
	$data->html .= "    }";
	$data->html .= "});";
	$data->html .= "$('.fa-pencil').on('click',function(e){";
	$data->html .= "	e.preventDefault();";
	$data->html .= "	var mid = $(this).attr('data');";
	$data->html .= "	var data='mid='+mid;";
	$data->html .= "	$.ajax({";
	$data->html .= "		url:'actions/getmedewerker.php',";
	$data->html .= "		data:data,";
	$data->html .= "		type:'post',";
	$data->html .= "		success:function(d) {";
	$data->html .= "			console.log(d);";
	$data->html .= "			var d = eval('('+d+')');";
	$data->html .= "			$('#formp [name=naam]').val(d['naam']);";
	$data->html .= "			$('#formp [name=mid]').val(d['email']);";
	$data->html .= "			$('#formp [name=email]').val(d['email']);";
	$data->html .= "			$('#formp [name=tel]').val(d['tel']);";
	$data->html .= "            if (d['level'] != 'admin') {";
	$data->html .= "				$('#formp [name=level]').val('normaal');";
	$data->html .= "            } else {";
	$data->html .= "				$('#formp [name=level]').val(d['level']);";
	$data->html .= "            }";
	$data->html .= "			$('.addChange').text('Medewerker wijzigen');";
	$data->html .= "			$('input[name=email]').prop('disabled', true);";
	$data->html .= "			$('.addnew').slideDown(200);";
	$data->html .= "		},";
	$data->html .= "		error:function() {";
	$data->html .= "			alert('Kan de server niet bereiken. Probeer het later nog eens.');";
	$data->html .= "		}";
	$data->html .= "	});	";
		
	$data->html .= "});";
	$data->html .= "$('#savebus').on('click',function() {";
	$data->html .= "	var data=$('#formp').serialize();";
	$data->html .= "	$.ajax({";
	$data->html .= "		url:'actions/addmedewerker.php',";
	$data->html .= "		data:data,";
	$data->html .= "		type:'post',";
	$data->html .= "		success:function(d) {";
	$data->html .= "			console.log(d);";
	$data->html .= "			if (d=='ok') loadPage('medewerkers',''); else alert(d);";
	$data->html .= "		},";
	$data->html .= "		error:function(mess) {";
	$data->html .= "			alert(mess);";
	$data->html .= "		}";
	$data->html .= "	});";
	$data->html .= "});";
	$data->html .= "$('.fa-trash').on('click',function(e){";
	$data->html .= "	e.preventDefault();";
	$data->html .= "	var mid = $(this).attr('data');";
	$data->html .= "	var data='mid='+mid;";
	$data->html .= "	if (confirm('Weet u het zeker?')) {";
	$data->html .= "		$.ajax({";
	$data->html .= "			url:'actions/delmedewerker.php',";
	$data->html .= "			data:data,";
	$data->html .= "			type:'post',";
	$data->html .= "			success:function(d) {";
	$data->html .= "				if (d=='ok') loadPage('medewerkers',''); else alert(d);";
	$data->html .= "			},";
	$data->html .= "			error:function() {";
	$data->html .= "				alert('Kan de server niet bereiken. Probeer het later nog eens.');";
	$data->html .= "			}";
	$data->html .= "		});";
	$data->html .= "	}";
	$data->html .= "});";
	$data->html .= "</script>";
} else {
	$data->forbidden = "true";
}
	
echo json_encode($data);	
?>
