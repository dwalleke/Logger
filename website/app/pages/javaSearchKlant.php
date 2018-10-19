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
	$r = searchKlant($db, $_POST["searchPostcode"], $_POST["searchHuisnummer"]);
	$data->results->rows = $r;
}
echo json_encode($data);
?>
