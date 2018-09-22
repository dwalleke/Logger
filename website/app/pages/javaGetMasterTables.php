<?php
include("appdb.php");
include("apputil.php");
$data = (object) array();
$data->errors = (object) array();
$data->results = (object) array();
$data->input = $_POST;

$data->results->type = getAllMasterType($db);
$data->results->status = getAllMasterStatus($db);
$data->results->invoer = getAllMasterInvoer($db);
$data->results->user = getAllUserActief($db, $_POST["bid"]);
echo json_encode($data);










?>
