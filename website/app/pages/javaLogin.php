<?php
include("appdb.php");
include("apputil.php");

$data = (object) array();
$data->errors = (object) array();
$data->results = (object) array();
$data->input = $_POST;
if (!hasKey("email", $_POST)) {
	$data->errors->email = "error_field_required";
}
if (!hasKey("password", $_POST)) {
	$data->errors->password = "error_field_required";
}
if (count($data->errors) != 0) {
	$medewerker = findMedewerker($db, strtolower($_POST["email"]));
	$pw = substr(sha1($_POST["password"]), 0, 20);
	if ($medewerker) {
		if ($medewerker["password"] == $pw) {
			$data->results->bid = $medewerker["bid"];
			$data->results->mid = $medewerker["email"];
			$data->results->lev = $medewerker["level"];
			$data->results->email = $_POST["email"];
			$data->results->password = $_POST["password"];
		} else {
			$data->errors->password = "error_invalid_password";
		}
	} else {
		$data->errors->email = "error_invalid_email";
	}
}
echo json_encode($data);
?>