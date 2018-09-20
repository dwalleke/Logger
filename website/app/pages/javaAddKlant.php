<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";


if (!hasKey("bid", $_POST)) {
	$data->error .= "U moet opnieuw inloggen.\n";
}
if (!hasKey("naam", $_POST)) {
	$data->error .= "Naam is verplicht.\n";
}
if (!hasKey("postcode", $_POST)) {
	$data->error .= "Postcode is verplicht.\n";
}
if (!hasKey("adres", $_POST)) {
	$data->error .= "Adres is verplicht.\n";
}
if (!hasKey("huisnr", $_POST)) {
	$data->error .= "Huisnummer is verplicht.\n";
}
if (!hasKey("plaats", $_POST)) {
	$data->error .= "Plaats is verplicht.\n";
}
if ($error == "") {
	$bid = $_POST["bid"];
	$values = array();
	$values["naam"] = $_POST["naam"];
	$values["adres"] = $_POST["adres"];
	$values["huisnr"] = $_POST["huisnr"];
	$values["kamer"] = "";
	$values["postcode"] = $_POST["postcode"];
	$values["plaats"] = $_POST["plaats"];
	if (hasKey("tel", $_POST)) {
		$values["tel"] = $_POST["tel"];
	} else {
		$values["tel"] = "";
	}
	if (hasKey("email", $_POST)) {
		$values["email"] = $_POST["email"];
	} else {
		$values["email"] = "";
	}
	$data->kid = insertKlant($db, $bid, $values);
}
echo json_encode($data);
?>