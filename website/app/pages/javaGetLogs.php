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

function parse($arg, $value) {
	$tag = "\"".$arg."\":";
	$start = strpos($value, $tag);
	if (!$start) {
		return "";
	}
	$start = $start+strlen($arg)+4;
	$stop = strpos($value, "\"", $start);
	$val = substr($value, $start, $stop-$start);
	return $val;
}

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
	$bid = $_POST["bid"];
	$kid = $_POST["kid"];
	$obj = $_POST["obj"];
	$f = getLogsByQR($db, $obj);
	$data->html = "<div id='lli'>";
	foreach ($f as $g) {
		$lid = $g['lid'];
		$datum = date('d-m-Y H:i',strtotime($g['datum']));
		$omschr = substr($g['omschr'],0,40);
		$omschr1 = $g['omschr'];
		$status = $g['stomschr'];
		$bedrijf = $g['naam'];
		$plaats = $g['plaats'];
		$cc="";
		switch($g['checked']) {
			case "1":
				$omschr="<font color=green>Status: OK</font>";
				break;
			case "2":
				$omschr="<font color=red>Status: Afgekeurd/Defect</font>";
				break;
		}
		
		if (!$omschr) { $omschr="Geen bijzonderheden"; $omschr1="Geen bijzonderheden<br/>"; }
		$omschr1.= $omschr."<br/><table cellpadding='0' cellspacing='0' width='100%' class='' style='color:#444'>";

		$tt = getAllMasterInvoer($db);		
		foreach($tt as $tr) {
			$val = parse($tr['id'], $g['logs']);
			if ($val != "" && $tr['id']!='9') {
				$omschr1.= "<tr>";
				
				if ($tr['invtype']=="B" || $tr['invtype']=="N" || $tr["invtype"]=="T" || $tr["invtype"]=="S") {
					$omschr1.= "<td valign='top' width='150px'>".$tr['omschr']."</td>";
					$omschr1.= "<td valign='top' width='10px'>:</td>";		
					$omschr1.= "<td valign='top'>".str_replace("\\r\\n", "<br/>", $val)."</td>";
				}
				if ($tr['invtype']=="I") {
					$omschr1.= "<td valign='top' width='150px'>".$tr['omschr']."</td>";
					$omschr1.= "<td valign='top' width='10px'>:</td>";		
					$omschr1.= "<td><div class='butSmall' onClick='showPicture(this)' data='".$val."' tekst='".$tr['omschr']."'>toon foto</div></td>";
				}
				
				if ($tr['invtype']=="R1") {
					$omschr1.= "<td valign='top' width='150'>".$tr['omschr']."</td>";
					$omschr1.= "<td valign='top' width='10'>:</td>";		

					switch($val) {
						case "J":
							$lx = "Goedgekeurd";
							break;
						default:
							$lx = "Afgekeurd";
							break;
					}
					$omschr1.= "<td valign='top'>".$lx."</td>";
				}
				$omschr1.= "</tr>";
			}
		}
		$omschr1.= "<tr><td colspan='3'>";
		$bijzonderheden = str_replace("\\r\\n", "<br/>", parse('9', $g['logs']));
		$omschr1 .= "<b>Bijzonderheden</b><Br/>".$bijzonderheden."</td></tr>";
		$omschr1 .= "<tr><td><font style='font-size:10pt;color:#0ae'>".$bedrijf." (".$plaats.")</font></td><td></td>";
		$omschr1 .= "<td><div class='butSmall' onClick='editLog(this)' data='".$lid."' >log wijzigen</div></td></tr>";
		$omschr1.= "</table>";	
		
		$data->html .= "<div style='position:relative;overflow:hidden;border-top:1px solid rgba(255,255,255,0.8);border-bottom:1px solid rgba(0,0,0,0.3)'>";
			$data->html .= "<div style='position:relative;overflow:hidden;padding:5px 10px;background:#0ae'>";
				$data->html .= "<span style='float:left;font-family:oswald;font-size:12pt;color:#fff'><strong>".$status."</strong></span>";
				$data->html .= "<span style='float:right;font-family:oswald;font-size:12pt;color:#fff' onClick='editLog(\"".$lid."\");' data='".$lid."'>";
				$data->html .= "<strong>".$datum."</strong></span>";
			$data->html .= "</div>";
			$data->html .= "<div style='position:relative;overflow:hidden;padding:10px;text-align:left;background:rgba(255,255,255,0.9)'>";
				$data->html .= "<div onClick='openDetails(this)' class='los loss loss".$lid."' data='$lid' style='font-family:oswald;font-size:12pt;color:#444'>".$omschr."</div>";
				$data->html .= "<div onClick='openDetails(this)' class='los losb losb".$lid."' data='$lid' style='display:none;font-family:oswald;font-size:12pt;color:#444'>".$omschr1."</div>";
			$data->html .= "</div>";
		$data->html .= "</div>";
		if (hasValue("details", $_POST)) {
			$data->details = $_POST["details"];
		} else {
			$data->details = "";
		}
	}
	$data->html .= "</div>";
	
}
echo json_encode($data, JSON_UNESCAPED_SLASHES, JSON_UNESCAPED_LINE_TERMINATORS);
?>
