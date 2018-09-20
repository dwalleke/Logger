<?php
include("appdb.php");
include("apputil.php");

// DEBUG
/*
$_POST["email"] = "tester@penbtechniek.nl";
$_POST["password"] = "techniek";
*/


$data = (object) array();
$data->error = "";
if ($_POST) {
	$data->input = $_POST;
} else {
	$data->input = "empty";
}
if (!hasKey("email", $_POST)) {
	$data->error .= "Geen E-mailadres opgegeven.\n";
}
if (!hasKey("password", $_POST)) {
	$data->error .= "Geen wachtwoord opgegeven.\n";
}
if ($data->error == "") {
	$medewerker = findMedewerker($db, strtolower($_POST["email"]));
	$pw = substr(sha1($_POST["password"]), 0, 20);
	if ($medewerker) {
		if ($medewerker["password"] == $pw) {
			$data->bid = $medewerker["bid"];
			$data->mid = $medewerker["email"];
			$data->lev = $medewerker["level"];
			$data->email = $_POST["email"];
			$data->password = $_POST["password"];
		} else {
			$data->error = "Uw wachtwoord is onjuist.\n";
		}
	} else {
		$data->error = "Dit E-mailadres is onbekend.\n";
	}
}
echo json_encode($data);
?>