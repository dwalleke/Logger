<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->errors = (object) array();
$data->results = (object) array();
$data->input = $_POST;

if (!hasValue("bid", $_POST)) {
	$data->errors->bid = "U bent niet ingelogd.\n";
}
if (!hasValue("kid", $_POST)) {
	$data->errors->kid = "Geen klant geselecteerd.\n";
}
if (!hasValue("obj", $_POST)) {
	$data->errors->obj = "Geen object geselecteerd.\n";
}
if (count($data->errors) != 0) {
	$bid = $_POST["bid"];
	$kid = $_POST["kid"];
	$obj = $_POST["obj"];
	$f = getReps($db, $bid, $obj);
	$rows = array();
	foreach($f as $g) {
		$g["logs"] = getLogs($db, $g["lid"]);
		$rows[] = $g;
	}
	$data->results->rows = $rows;
}

echo json_encode($data);
?>
