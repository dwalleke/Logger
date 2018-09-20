<?php
session_start();
include("../db.php");
include("../util.php");

$data = (object) array();
$data->error = "";
$data->forbidden = "";
$data->html = "";
if (ready()) {		
	$data->html .="<div class='box1' style='background:#f0f0f0;border-top:1px solid #fff;border-bottom:1px solid #ddd'>";
		$data->html .="<span class='oswald f24 bold blue'>DASHBOARD</span><br/>";
	$data->html .="</div>";
	$data->html .="<div class='box1'  style='background:#fafafa;border-top:1px solid #fff;border-bottom:1px solid #ddd'>";
		$data->html .="<div style='position:relative;overflow:hidden'>";
		$r = getBedrijf($db, $_SESSION["bid"]);
		if ( $r && count($r) > 0 ) {
			
			$b_naam = $r['naam'];
			$b_adres = $r['adres'];
			$b_huisnr = $r['huisnr'];
			$b_postcode = $r['postcode'];
			$b_plaats = $r['plaats'];
			$b_tel = $r['tel'];
			$b_email = $r['email'];
			$b_domein = $r['domein'];
			$b_kvk = $r['kvk'];
			$b_btw = $r['btw'];

			$data->html .="<div style='float:left;width:50%'>";
				$data->html .="<span class='oxygen f20 lgrey bold'>Bedrijfsgegevens</span><br/><br/><br/>";
				$data->html .="<span class='oxygen f14 grey'>".$b_naam."<br/>".$b_adres." ".$b_huisnr."<br/>".$b_postcode." ".$b_plaats."<br/>";
				$data->html .="Tel: ".$b_tel."<br/>";
				$data->html .="Email: ".$b_email."<br/>";
				$data->html .="Domein: <a target='_new' href='".$b_domein."'>".$b_domein."</a><br/>";
				$data->html .="KVK nummer: ".$b_kvk."<br/>";
				$data->html .="BTW nummer: ".$b_btw."<br/>";
				$data->html .="</span>";

			$data->html .="</div>";
		}
		$m = getMedewerker($db, $_SESSION["bid"], $_SESSION["mid"]);
		if ($m && count($m) > 0) {
			$m_naam = $m['naam'];
			$m_level = $m['level'];
			$m_email = $m['email'];
			$m_tel = $m['tel'];

			$data->html .="<div style='float:right;width:50%'>";
				$data->html .="<span class='oxygen f20 lgrey bold'>Uw gegevens</span><br/><br/><br/>";
				$data->html .="<span class='oxygen f14 grey'>Naam: ".$m_naam."<br/>Toegangsniveau: ".$m_level."<br/>E-mailadres: ".$m_email."<br/>Telefoonnummer: ".$m_tel."</span>";

			$data->html .="</div>";
		}
		$data->html .="</div>";
	$data->html .="</div>";
} else {
	$data->forbidden = "true";
}
echo json_encode($data);
?>
