<?php
session_start();
include("../db.php");

$bid = $_SESSION['bid'];
$mid = $_POST["mid"];
$r = getMedewerker($db, $bid, $mid);
$out=array( "mid"=>$r['email'],"naam"=>$r['naam'],"email"=>$r['email'],"tel"=>$r['tel'],"level"=>$r['level'] );
echo json_encode($out);
?>