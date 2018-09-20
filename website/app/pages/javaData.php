<?php
include("../../db.php");
include("../../util.php");
$data = (object) array();

if (hasKey("isChangedBid", $_POST)) {
	$data->bedrijf = getBedrijfByBid($db, $_POST["bid"])["naam"];
} else {
	$data->bedrijf = $_POST["menuBedrijf"];
}
if (hasKey("isChangedMid", $_POST)) {
	$data->medewerker = getMedewerker($db, $_POST["bid"], $_POST["mid"])["naam"];
} else {
	$data->medewerker = $_POST["menuMedewerker"];
}
if (hasKey("isChangedKid", $_POST)) {
	$data->klant = getKlant($db, $_POST["bid"], $_POST["kid"])["naam"];
} else {
	$data->klant = $_POST["menuKlant"];
}
if (hasKey("isChangedObj", $_POST)) {
	$r = getQRCode($db, stripslashes($_POST["obj"]));
	$data->object = $r["merk"]." ".$r["type"];
} else {
	$data->object = $_POST["menuObject"];
}
echo json_encode($data);










?>
