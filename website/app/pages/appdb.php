<?php
$db_host = "mysql.dds.nl"; // prod: mysql.dds.nl
$db_database = "dwaal";
$db_username = "dwaal";
$db_password = "arjen1";

$db = new PDO('mysql:host='.$db_host.';dbname='.$db_database.';charset=utf8mb4', $db_username, $db_password);

// javaData
function getBedrijf($db, $bid) {
	$stmt = $db->prepare("select * from bedrijven where bid=:bid");
	$stmt->bindValue(":bid", $bid);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
// javaData
function getMedewerker($db, $bid, $mid) {
	$stmt = $db->prepare("select * from medewerkers where email=:mid and bid=:bid");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":mid", $mid);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
// javaCheckObject, javaData
function getKlant($db, $bid, $kid) {
	$stmt = $db->prepare("select * from klanten where kid=:kid and bid=:bid");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":kid", $kid);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}

// javaLogin
function findMedewerker($db, $email) {
	$stmt = $db->prepare("select * from medewerkers where email=:email");
	$stmt->bindValue(":email", $email);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
// javaSearchKlanten
function searchKlanten($db, $bid, $search) {
	$select = "select * from klanten where bid=:bid ";
	if ($search && $search != "") {
		$select.="and (".
			"naam like :search1 ".
			"or postcode like :search2 ".
			"or plaats like :search3".
			") ";
	}
	$select.="order by naam";
	$stmt = $db->prepare($select);
	$stmt->bindValue(":bid", $bid);
	if ($search && $search != "") {
		$s = "%".$search."%";
		$stmt->bindValue(":search1", $s);
		$stmt->bindValue(":search2", $s);
		$stmt->bindValue(":search3", $s);
	}
	$stmt->execute();
	return $stmt->fetchAll(PDO::FETCH_ASSOC);
}
// javaAddKlant
function insertKlant($db, $bid, $values) {
	$stmt = $db->prepare("insert into klanten set kid=:kid, bid=:bid,naam=:naam,adres=:adres,huisnr=:huisnr,postcode=:postcode,plaats=:plaats,tel=:tel,email=:email");
	$stmt->bindValue(":bid", $bid);
	$id = uniqid();
	$stmt->bindValue(":kid", $id);
	$stmt->bindValue(":naam", $values["naam"]);
	$stmt->bindValue(":adres", $values["adres"]);
	$stmt->bindValue(":huisnr", $values["huisnr"]);
	$stmt->bindValue(":postcode", $values["postcode"]);
	$stmt->bindValue(":plaats", $values["plaats"]);
	$stmt->bindValue(":tel", $values["tel"]);
	$stmt->bindValue(":email", $values["email"]);
	$stmt->execute();
	return $id;
}
// javaAddKlant
function updateKlant($db, $bid, $values) {
	$stmt = $db->prepare("insert into klanten set naam=:naam,adres=:adres,huisnr=:huisnr,postcode=:postcode,plaats=:plaats,tel=:tel,email=:email where klant=:kid");
	$stmt->bindValue(":kid", $kid);
	$stmt->bindValue(":naam", $values["naam"]);
	$stmt->bindValue(":adres", $values["adres"]);
	$stmt->bindValue(":huisnr", $values["huisnr"]);
	$stmt->bindValue(":postcode", $values["postcode"]);
	$stmt->bindValue(":plaats", $values["plaats"]);
	$stmt->bindValue(":tel", $values["tel"]);
	$stmt->bindValue(":email", $values["email"]);
	$stmt->execute();
}

// javaGetObjecten
function getObjecten($db, $bid, $kid) {
	$select = "select q.qr,q.merk,q.type,q.klant,q.serienr,m.omschr,k.naam,";
	$select .= "(select count(*) from reps r where r.qr = q.qr) as treps ";
    $select .= "from qrcode q ";
	$select .= "inner join master_type m on q.soort = m.id ";
    $select .= "inner join klanten k on k.kid = q.klant ";
    $select .= "where k.bid = :bid and q.klant = :kid ";
	$stmt = $db->prepare($select);
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":kid", $kid);
	$stmt->execute();
	return $stmt->fetchAll(PDO::FETCH_ASSOC);
}
// javaGetObject
function getObject($db, $obj) {
	$select = "select * from qrcode q where q.qr = :qr";
	$stmt = $db->prepare($select);
	$stmt->bindValue(":qr", $obj);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
// javaGetInvoeren
function getLog($db, $lid) {
	$select = "select * from reps where lid = :lid";
	$stmt = $db->prepare($select);
	$stmt->bindValue(":lid", $lid);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
// javaData, javaAddLog
function getQRCode($db, $qr) {
	$s = $db->prepare("select * from qrcode where qr=:qr");
	$s->bindValue(":qr", $qr);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}
// javaCheckObject
function getQRCodeByCode($db, $qr) {
	$s = $db->prepare("select * from qrcode where code=:qr");
	$s->bindValue(":qr", $qr);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}
// javaGetLogs
function getLogsByQr($db, $qr) {
	$select = "select r.*,b.naam, b.plaats,";
	$select .= "m.omschr as stomschr ";
	$select .= "from reps r inner join master_status m on m.id  = r.status ";
	$select .= "left  join bedrijven b     on b.bid = r.bedrijf where r.qr=:qr order by r.datum desc";
	$s = $db->prepare($select);
	$s->bindValue(":qr", $qr);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
// javaGetLogs
function getAllMasterInvoer($db) {
	$s = $db->prepare("select * from master_invoer order by id");
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
// javaAddObject
function insertQRCode($db, $kid, $values) {
	$s = $db->prepare("insert into qrcode set qr=:qr,code=:code,klant=:klant,soort=:soort,merk=:merk,type=:type,serienr=:serienr");
	$uid = uniqid();
	$s->bindValue(":qr", $uid);
	$s->bindValue(":code", $values["code"]);
	$s->bindValue(":klant", $kid);
	$s->bindValue(":soort", $values["soort"]);
	$s->bindValue(":merk", $values["merk"]);
	$s->bindValue(":type", $values["type"]);
	$s->bindValue(":serienr", $values["serienr"]);
	$s->execute();
	return $uid;
}
// javaAddObject
function updateQRCode($db, $kid, $values) {
	$s = $db->prepare("update qrcode set code=:code,soort=:soort,merk=:merk,type=:type,serienr=:serienr where qr=:qr");
	$s->bindValue(":qr", $values["qr"]);

	$s->bindValue(":code", $values["code"]);
	$s->bindValue(":soort", $values["soort"]);
	$s->bindValue(":merk", $values["merk"]);
	$s->bindValue(":type", $values["type"]);
	$s->bindValue(":serienr", $values["serienr"]);
	$s->execute();
}
// javaGetSoorten
function getTypes($db, $bid) {
	$s = $db->prepare("select * from master_type mt where id in (select user_type from users_actief where bedrijf = :bid)");
	$s->bindValue(":bid", $bid);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
	
// javaGetStatussen
function getStatussenByObject($db, $bid, $qr) {
	$select = "select * from master_status where id in (";
	$select .= "select distinct u.user_status from users_actief u ";
	$select .= "inner join qrcode q on q.soort = u.user_type ";
	$select .= "inner join klanten k on k.kid = q.klant ";
	$select .= "where q.qr = :qr and k.bid = :bid";
	$select .= ") order by omschr";
	$s = $db->prepare($select);
	$s->bindValue(":bid", $bid);
	$s->bindValue(":qr", $qr);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
// javaGetInvoeren, javaAddLog
function getInvoerenByObject($db, $bid, $qr, $status) {
	$select = "select * from master_invoer where id in (";
	$select .= "select distinct u.user_invoer from users_actief u ";
	$select .= "inner join qrcode q on q.soort = u.user_type ";
	$select .= "inner join klanten k on k.kid = q.klant ";
	$select .= "where q.qr = :qr and k.bid = :bid and u.user_status = :status";
	$select .= ") order by id";
	$s = $db->prepare($select);
	$s->bindValue(":bid", $bid);
	$s->bindValue(":qr", $qr);
	$s->bindValue(":status", $status);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
// javaAddLog
function insertLog($db, $bid, $mid, $kid, $obj, $type, $status, $checked, $logs) {
	$i = "insert into reps set lid=:lid,qr=:qr, datum=:datum,bedrijf=:bedrijf,medewerker=:medewerker,kid=:kid,logs=:logs,typex=:typex,status=:status,checked=:checked";
	$s = $db->prepare($i);
	
	$lid = uniqid();
	$s->bindValue("lid", $lid);
	$s->bindValue("qr", $obj);
	$s->bindValue("datum", date('Y-m-d H:i:s'));
	$s->bindValue("bedrijf", $bid);
	$s->bindValue("medewerker", $mid);
	$s->bindValue("kid", $kid);
	$s->bindValue("logs", $logs);
	$s->bindValue("typex", (int)$type);
	$s->bindValue("status", (int)$status);
	$s->bindValue("checked", (int)$checked);
	
	$s->execute();
	
	return $lid;
	
}
// javaAddLog
function updateLog($db, $mid, $lid, $checked, $logs) {
	$i = "update reps set medewerker=:medewerker,logs=:logs,checked=:checked where lid=:lid";
	$s = $db->prepare($i);
	
	$s->bindValue("lid", $lid);
	$s->bindValue("medewerker", $mid);
	$s->bindValue("logs", $logs);
	$s->bindValue("checked", (int)$checked);
	
	$s->execute();
}
	
	
