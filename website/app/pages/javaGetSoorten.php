<?php
include("appdb.php");
include("apputil.php");

$data = (object) array();
$data->error = "";
if (!hasValue("bid", $_POST)) {
	$data->error .= "U bent niet ingelogd";
}
if ($data->error == "") {
	$data->html = "<select class='inv' id='objectsoort' name='objectsoort' style='background:#fff'>";
		$yy = getTypes($db, $_POST["bid"]);
		$selected = false;
		foreach ($yy as $ll) {
			if (!$selected) {
				$data->html .= "<option selected value='".$ll['id']."'>".$ll['omschr']."</option>";
				$selected = true;
			} else {
				$data->html .= "<option value='".$ll['id']."'>".$ll['omschr']."</option>";
			}
		}
	$data->html .= "</select>";
}
echo json_encode($data);

?>