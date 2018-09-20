<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";
$data->debug = "";

/*
$_POST["bid"] = "102030102030ACBEHIJ9";
$_POST["kid"] = "15";
$_POST["obj"] = "2MqtBRSI7jDYC9okH5OcVJyfPZ3Qe1";
$_POST["log"] = "5b8f0460c5432";
*/


if (!hasValue("bid", $_POST)) {
	$data->error .= "U bent niet ingelogd.\n";
}
if (!hasValue("kid", $_POST)) {
	$data->error .= "U heeft geen klant geselecteerd.\n";
}
if (!hasValue("obj", $_POST)) {
	$data->error .= "U heeft geen object geselecteerd.\n";
}
if ($data->error == "") {
	$data->html = "<table cellpadding='0' cellspacing='0' width='100%' class='' style='color:#444'>";

	$log = null; // worden we aangeroepen om een nieuwe log aan te maken?
	if (hasValue("log", $_POST)) {
		$log = getLog($db, $_POST["log"]); // of gaan we een bestaande log wijzigen?
	}
	
	// Welke statussen kan dit object hebben? Welke werkzaamheden kunnen worden uitgevoerd voor dit object?
	$data->html .= "<tr><td colspan='2'>";
	
	
	$ssbo = getStatussenByObject($db, $_POST["bid"], $_POST["obj"]);
	$status = $ssbo[0]["id"]; // we nemen de eerste status als er geen bestaande log wordt geladen.
	if ($log) { // een bestaande log, dus een bekende status.
		$status = $log["status"];
		$statusTekst = "Niet gevonden";
		foreach ($ssbo as $ll) {
			if ($ll["id"] == $status) {
				$statusTekst = $ll["omschr"];
			}
		}
		$data->html .= "<input type='hidden'  name='info_status' value='".$status."'/>";
		$data->html .= "<div class='but4'>".$statusTekst."</div>";
	} else {
		if (hasValue("status", $_POST)) {
			$status = $_POST["status"];
		}
		$data->html .= "<script>function reloadPage(arg) {
            actions.getSession().setArg('status', arg.value);
            loadPage();
			} </script>";
		$data->html .= "<div class='but1'><select onchange='reloadPage(this)' class='inv' name='info_status' style='background:#fff'>";
		foreach ($ssbo as $ll) {
			if ($status == $ll['id']) {
				$data->html .= "<option selected value='".$ll['id']."'>".$ll['omschr']."</option>";
			} else {
				$data->html .= "<option value='".$ll['id']."'>".$ll['omschr']."</option>";
			}
		}
		$data->html .= "</select></div>";
	}
	$data->html .= "</td></tr>";
	$data->html .= "<tr><td colspan='2'>";
	$data->html .= addStatusChecked($log["checked"]);
	$data->html .= "</td></tr>";
	// Op basis van de geselecteerde status ($status) gaan we de invoer bepalen, en eventueel vullen met gegeven waardes.
	
	$yy = getInvoerenByObject($db, $_POST["bid"], $_POST["obj"], $status);
	foreach ($yy as $ll) {
		$value = parse($ll['id'], $log["logs"]);
		
		$data->html .= "<tr><td valign='middle' width='110px'>".$ll['omschr']."</td>";
		$data->html .= "<td valign='middle'>";
		//$data->debug .= "adding value ".$ll['omschr']."#".substr($value,0,20)."#\n";
		switch ($ll["invtype"]) {
			case "I": 
				$data->html .= addImage($ll, $value);
			break;
			case "B": 
				$data->html .= "<div class='inv'>";
				$data->html .= addTextArea($ll, $value);
				$data->html .= "</div>";
			break;
			case "T": 
				$data->html .= "<div class='inv'>";
				$data->html .= addInput($ll, $value);
				$data->html .= "</div>";
			break;
			case "N": 
				$data->html .= "<div class='inv'>";
				$data->html .= addNumber($ll, $value);
				$data->html .= "</div>";
			break;
			case "R1": 
				$data->html .= "<div class='inv'>";
				$data->html .= addRadio($ll, $value);
				$data->html .= "</div>";
			break;
			default:
				$data->html .= "<div class='inv'>";
				if ($value != "") {
					$data->html .= "<input type='text'  name='info_".$ll["id"]."' class='butOther' value='".$value."'/>";
				} else {
					$data->html .= "<input type='text'  name='info_".$ll["id"]."' class='butOther' />";
				}
				$data->html .= "</div>";
		}
		
		$data->html .= "</td></tr>";
	}
	$data->html .= "<tr>";
	$data->html .= "<td><div class='but2' onClick='annuleren()'>annuleren</div></td><td>";
	if ($log != null) {
		$data->html .= "<div class='but2' onClick='wijzigen()'>Wijzigen</div>";
	} else {
		$data->html .= "<div class='but2' onClick='aanmaken()'>Aanmaken</div>";
	}
	$data->html .= "</td></tr></table>";
}
echo json_encode($data, JSON_UNESCAPED_SLASHES, JSON_UNESCAPED_LINE_TERMINATORS);


function addImage($ll, $value) {
	if ($value != '') {
		$out = "<div class='afb abc".$ll["id"]."' data='".$ll["id"]."' style='background:#0ea;'>"; // een image heeft een kleurtje als het geupload is.
	} else {
		$out = "<div class='afb abc".$ll["id"]."' data='".$ll["id"]."' >"; 
	}
	$out .= "<div onclick=\"actions.takePhoto('".$ll["id"]."');checkPhoto();\" style='float:left;margin-right:10px;width:40px;height:40px'><img id='img".$ll["id"]."' width='40px' height='40px' src='http://www.dwaal.dds.nl/itrs/images/camera.png'/></div>";
	$out .="<div onclick=\"actions.selectPhoto('".$ll["id"]."');checkPhoto();\" style='float:right;margin-left:10px;width:40px;height:40px'><img id='img".$ll["id"]."' width='40px' height='40px' src='http://www.dwaal.dds.nl/itrs/images/folder.png'/></div>";

	if ($value != '') {
		$out .="<span class='arial' style='font-size:10pt;color:#fff'>foto wijzigen</span><br/>";
		$out .="<input name='info_".$ll["id"]."' type='hidden' class='ic inv1 ivi' value='".$value."'/>";
	} else {
		$out .="<span class='arial' style='font-size:10pt;color:#fff'>foto bijvoegen</span><br/>";
		$out .="<input name='info_".$ll["id"]."' type='hidden' class='ic inv1 ivi' value=''/>";
	}
	$out .= "</div>";
	return $out;
}
function addTextArea($ll, $value) {
	if ($value != '') {
		return "<textarea name='info_".$ll["id"]."' class='butTextArea' style='height:100px'>".str_replace("\\r\\n", "<br/>", $value)."</textarea>";
	} else {
		return "<textarea name='info_".$ll["id"]."' class='butTextArea' style='height:100px'></textarea>";
	}
}
function addInput($ll, $value) {
	if ($value != '') {
		return "<input type='text'  name='info_".$ll["id"]."' class='butOther' value='".$value."'/>";
	} else {
		return "<input type='text'  name='info_".$ll["id"]."' class='butOther' />";
	}
}
function addNumber($ll, $value) {
	if ($value != '') {
		return "<input type='number'  name='info_".$ll["id"]."' class='butOther' value='".$value."'/>";
	} else {
		return "<input type='number'  name='info_".$ll["id"]."' class='butOther' />";
	}
}
function addRadio($ll, $value) {
	$out = "";
	if ($value != '') {
		if ($value == 'J') {
			$out .= "<input selected type='radio' value='J' name='info_".$ll["id"]."' style='width:20px;height:20px;margin:0px 5px 0px 0px;vertical-align:middle' />";
		} else {
			$out .= "<input type='radio' value='J' name='info_".$ll["id"]."' style='width:20px;height:20px;margin:0px 5px 0px 0px;vertical-align:middle' />";
		}
		$out .= "<span class='arial f10'>Goedkeuren</span>";
		if ($value == 'N') {
			$out .= "<input selected type='radio' value='N' name='info_".$ll["id"]."' style='width:20px;height:20px;margin:0px 5px 0px 20px;vertical-align:middle' />";
		} else {
			$out .= "<input type='radio' value='N' name='info_".$ll["id"]."' style='width:20px;height:20px;margin:0px 5px 0px 20px;vertical-align:middle' />";
		}
		$out .= "<span class='arial f10'>Afkeuren</span>";
	} else {
		$out .= "<input type='radio' value='J' name='info_".$ll["id"]."' style='width:20px;height:20px;margin:0px 5px 0px 0px;vertical-align:middle' />";
		$out .= "<span class='arial f10'>Goedkeuren</span>";
		$out .= "<input type='radio' value='N' name='info_".$ll["id"]."' style='width:20px;height:20px;margin:0px 5px 0px 20px;vertical-align:middle' />";
		$out .= "<span class='arial f10'>Afkeuren</span>";
	}
	return $out;
}
function addStatusChecked($value) {
	$out = "<div class='but1'>";
	if ($value != '') {
		if ($value == 1) {
			$out .= "<input checked type='radio' value='1' name='info_checked' style='width:20px;height:20px;margin:0px 5px 0px 0px;vertical-align:middle' />";
		} else {
			$out .= "<input type='radio' value='1' name='info_checked' style='width:20px;height:20px;margin:0px 5px 0px 0px;vertical-align:middle' />";
		}
		$out .= "<span class='arial f10 red'>OK</span>";
		if ($value == 2) {
			$out .= "<input checked type='radio' value='2' name='info_checked' style='width:20px;height:20px;margin:0px 5px 0px 20px;vertical-align:middle' />";
		} else {
			$out .= "<input type='radio' value='2' name='info_checked' style='width:20px;height:20px;margin:0px 5px 0px 20px;vertical-align:middle' />";
		}
		$out .= "<span class='arial f10 green'>Afgekeurd/Defect</span>";
	} else {
		$out .= "<input checked type='radio' value='1' name='info_checked' style='width:20px;height:20px;margin:0px 5px 0px 0px;vertical-align:middle' />";
		$out .= "<span class='arial f10 red'>Goedkeuren</span>";
		$out .= "<input type='radio' value='2' name='info_checked' style='width:20px;height:20px;margin:0px 5px 0px 20px;vertical-align:middle' />";
		$out .= "<span class='arial f10 green'>Afkeuren</span>";
	}
	$out .= "</div>";
	return $out;
}



function parse($arg, $value) {
	$json = json_decode($value);
	return $json->$arg;
	
	/*
	$tag = "\"".$arg."\":";
	$start = strpos($value, $tag);
	if (!$start) {
		return "";
	}
	$start = $start+strlen($arg)+4;
	$stop = strpos($value, "\"", $start);
	$val = substr($value, $start, $stop-$start);
	return $val;
	*/
	
}



?>
