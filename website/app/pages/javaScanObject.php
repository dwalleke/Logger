<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->errors = (object) array();
$data->results = (object) array();
$data->input = $_POST;



if (hasKey("qrc", $_POST)) {
	$qr = scanObject($db, stripslashes($_POST["qrc"]));
	if ($qr) {
		$data->results->kid = $qr["kid"];
		$data->results->obj = $qr["qr"];
	} else {
		$data->errors->error = "Geen object gevonden";
	}
}
echo json_encode($data);










?>
