<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->errors = (object) array();
$data->results = (object) array();
$data->input = $_POST;

$data->results->bedrijf = "Niet ingelogd";
$data->results->medewerker = "Niet ingelogd";
$data->results->klant = "Geen klant geselecteerd";
$data->results->object = "Geen object geselecteerd";


$r = getMenuData($db, $_POST["bid"], $_POST["mid"], $_POST["kid"], stripslashes($_POST["obj"]));
if ($r["bedrijf"] != "") { $data->results->bedrijf = $r["bedrijf"]; }
if ($r["medewerker"] != "") { $data->results->medewerker = $r["medewerker"]; }
if ($r["klant"] != "") { $data->results->klant = $r["klant"]; }
if ($r["object"] != "") { $data->results->object = $r["object"]; }
echo json_encode($data);

?>
