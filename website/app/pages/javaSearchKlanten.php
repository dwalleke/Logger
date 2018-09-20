<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->error = "";

if (!hasValue("bid", $_POST)) {
	$data->error .= "U bent niet ingelogd.\n";
}
if ($data->error == "") {
	$bid = $_POST["bid"];
	$search = "";
	if (hasValue("search", $_POST)) {
		$search = $_POST["search"];
	}
	$r = searchKlanten($db, $bid, $search);
	$alt = true;
	$data->html = "";
	foreach ($r as $l) {
		$clazz = "klnmeven";
		if ($alt) {
			$clazz = "klnmodd";
		}
		$alt = !$alt;

		$data->html .= "<div class='klnm ".$clazz."'>";
		$data->html .= "	<div onClick='kiesKlant(\"".$l['kid']."\");'>";
		$data->html .= "		<strong>".$l['naam']."</strong><br />";
		$data->html .= $l['adres']." ".$l['huisnr']."<br />";
		$data->html .= $l['postcode']." ".$l['plaats']."<br />";
		$data->html .= "	</div>";
		$data->html .= "	<div onClick='editKlant(\"".$l['kid']."\");' class='edit'><span class='fa fa-edit'></span></div>";
		$data->html .= "</div>";
	}
}
echo json_encode($data);
?>
