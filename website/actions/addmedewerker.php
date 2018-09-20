<?php
session_start();
include("../util.php");
include("../db.php");
include("../mailer.php");

$data = (object) array();
$data->error = "";
if (!hasValue("bid", $_SESSION)) {
	$data->error .= "Niet ingelogd.\n";
}
if (!hasValue("naam", $_POST)) {
	$data->error .= "Naam is verplicht.\n";
}
if (!hasValue("tel", $_POST)) {
	$data->error .= "Telefoonnummer is verplicht.\n";
}
if (!hasValue("email", $_POST)) {
	$data->error .= "E-mailadres is verplicht.\n";
}
if ($data->error == "") {
	$bid = $_SESSION['bid'];
	if (!hasValue("mid", $_POST)) {
		$r = findMedewerker($db, $_POST["email"]);
		if ($r && count($r) > 0) {
			$data->error .= "Dit e-mailadres wordt al gebruikt.\n";
		} else {
			$password = generateRandomString(7);
			insertMedewerker($db, $bid, $_POST, substr(sha1($password), 0, 20));
			mailPassword($_POST["email"], $_POST["naam"], $password);
		}
	} else {
		updateMedewerker($db, $bid, $_POST);
	}
}
echo json_encode($data);








function generateRandomString($length = 15) {
    return substr(sha1(rand()), 0, $length);
}
?>