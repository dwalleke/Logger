<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";

if (!hasValue("bid", $_POST)) {
	$data->error .= "U bent niet ingelogd.\n";
}
if (!hasValue("cod", $_POST)) {
	$data->error .= "De scan is niet gelukt.\n";
}
if ($data->error == "") {
	$bid = $_POST["bid"]; // we zijn iig ingelogd.
	$cod = $_POST["cod"];
	$r = getQRCodeByCode($db, $cod);
	if ($r) {
		$data->object = $r['qr'];
		if ($r["klant"]) {
			if (getKlant($db, $bid, $r["klant"])) {
				$data->klant = $r["klant"];
			}
		}
	}
}
echo json_encode($data);
?>
