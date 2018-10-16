<?php
$db_host = "mysql.dds.nl";
$db_database = "dwaal";
$db_username = "dwaal";
$db_password = "arjen1";

function parse($arg, $value) {
	$tag = "\"".$arg."\":";
	$start = strpos($value, $tag);
	if (!$start) {
		return "";
	}
	$start = $start+strlen($arg)+4;
	$stop = strpos($value, "\"", $start);
	$val = substr($value, $start, $stop-$start);
	return $val;
}
function getAllMasterInvoer($db) {
	$s = $db->prepare("select * from master_invoer");
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}




$db = new PDO('mysql:host='.$db_host.';dbname='.$db_database.';charset=utf8mb4', $db_username, $db_password);
echo "Orsit verbonden.<br/>";

$invoerList = getAllMasterInvoer($db);

$reps = $db->prepare("select * from reps");
$reps->execute();
$list = $reps->fetchAll(PDO::FETCH_ASSOC);
$count = 0;

$insert = $db->prepare("insert into logs(lid, invoer, value) values (:lid, :invoer, :value)");

foreach ($list as $rep) {
	$lid = $rep["lid"];
	$status = $rep["status"];
	$logs = $rep["logs"];
	foreach($invoerList as $invoerItem) {
		$invoer = $invoerItem["id"];
		$val = parse($invoer, $logs);
		if ($val != "") {
			$insert->bindValue(":lid", $lid);
			$insert->bindValue(":invoer" , $invoer);
			$insert->bindValue(":value", $val);
			$insert->execute();
			echo $count." - ".$lid.", ".$invoer."<br/>";
			$count++;
		}
	}
}
echo "Done";


?>