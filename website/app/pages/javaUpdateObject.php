<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";

// TODO: complete rewrite for update
if (!hasValue("bid", $_POST)) {
	$data->error .= "U moet opnieuw inloggen.\n";
}
if (!hasValue("kid", $_POST)) {
	$data->error .= "U moet een klant selecteren.\n";
}
if (!hasValue("objectsoort", $_POST)) {
	$data->error .= "Soort is verplicht.\n";
}
if (!hasValue("objectmerk", $_POST)) {
	$data->error .= "Merk is verplicht.\n";
}
if (!hasValue("objecttype", $_POST)) {
	$data->error .= "Type is verplicht.\n";
}
if (!hasValue("objectserie", $_POST)) {
	$data->error .= "Kenmerk/serienummer is verplicht.\n";
}

if ($data->error == "") {
	$bid = $_POST["bid"];
	$kid = $_POST["kid"];
	$values = array();
	$values["soort"] = $_POST["objectsoort"];
	$values["merk"] = $_POST["objectmerk"];
	$values["type"] = $_POST["objecttype"];
	$values["serienr"] = $_POST["objectserie"];
	$values["qr"] = $_POST["objectqr"];
	$values["code"] = '';
	if (hasValue("objectcode", $_POST)) {
		$r = getQRCodeByCode($db, $_POST["objectcode"]);
		if ($r) {// check if qrcode has not been used elsewhere.
			$data->error .= "QR code is al in gebruik.\n";		
		} else {
			$values["code"] = $_POST["objectcode"];
		}
	}
	updateQRCode($db, $kid, $values);
	$data->obj = $_POST["objectqr"];
}
echo json_encode($data);
?>