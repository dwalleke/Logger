<?php
function mailPassword($email, $naam, $password) {
	$to      = $email;
	$subject = 'Uw wachtwoord voor ORSIT';
	$message = $naam.', welkom bij ORSIT! Uw kunt inloggen met dit wachtwoord: '.$password;
	$headers = 'From: info@orsit.nl' . "\r\n" .
		'Reply-To: info@orsit.nl' . "\r\n" .
		'X-Mailer: PHP/' . phpversion();
	mail($to, $subject, $message, $headers);

}
?>