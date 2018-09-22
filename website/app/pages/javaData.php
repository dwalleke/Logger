<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->errors = (object) array();
$data->results = (object) array();
$data->input = $_POST;


if ($_POST["bid"] == "") {
	$data->results->bedrijf = "Niet ingelogd";
} else {
	$data->results->bedrijf = getBedrijf($db, $_POST["bid"])["naam"];
}
if ($_POST["bid"] == "" || $_POST["mid"] == "") {
	$data->results->medewerker = "Niet ingelogd";
} else {
	$data->results->medewerker = getMedewerker($db, $_POST["bid"], $_POST["mid"])["naam"];
}
if ($_POST["bid"] == "" || $_POST["kid"] == "") {
	$data->results->klant = "Geen klant geselecteerd";
} else {
	$data->results->klant = getKlant($db, $_POST["bid"], $_POST["kid"])["naam"];
}
if ($_POST["obj"] == "") {
	$data->results->object = "Geen object geselecteerd";
} else {
	$r = getQRCode($db, stripslashes($_POST["obj"]));
	$data->results->object = $r["merk"]." ".$r["type"];
}
echo json_encode($data);










?>
