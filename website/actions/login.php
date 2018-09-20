<?php
session_start();
include("../db.php");
include("../util.php");

$data = (object) array();
$data->error = "";
if (!hasValue("email", $_POST)) {
	$data->error .= "Geen E-mailadres opgegeven.\n";
}
if (!hasValue("password", $_POST)) {
	$data->error .= "Geen wachtwoord opgegeven.\n";
}
if ($data->error == "") {
	$medewerker = findMedewerker($db, strtolower($_POST["email"]));
	$pw = substr(sha1($_POST["password"]), 0, 20);
	if ($medewerker) {
		if ($medewerker["password"] == $pw) {
			$_SESSION["bid"] = $medewerker["bid"];
			$_SESSION["mid"] = $medewerker["email"];
			$_SESSION["level"] = $medewerker["level"];
		} else {
			$data->error = "Uw wachtwoord is onjuist.\n";
		}
	} else {
		$data->error = "Dit E-mailadres is onbekend.\n";
	}
}
echo json_encode($data);
?>