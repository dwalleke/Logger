<?php

include("../../util.php");
$data = (object) array();
$data->error = "";

if (!hasValue("postcode", $_POST)) {
	$data->error .= "U heeft geen postcode ingevuld.\n";
}
if (!hasValue("huisnr", $_POST)) {
	$data->error .= "U heeft geen huisnummer ingevuld.\n";
}
if ($data->error == "") {
	$postcode = $_POST['postcode'];
	$huisnr = $_POST['huisnr'];
	// De headers worden altijd meegestuurd als array
	$headers = array();
	$headers[] = 'X-Api-Key: r9Zdpg7p8nt01NtAilJE341qqT2PUQ4aTxg4YrZ1';

	// De URL naar de API call
	$url = 'https://api.postcodeapi.nu/v2/addresses/?postcode='.$postcode.'&number='.$huisnr;

	$curl = curl_init($url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

	// Indien de server geen TLS ondersteunt kun je met 
	// onderstaande optie een onveilige verbinding forceren.
	// Meestal is dit probleem te herkennen aan een lege response.
	//curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

	// De ruwe JSON response
	$response = curl_exec($curl);

	// Gebruik json_decode() om de response naar een PHP array te converteren
	$data = json_decode($response);
	curl_close($curl);
	if (!array_key_exists("error", $data)) {
		if(count($data->_embedded->addresses) == 1) {
			$location = $data->_embedded->addresses[0];
			$data->html = "<b>".$location->street." ".$huisnr."</b><br />";
			$data->html .= $location->city->label;
			$data->adres = $location->street;
			$data->plaats = $location->city->label;
		}
	}
}
echo json_encode($data);
?>