<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";


if (!hasValue("bid", $_POST)) {
	$data->error .= "U bent niet ingelogd.\n";
}
if (!hasValue("kid", $_POST)) {
	$data->error .= "U heeft geen klant gekozen. Selecteer een klant.\n";
}
if ($data->error == "") {
	$r = getKlant($db, $_POST["bid"], $_POST["kid"]);
	$data->naam = $r['naam'];
	$data->postcode = $r['postcode'];
	$data->huisnr = $r['huisnr'];
	$data->adres = $r['adres'];
	$data->plaats = $r['plaats'];
	$data->tel = $r['tel'];
	$data->email = $r['email'];
}
echo json_encode($data);
?>
