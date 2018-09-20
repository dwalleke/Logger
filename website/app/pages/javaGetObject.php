<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";


if (!hasValue("obj", $_POST)) {
	$data->error .= "U heeft geen object gekozen. Selecteer een object.\n";
}
if ($data->error == "") {
	$r = getObject($db, $_POST["obj"]);
	$data->qr = $r['qr'];
	$data->soort = $r['soort'];
	$data->merk = $r['merk'];
	$data->type = $r['type'];
	$data->serienr = $r['serienr'];
	if ($r['code'] && $r['code'] != '') {
		$data->code = $r['code'];
	} else {
		$data->code = '';
	}
}
echo json_encode($data);
?>
