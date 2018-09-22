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
if (count($data->errors) != 0) {
	$bid = $_POST["bid"];
	$search = "";
	if (hasValue("searchKlant", $_POST)) {
		$search = $_POST["searchKlant"];
	}
	$r = searchKlanten($db, $bid, $search);
	$data->results->rows = $r;
}
echo json_encode($data);
?>
