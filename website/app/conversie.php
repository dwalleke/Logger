<?php
$db_host = "mysql.dds.nl";
$db_database = "dwaal";
$db_username = "dwaal";
$db_password = "arjen1";

$dbOrsit = new PDO('mysql:host='.$db_host.';dbname='.$db_database.';charset=utf8mb4', $db_username, $db_password);
echo "Orsit verbonden.<br/>";

$host = "localhost";
$login_name = "cvlog";
$password = "cvlog";
$database="cvlog";

$dbEqlog = new PDO('mysql:host='.$host.';dbname='.$database.';charset=utf8mb4', $login_name, $password);
echo "Eqlog verbonden.<br/>";

// TODO maak twee scripts: standaard tabellen en dynamische gegevens

// WEG
// abo,access,branches,cat,eigen,equipment,facturen,factuur,invoer,invoercat,invoersubcat,lastknow,opvraag,orders,sd_foutcode,sd_merken,sd_types,settings,status,
// statuscat,statussubcat,subcat,usercat,userinvoer,userstatus,user_actief,definvoer,filters,counter,

// WEBSITE
// default_actief,master_cats,

// APP
// bedrijven,klanten,master_invoer,master_status,master_type,medewerkers,qrcode,reps,users_actief,


convert($dbOrsit, $dbEqlog, "default_actief", ["id"=>"id", "master_type"=>"master_type", "master_status"=>"master_status","master_invoer"=>"master_invoer"]);
convert($dbOrsit, $dbEqlog, "master_cats", ["id"=>"id", "omschr"=>"omschr", "actief"=>"actief"]);
convert($dbOrsit, $dbEqlog, "bedrijven", ["naam"=>"naam", "adres"=>"adres", "huisnr"=>"huisnr", "postcode"=>"postcode", "plaats"=>"plaats", "tel"=>"tel", "email"=>"email", "domein"=>"domein", "bid"=>"uid", "kvk"=>"kvk"]);
convert($dbOrsit, $dbEqlog, "klanten", ["kid"=>"id", "bid"=>"bedrijf", "naam"=>"naam", "adres"=>"adres", "huisnr"=>"huisnr", "postcode"=>"postcode", "plaats"=>"plaats", "tel"=>"tel", "email"=>"email"]);
convert($dbOrsit, $dbEqlog, "master_invoer", ["id"=>"id", "omschr"=>"omschr", "invtype"=>"invtype"]);
convert($dbOrsit, $dbEqlog, "master_status", ["id"=>"id", "omschr"=>"omschr", "master_type"=>"master_type"]);
convert($dbOrsit, $dbEqlog, "master_type", ["id"=>"id", "omschr"=>"omschr"]);
//convert($dbOrsit, $dbEqlog, "medewerkers", ["bid"=>"bedrijf", "naam"=>"naam", "password"=>"password", "level"=>"level", "email"=>"email", "tel"=>"tel"]); // special password update: 9a7bea878acf5c68449a
$sP = $dbOrsit->prepare("delete from medewerkers");
$sP->execute();
$sHenk = $dbOrsit->prepare("insert into medewerkers set bid='102030102030ACBEHIJ9',naam='Henk Pietersen',level='',email='henk@penbtechniek.nl',tel='0652676117',password='9a7bea878acf5c68449a'");
$sHenk->execute();
$sArjen = $dbOrsit->prepare("insert into medewerkers set bid='102030102030ACBEHIJ9',naam='Arjen Reudink',level='A',email='a.reudink@gmail.com',tel='0621887530',password='9a7bea878acf5c68449a'");
$sArjen->execute();
$sMonique = $dbOrsit->prepare("insert into medewerkers set bid='102030102030ACBEHIJ9',naam='Monique Bosman',level='',email='mibosman75@gmail.com',tel='0624278383',password='9a7bea878acf5c68449a'");
$sMonique->execute();
$sTester = $dbOrsit->prepare("insert into medewerkers set bid='102030102030ACBEHIJ9',naam='Theo Ester',level='A',email='tester@penbtechniek.nl',tel='0612341234',password='9a7bea878acf5c68449a'");
$sTester->execute();

echo "Medewerker wachtwoord updated<br/>";
convert($dbOrsit, $dbEqlog, "qrcode", ["qr"=>"qr", "code"=>"qr", "soort"=>"soort", "merk"=>"merk", "type"=>"type", "klant"=>"klant", "serienr"=>"serienr"]); // special needed to connect klanten aan qr
$s1 = $dbOrsit->prepare("select qr from qrcode");
$s1->execute();
$r1 = $s1->fetchAll(PDO::FETCH_ASSOC);
foreach($r1 as $l1) {
	$s2 = $dbOrsit->prepare("select kid from reps where qr=:qr");
	$s2->bindValue(":qr", $l1["qr"]);
	$s2->execute();
	$r2 = $s2->fetch(PDO::FETCH_ASSOC);
	if ($r2["kid"]) {
		$s3 = $dbOrsit->prepare("update qrcode set klant=:klant where qr=:qr");
		$s3->bindValue(":klant", $r2["kid"]);
		$s3->bindValue(":qr", $l1["qr"]);
		$s3->execute();
	} else {
		$s4 = $dbOrsit->prepare("delete from qrcode where qr=:qr");
		$s4->bindValue(":qr", $l1["qr"]);
		$s4->execute();
	}
}
echo "Klanten gekoppeld aan QRCode<br/>";
convert($dbOrsit, $dbEqlog, "reps", ["lid"=>"id", "datum"=>"datum", "qr"=>"qr", "bedrijf"=>"bedrijf", "medewerker"=>"medewerker", "typex"=>"typex", "status"=>"status", "logs"=>"logs", "checked"=>"checked", "kid"=>"klant"]);
convert($dbOrsit, $dbEqlog, "users_actief", ["id"=>"id", "bedrijf"=>"bedrijf", "user_type"=>"user_type", "user_status"=>"user_status", "user_invoer"=>"user_invoer"]);

purge($dbOrsit, "bedrijven", "bid");
purge($dbOrsit, "klanten", "bid");
purge($dbOrsit, "medewerkers", "bid");
purge($dbOrsit, "users_actief", "bid");
purge($dbOrsit, "reps", "bedrijf");
$p1 = $dbOrsit->prepare("delete from qrcode where klant not in (select kid from klanten)");
$p1->execute();

//convert($dbOrsit, $dbEqlog, "", [""=>"", ""=>""]);

function purge($dbImport, $table, $col) {
	$purge = $dbImport->prepare("delete from ".$table." where not ".$col." = '102030102030ACBEHIJ9'");
	$purge->execute();
	echo "Purged ".$table."<br/>";
}

function convert($dbImport, $dbExport, $table, $fields) {
	echo $table;
	$select = $dbExport->prepare("select * from ".$table);
	$select->execute();
	$export = $select->fetchAll(PDO::FETCH_ASSOC);
	
	$truncate = $dbImport->prepare("truncate table ".$table);
	$truncate->execute();
	
	$insert = "insert into ".$table." set ";
	foreach($fields as $in => $out) {
		$insert .= $in."=:".$in.",";
	}
	$insert = substr($insert,0,strlen($insert)-1); // remove last comma
	$import = $dbImport->prepare($insert);
	foreach($export as $l) {
		foreach($fields as $in => $out) {
			$import->bindValue(":".$in, $l[$out]);
		}
		$import->execute();
		echo ".";
	}
	echo "klaar<br/>";
}




// WEBSITE
// default_actief,master_cats,

// APP
// bedrijven,klanten,master_invoer,master_status,master_type,medewerkers,qrcode,reps,users_actief,


?>