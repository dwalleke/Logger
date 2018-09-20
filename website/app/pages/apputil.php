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
?>
