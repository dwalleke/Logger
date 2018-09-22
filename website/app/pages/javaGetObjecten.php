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
if (count($data->errors) != 0) {
	$bid = $_POST["bid"];
	$kid = $_POST["kid"];
	$r = getObjecten($db, $bid, $kid);
	$data->results->rows = $r;
}
echo json_encode($data);
?>
