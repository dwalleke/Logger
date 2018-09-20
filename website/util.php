<?php

function hasKey($val, $arr) {
	return in_array($val, array_keys($arr));
}
function hasValue($val, $arr) {
	if (in_array($val, array_keys($arr))) {
		if (strlen($arr[$val]) > 0) {
			return true;
		}
	}
	return false;
}
function isAdmin() {
	if (hasKey("level", $_SESSION)) {
		return $_SESSION["level"] == "admin";
	}
	return false;
}	
function removeFromSession($val) {
	if (hasKey($val, $_SESSION)) {
		$_SESSION = array_diff($_SESSION, [$_SESSION[$val]]);
	}
}

function ready() {
	return hasKey("bid", $_SESSION) && hasKey("mid", $_SESSION);
}
?>
