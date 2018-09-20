<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";

if (!hasValue("bid", $_POST)) {
	$data->error .= "U bent niet ingelogd. Herstart de applicatie.\n";
}
if (!hasValue("kid", $_POST)) {
	$data->error .= "U heeft geen klant gekozen. Selecteer een klant.\n";
}
if ($data->error == "") {
	$bid = $_POST["bid"];
	$kid = $_POST["kid"];
	$r = getObjecten($db, $bid, $kid);
	$alt = true;
	$data->html = "";
	$data->count = count($r);
	foreach ($r as $l) {
		$data->arg = $l['qr']; // the last one; if we have only one object we use this to go to the logs.
		$clazz = "objeven";
		if ($alt) {
			$clazz = "objodd";
		}
		$alt = !$alt;
		$data->html .= "<div class='obj ".$clazz."'>";
		$data->html .= "	<div onClick='kiesObject(\"".$l['qr']."\");'>";
		$data->html .= "		<strong>".$l['omschr']."</strong><br/>";
		$data->html .= $l['merk']." ".$l['type']."<br/>";
		$data->html .= $l['serienr']."<br/>";
		$data->html .= "	</div>";
		$data->html .= "	<div onClick='editObject(\"".$l['qr']."\");' data='".$l['qr']."' class='edit'><span class='fa fa-edit'></span></div>";
		$data->html .= "</div>";
	}
}
echo json_encode($data);
?>
