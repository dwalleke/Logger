<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";
$data->debug = "";

if (!hasValue("bid", $_POST)) {
	$data->error .= "U moet opnieuw inloggen.\n";
}
if (!hasValue("mid", $_POST)) {
	$data->error .= "U moet opnieuw inloggen.\n";
}
if (!hasValue("kid", $_POST)) {
	$data->error .= "U moet een klant selecteren.\n";
}
if (!hasValue("obj", $_POST)) {
	$data->error .= "U heeft geen object geselecteerd.\n";
}
if (!hasValue("info_status", $_POST)) {
	$data->error .= "U heeft geen werkzaamheden geselecteerd.\n";
}
if (!hasValue("info_checked", $_POST)) {
	$data->error .= "U heeft geen status geselecteerd.\n";
}


if ($data->error == "") {
	$bid = $_POST["bid"];
	$mid = $_POST["mid"];
	$kid = $_POST["kid"];
	$obj = $_POST["obj"];
	$status = $_POST["info_status"];
	$checked = $_POST["info_checked"];
	
	$data->html = "";
	$logs = (object) array();
	
	
	$yy = getInvoerenByObject($db, $bid, $obj, $status);
	foreach ($yy as $ll) {
		$key = "info_".$ll["id"];
		$num = $ll["id"];
		if (hasValue($key, $_POST)) {
			$logs->$num = $_POST[$key];
		} else {
			$logs->$num = "";
		}
	}
	$qrCode = getQRCode($db, $obj);
	$type = $qrCode["soort"];
	$log = insertLog($db, $bid, $mid, $kid, $obj, $type, $status, $checked, json_encode($logs, JSON_UNESCAPED_SLASHES, JSON_UNESCAPED_LINE_TERMINATORS));
	$data->log = $log;
}
echo json_encode($data, JSON_UNESCAPED_SLASHES, JSON_UNESCAPED_LINE_TERMINATORS);
?>