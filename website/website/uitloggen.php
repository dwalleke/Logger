<?php
session_start();
include("../util.php");
removeFromSession("bid");
removeFromSession("mid");

$data = (object) array();
$data->error = "";
$data->forbidden = "";
$data->html = "<script>loadPage('dashboard', '');</script>";
echo json_encode($data);
?>
