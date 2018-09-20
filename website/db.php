<?php
$db_host = "mysql.dds.nl"; // prod: mysql.dds.nl
$db_database = "dwaal";
$db_username = "dwaal";
$db_password = "arjen1";

$db = new PDO('mysql:host='.$db_host.';dbname='.$db_database.';charset=utf8mb4', $db_username, $db_password);
// VERIFIED
function findMedewerker($db, $email) {
	$stmt = $db->prepare("select * from medewerkers where email=:email");
	$stmt->bindValue(":email", $email);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
function getBedrijf($db, $bid) {
	$stmt = $db->prepare("select * from bedrijven where bid=:bid");
	$stmt->bindValue(":bid", $bid, PDO::PARAM_STR);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
function getMedewerker($db, $bid, $mid) {
	$stmt = $db->prepare("select * from medewerkers where email=:mid and bid=:bid");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":mid", $mid);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
function searchMedewerkers($db, $bid, $search) {
	$select = "select * from medewerkers where bid=:bid ";
	if ($search && $search != "") {
		$select.="and (".
			"naam like :search1 ".
			"or email like :search2 ".
			"or tel like :search3".
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
function deleteMedewerker($db, $bid, $mid) {
	$stmt = $db->prepare("delete from medewerkers where bid=:bid and email=:mid");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":mid", $mid);
	$stmt->execute();
}
function insertMedewerker($db, $bid, $values, $password) {
	$stmt = $db->prepare("insert into medewerkers set bid=:bid,level=:level,naam=:naam,password=:password,email=:email,tel=:tel");
	$stmt->bindValue(":bid", $bid);

	$stmt->bindValue(":naam", $values["naam"]);
	$stmt->bindValue(":password", $password);
	$stmt->bindValue(":email", $values["email"]);
	$stmt->bindValue(":tel", $values["tel"]);
	if (isAdmin()) {
		$stmt->bindValue(":level", $values["level"]);
	} else {
		$stmt->bindValue(":level", "normaal");
	}
	$stmt->execute();
}
function updateMedewerker($db, $bid, $values) {
	
	$stmt = $db->prepare("update medewerkers set naam=:naam,tel=:tel,level=:level where bid=:bid and email=:email");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":email", $values["mid"]);

	$stmt->bindValue(":naam", $values["naam"]);
	$stmt->bindValue(":tel", $values["tel"]);
	if (isAdmin()) {
		$stmt->bindValue(":level", $values["level"]);
	} else {
		$r = getMedewerker($db, $bid, $values["mid"]);
		$stmt->bindValue(":level", $r["level"]);
	}
	$stmt->execute();
}




// OLD

/*
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
*/
/*
function getKlant($db, $bid, $kid) {
	$stmt = $db->prepare("select * from klanten where kid=:kid and bid=:bid");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":kid", $kid);
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}
*/

/*
function insertKlant($db, $bid, $values) {
	$stmt = $db->prepare("insert into klanten set kid=:kid, bid=:bid,naam=:naam,adres=:adres,huisnr=:huisnr,kamer=:kamer,postcode=:postcode,plaats=:plaats,tel=:tel,email=:email");
	$stmt->bindValue(":bid", $bid);
	$id = uniqid();
	$stmt->bindValue(":kid", $id);
	$stmt->bindValue(":naam", $values["naam"]);
	$stmt->bindValue(":adres", $values["adres"]);
	$stmt->bindValue(":huisnr", $values["huisnr"]);
	$stmt->bindValue(":kamer", $values["kamer"]);
	$stmt->bindValue(":postcode", $values["postcode"]);
	$stmt->bindValue(":plaats", $values["plaats"]);
	$stmt->bindValue(":tel", $values["tel"]);
	$stmt->bindValue(":email", $values["email"]);
	$stmt->execute();
	return $id;
}
*/
/*
function updateKlant($db, $bid, $values) {
	
	$stmt = $db->prepare("update klanten set naam=:naam,adres=:adres,huisnr=:huisnr,kamer=:kamer,postcode=:postcode,plaats=:plaats,tel=:tel,email=:email where bid=:bid and kid=:kid");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":kid", $values["kid"]);
	$stmt->bindValue(":naam", $values["naam"]);
	$stmt->bindValue(":adres", $values["adres"]);
	$stmt->bindValue(":huisnr", $values["huisnr"]);
	$stmt->bindValue(":kamer", $values["kamer"]);
	$stmt->bindValue(":postcode", $values["postcode"]);
	$stmt->bindValue(":plaats", $values["plaats"]);
	$stmt->bindValue(":tel", $values["tel"]);
	$stmt->bindValue(":email", $values["email"]);
	$stmt->execute();
}
*/
function deleteKlant($db, $bid, $kid) {
	$stmt = $db->prepare("delete from klanten where bid=:bid and kid=:kid");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":kid", $kid);
	$stmt->execute();
}

function searchObjecten($db, $bid, $search) {
	$select = "select q.qr,q.merk,q.type,q.klant,q.serienr,m.omschr,a.id,k.naam,a.aangemaakt,a.betaald,a.geldig,";
	$select .= "(select count(*) from reps r where r.qr = q.qr ) as treps ";
    $select .= "from access a ";
	$select .= "inner join qrcode q on a.qr = q.qr ";
	$select .= "inner join master_type m on q.soort = m.id ";
    $select .= "inner join klanten k on k.kid = q.klant ";
    $select .= "where a.bedrijf = :bid ";
	if ($search && $search != "") {
		$select.="and (".
			"omschr like :search1 ".
			"or merk like :search2 ".
			"or type like :search3 ".
			"or serienr like :search4".
			") ";
	}
	$select.="order by a.aangemaakt";
	$stmt = $db->prepare($select);
	$stmt->bindValue(":bid", $bid);
	if ($search && $search != "") {
		$s = "%".$search."%";
		$stmt->bindValue(":search1", $s);
		$stmt->bindValue(":search2", $s);
		$stmt->bindValue(":search3", $s);
		$stmt->bindValue(":search4", $s);
	}
	$stmt->execute();
	return $stmt->fetchAll(PDO::FETCH_ASSOC);
}

function searchKlantenByReps($db, $bid, $kid, $qr) {
	$select = "select klanten.naam,klanten.plaats,reps.id from reps ";
	$select .="inner join klanten on klanten.kid = reps.kid ";
	$select .="where reps.bedrijf=:bid ";
	if ($kid && $kid != "") {
		$select.="and klanten.kid = :kid";
	}
	$select .= " and reps.qr = :qr order by reps.id";
	
	$stmt = $db->prepare($select);
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":qr", $qr);
	if ($kid && $kid != "") {
		$stmt->bindValue(":kid", $kid);
	}
	$stmt->execute();
	return $stmt->fetch(PDO::FETCH_ASSOC);
}

/*
function getObject($db, $qr) {
	$select = "select q.*,m.omschr from qrcode q inner join master_type m on m.id = q.soort where q.qr=:qr";
	$s = $db->prepare($select);
	$s->bindValue(":qr", $qr);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}
*/
/*
function getAllMasterInvoer($db) {
	$s = $db->prepare("select * from master_invoer");
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
*/
function getRepsForReport($db, $bid, $id) {
	$select = "select * from reps where bedrijf=:bid and qr=:id order by datum";
	$s = $db->prepare($select);
	$s->bindValue(":bid", $bid);
	$s->bindValue(":id", $id);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function getFacturen($db, $bid) {
	$s = $db->prepare("select * from factuur where bedrijf=:bid order by id desc");
	$s->bindValue(":bid", $bid);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function getFactuur($db, $bid, $id) {
	$s = $db->prepare("select * from factuur where bedrijf=:bid and id=:id order by id desc");
	$s->bindValue(":bid", $bid);
	$s->bindValue(":id", $id);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}





function getObjectDetails($db, $accessId) {
	$select = "select a.id,a.aangemaakt,a.betaald,a.geldig,";
	$select .= "q.merk,q.type,q.qr,q.klant,q.serienr,q.id as idd,";
	$select .= "k.naam,k.plaats,m.omschr ";
	$select .= "from access a inner join qrcode q on q.qr = a.qr ";
	$select .= "inner join master_type m on q.soort = m.id ";
	$select .= "left join klanten k on k.kid = q.klant ";
	$select .= "where a.id=:accessId";
	$s = $db->prepare($select);
	$s->bindValue(":accessId", $accessId);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}
function getObjectKlantgegevens($db, $bid, $qr) {
	$select = "select k.*, r.datum from klanten k inner join reps r	on r.klant = k.kid where k.bid = :bid ";
	$select .= "and r.qr=:qr group by k.kid order by r.datum desc";
	$stmt = $db->prepare($select);
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":qr", $qr);
	$stmt->execute();
	return $stmt->fetchAll(PDO::FETCH_ASSOC);
}
function getObjectLogs($db, $qr) {
	$select = "select r.id,r.datum,r.status,r.bedrijf,r.logs,r.checked,q.qr,q.soort,q.merk,q.type,m.naam as medewerkernaam,";
	$select .= "b.naam as bedrijfsnaam,b.plaats as bedrijfsplaats,ms.omschr as cat ";
	$select .= "from reps r inner join qrcode q on q.qr = r.qr ";
	$select .= "left join medewerkers m on m.email = r.medewerker ";
	$select .= "left join bedrijven b on b.bid = r.bedrijf ";
	$select .= "inner join master_status ms on ms.id = r.status ";
	$select .= "where r.qr=:qr ";
	$select .= "order by r.datum desc";
	$s = $db->prepare($select);
	$s->bindValue(":qr", $qr);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function getMasterInvoer($db, $ids) {
	if (count($ids) == 0) {
		return null;
	}
	$qMarks = str_repeat('?,', count($ids) - 1) . '?';
	$s = $db->prepare("select * from master_invoer where id in ($qMarks)");
	$s->execute($ids);
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
/*
function updateQRCode($db, $id, $merk, $type, $kenmerk) {
	$u = $db->prepare("update qrcode set merk=:merk,type=:type,serienr=:serienr where id=:id");
	$u->bindValue(":id", $id);
	$u->bindValue(":merk", $merk);
	$u->bindValue(":type", $type);
	$u->bindValue(":serienr", $kenmerk);
	$u->execute();
}
*/
function searchLogs($db, $bid, $search) {
	$select = "select ";
	$select .= "r.id,r.datum,r.status,r.bedrijf,r.logs,r.checked,";
	$select .= "q.qr,q.soort,q.merk,q.type,q.klant,";
	$select .= "m.naam as medewerkernaam,b.naam as bedrijfsnaam,b.plaats as bedrijfsplaats,";
	$select .= "ms.omschr as cat,k.naam,k.plaats ";
	$select .= "from reps r inner join qrcode q on q.qr = r.qr ";
	$select .= "left join medewerkers m on m.email = r.medewerker ";
	$select .= "left join bedrijven b on b.bid = r.bedrijf ";
	$select .= "inner join master_status ms on ms.id = r.status ";
	$select .= "left join klanten k on k.kid = r.klant ";
	$select .= "where b.bid=:bid ";
	if ($search && $search != "") {
		$select .= "and (k.plaats like :search1 or q.merk like :search2 or q.type like :search3) ";
	}
	$select .= "order by r.datum desc";
	$s = $db->prepare($select);
	$s->bindValue(":bid", $bid);
	if ($search && $search != "") {
		$search = "%".$search."%";
		$s->bindValue(":search1", $search);
		$s->bindValue(":search2", $search);
		$s->bindValue(":search3", $search);
	}
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function deleteLog($db, $bid, $id) {
	$stmt = $db->prepare("delete from reps where bedrijf=:bid and id=:id");
	$stmt->bindValue(":bid", $bid);
	$stmt->bindValue(":id", $id);
	$stmt->execute();
}
function getReps($db, $id) {
	$s = $db->prepare("select * from reps where id = :id");
	$s->bindValue(":id", $id);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}
function updateReps($db, $id, $klant, $oo) {
	$s = $db->prepare("update reps set logs=:oo,klant=:klant where id=:id");
	$s->bindValue(":oo", $oo);
	$s->bindValue(":klant", $klant);
	$s->bindValue(":id", $id);
	$s->execute();
}

function getLogItems($db, $bid) {
	$s = $db->prepare("select m.* from master_type m inner join users_actief u on u.user_type = m.id where u.bedrijf = :bid group by u.user_type order by m.omschr");
	$s->bindValue(":bid", $bid);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function getMasterLogItem($db, $id) {
	$s = $db->prepare("select * from master_type where id = :id");
	$s->bindValue(":id", $id);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}
function getMasterCategories($db) {
	$s = $db->prepare("select * from master_cats order by omschr");
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function getMasterStatusByCategory($db, $bid, $id, $idx) { 
	$select = "select m.*,";
	$select .= "(select COUNT(*) from users_actief u where bedrijf=:bid and user_type=:id  and user_status = m.id) as totaal2 ";
	$select .= "from master_status m where m.master_type=:idx order by m.omschr";
	$s = $db->prepare($select);
	$s->bindValue(":bid", $bid);
	$s->bindValue(":id", $id);
	$s->bindValue(":idx", $idx);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}	
function getMasterStatus($db, $id) {
	$s = $db->prepare("select * from master_status where id = :id");
	$s->bindValue(":id", $id);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}
function getMasterInvoerByCat($db, $cat, $id, $bid) {
	$select = "select m.*,(select COUNT(*) from users_actief where user_type=:cat and user_status = :id and user_invoer = m.id and bedrijf=:bid) as totaal";
    $select .= " from master_invoer m order by m.omschr";
	$s = $db->prepare($select);
	$s->bindValue(":cat", $cat);
	$s->bindValue(":bid", $bid);
	$s->bindValue(":id", $id);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function insertUsersActief($db, $bid, $user_type, $user_status, $user_invoer) {
	$s = $db->prepare("insert into users_actief set bedrijf=:bid,user_type=:user_type,user_status=:user_status,user_invoer=:user_invoer");
	$s->bindValue(":bid", $bid);
	$s->bindValue(":user_type", $user_type);
	$s->bindValue(":user_status", $user_status);
	$s->bindValue(":user_invoer", $user_invoer);
	$s->execute();
}
function deleteUsersActiefFull($db, $bid, $user_type, $user_status, $user_invoer) {
	$s = $db->prepare("delete from users_actief where bedrijf=:bid and user_type=:user_type and user_status=:user_status and user_invoer=:user_invoer");
	$s->bindValue(":bid", $bid);
	$s->bindValue(":user_type", $user_type);
	$s->bindValue(":user_status", $user_status);
	$s->bindValue(":user_invoer", $user_invoer);
	$s->execute();
}
function getMasterLogItems($db, $bid) {
	$s = $db->prepare("select m.*,(select COUNT(*) from users_actief where user_type=m.id and bedrijf=:bid) as totaal from master_type m order by m.omschr");
	$s->bindValue(":bid", $bid);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function getDefaultActiefForMasterLogItem($db, $id) {
	$s = $db->prepare("select * from default_actief where master_type=:id");
	$s->bindValue(":id", $id);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function purgeUsersActiefForBedrijf($db, $bid, $id) {
	$s = $db->prepare("delete from users_actief where bedrijf=:bid and user_type=:id");
	$s->bindValue(":bid", $bid);
	$s->bindValue(":id", $id);
	$s->execute();
}
function deleteUsersActief($db, $bid, $id) {
	$s = $db->prepare("delete from users_actief where bedrijf=:bid and user_type=:id");
	$s->bindValue(":bid", $bid);
	$s->bindValue(":id", $id);
	$s->execute();
}



// Administratie
function getBedrijven($db) {
	$select = "select * from bedrijven order by naam desc";
	$s = $db->prepare($select);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function getAllObjecten($db) {
	$select = "select q.*,m.omschr,k.naam as klantnaam,b.naam as bedrijfsnaam, b.plaats from qrcode q inner join master_type m on m.id = q.soort inner join klanten k on k.kid = q.klant inner join bedrijven b on b.bid = k.bid order by id desc";
	$s = $db->prepare($select);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}	
function getAllLogs($db) {
	$select = "select r.*,q.merk,q.type,b.naam,b.plaats,m.omschr from reps r inner join qrcode q on q.qr = r.qr inner join bedrijven b on b.bid = r.bedrijf inner join master_status m on m.id = r.status order by datum desc";
	$s = $db->prepare($select);
	$s->execute();
	return $s->fetchAll(PDO::FETCH_ASSOC);
}
function getSubCategory($db, $id) {
	$select = "select * from subcat where id=:id";
	$s = $db->prepare($select);
	$s->bindValue(":id", $id);
	$s->execute();
	return $s->fetch(PDO::FETCH_ASSOC);
}

